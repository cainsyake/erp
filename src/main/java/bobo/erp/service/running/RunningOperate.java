package bobo.erp.service.running;

import bobo.erp.domain.rule.Rule;
import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.finance.DebtState;
import bobo.erp.domain.state.finance.FinancialStatement;
import bobo.erp.domain.state.marketing.AdvertisingState;
import bobo.erp.repository.state.RunningStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 59814 on 2017/7/30.
 */
@Service
public class RunningOperate {
    private Logger logger = LoggerFactory.getLogger(RunningOperate.class);

    @Autowired
    private GetSubRunningStateService getSubRunningStateService;
    @Autowired
    private GetTeachClassRuleService getTeachClassRuleService;

    @Autowired
    private RunningStateRepository runningStateRepository;

    @Transactional
    public RunningState advertising(AdvertisingState advertisingState, String username){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        advertisingState.setAd26(advertisingState.getAd1()+advertisingState.getAd6()+advertisingState.getAd11()+advertisingState.getAd16()+advertisingState.getAd21());
        advertisingState.setAd27(advertisingState.getAd2()+advertisingState.getAd7()+advertisingState.getAd12()+advertisingState.getAd17()+advertisingState.getAd22());
        advertisingState.setAd28(advertisingState.getAd3()+advertisingState.getAd8()+advertisingState.getAd13()+advertisingState.getAd18()+advertisingState.getAd23());
        advertisingState.setAd29(advertisingState.getAd4()+advertisingState.getAd9()+advertisingState.getAd14()+advertisingState.getAd19()+advertisingState.getAd24());
        advertisingState.setAd30(advertisingState.getAd5()+advertisingState.getAd10()+advertisingState.getAd15()+advertisingState.getAd20()+advertisingState.getAd25());
        advertisingState.setAd0(advertisingState.getAd26()+advertisingState.getAd27()+advertisingState.getAd28()+advertisingState.getAd29()+advertisingState.getAd30());

        Integer balance = runningState.getFinanceState().getCashAmount();

        Integer timeYear = runningState.getBaseState().getTimeYear();
        if(timeYear > 1){
            List<FinancialStatement> financialStatementList = runningState.getFinanceState().getFinancialStatementList();
            for(FinancialStatement financialStatement : financialStatementList){
                if(financialStatement.getYear() == timeYear - 1){
                    balance -= financialStatement.getIncomeTax();   //扣除应缴税金
                }
            }

            List<DebtState> debtStateList = runningState.getFinanceState().getDebtStateList();
            for (DebtState debtState : debtStateList){
                if (debtState.getDebtType() == 2){
                    Integer interest = (int)Math.round(debtState.getAmounts() * rule.getRuleParam().getParamLongTermLoanRates());
                    balance -= interest;   //扣除长贷利息
                    for(FinancialStatement financialStatement : financialStatementList){
                        if(financialStatement.getYear() == timeYear){
                            financialStatement.setFinancialCost(financialStatement.getFinancialCost() + interest);   //记录利息至财务报表中
                        }
                    }
                    if(debtState.getRepaymentPeriod() == 1){
                        balance -= debtState.getAmounts();  //归还到期长贷本金
                    }else {
                        debtState.setRepaymentPeriod(debtState.getRepaymentPeriod()-1);
                    }
                }
            }
        }

        balance -= advertisingState.getAd0() ;
        if (balance < 0){
            runningState.getBaseState().setMsg("现金不足");
            logger.info("现金不足警告");
        }else {
            runningState.getFinanceState().setCashAmount(balance);
            advertisingState.setYear(runningState.getBaseState().getTimeYear());
            runningState.getMarketingState().getAdvertisingStateList().add(advertisingState);   //保存数据至广告记录

            FinancialStatement financialStatement = new FinancialStatement();
            financialStatement.setAdvertisingCost(advertisingState.getAd0());
            runningState.getFinanceState().getFinancialStatementList().add(financialStatement); //保存数据至财务报表

            runningState.getBaseState().getOperateState().setAd(1); //时间轴：关闭广告投放
            runningState.getBaseState().getOperateState().setOrderMeeting(0);   //时间轴：允许订货会
            runningState.getBaseState().getOperateState().setLongLoan(0);   //时间轴：允许长贷申请

            logger.info("最新余额：{}", runningState.getFinanceState().getCashAmount());
        }
        logger.info("测试广告总额：{}", advertisingState.getAd0());
        return runningState;
//        return runningStateRepository.save(runningState);
    }
}
