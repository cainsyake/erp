package bobo.erp.service.running;

import bobo.erp.domain.rule.Rule;
import bobo.erp.domain.state.FactoryState;
import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.factory.LineState;
import bobo.erp.domain.state.finance.DebtState;
import bobo.erp.domain.state.finance.FinancialStatement;
import bobo.erp.domain.state.marketing.AdvertisingState;
import bobo.erp.domain.state.stock.ProductState;
import bobo.erp.repository.state.RunningStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
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
    public RunningState advertising(String username, AdvertisingState advertisingState){
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
            Iterator<DebtState> debtStateIterator = debtStateList.iterator();
            while (debtStateIterator.hasNext()){
                DebtState debtState = debtStateIterator.next();
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
                        debtStateIterator.remove();     //删除数据库中的相应记录
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

            FinancialStatement financialStatement = new FinancialStatement();   //投广告时新建一个财务报表
            financialStatement.setAdvertisingCost(advertisingState.getAd0());
            financialStatement.setYear(timeYear);
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

    @Transactional
    public RunningState applyDebt(String username, DebtState debtState){
        //TODO: 此处需要再次验证贷款额度以及申请额
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        balance += debtState.getAmounts();
        runningState.getFinanceState().setCashAmount(balance);  //修改现金余额
        runningState.getFinanceState().getDebtStateList().add(debtState);   //存入贷款记录表
        logger.info("用户：{} 申请贷款：{}W 类型：{} 还款期：{}", username, debtState.getAmounts(), debtState.getDebtType(), debtState.getRepaymentPeriod());
//        List<DebtState> debtStateList = runningState.getFinanceState().getDebtStateList();
//        debtStateList.add(debtState);
        return runningState;
    }

    @Transactional
    public RunningState startYear(String username){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        runningState.getBaseState().setTimeQuarter(1); //跳转至第一季度
        runningState.getBaseState().setState(10);   //跳转至季度开始前
        runningState.getBaseState().getOperateState().setLongLoan(1);   //明年投广告前关闭长贷申请
        runningState.getBaseState().getOperateState().setOrderMeeting(1);   //明年投广告前关闭订货会
        runningState.getBaseState().getOperateState().setBidMeeting(1);   //明年投广告前关闭竞单会
        return runningState;
    }

    @Transactional
    public RunningState starQuarter(String username){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);

        //时间轴变换
        runningState.getBaseState().setState(11);   //跳转至季初
        runningState.getBaseState().getOperateState().setShortLoan(0);  //允许申请短贷

        /**
         * 自动进行的业务操作：
         * 1.更新短贷
         * 2.更新生产
         * 3.更新建设
         */
        Integer balance = runningState.getFinanceState().getCashAmount();
        Integer timeYear = runningState.getBaseState().getTimeYear();
        if( timeYear > 1){
            List<DebtState> debtStateList = runningState.getFinanceState().getDebtStateList();
            List<FinancialStatement> financialStatementList = runningState.getFinanceState().getFinancialStatementList();
            Iterator<DebtState> debtStateIterator = debtStateList.iterator();
            while (debtStateIterator.hasNext()){
                DebtState debtState = debtStateIterator.next();
                if (debtState.getDebtType() == 1){
                    if(debtState.getRepaymentPeriod() == 1){
                        Integer interest = (int)Math.round(debtState.getAmounts() * rule.getRuleParam().getParamShortTermLoanRates());
                        balance -= interest;   //扣除短贷利息
                        for(FinancialStatement financialStatement : financialStatementList){
                            if(financialStatement.getYear() == timeYear){
                                financialStatement.setFinancialCost(financialStatement.getFinancialCost() + interest);   //记录利息至财务报表中
                            }
                        }
                        balance -= debtState.getAmounts();  //归还到期短贷本金
                        debtStateIterator.remove(); //从数据库中删除相应的记录
                    }else {
                        debtState.setRepaymentPeriod(debtState.getRepaymentPeriod()-1);
                    }
                }
            }
        }

        List<ProductState> productStateList = runningState.getStockState().getProductStateList();
        Iterator<ProductState> productStateIterator = productStateList.iterator();
        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Iterator<FactoryState> factoryStateIterator = factoryStateList.iterator();
        while (factoryStateIterator.hasNext()){
            List<LineState> lineStateList = factoryStateIterator.next().getLineStateList();
            Iterator<LineState> lineStateIterator = lineStateList.iterator();
            while (lineStateIterator.hasNext()){
                LineState lineState = lineStateIterator.next();

                //更新生产
                if(lineState.getProduceState() == 1){
                    Integer productType = lineState.getProductType();
                    while (productStateIterator.hasNext()){
                        ProductState productState = productStateIterator.next();
                        if (productState.getType() == productType){
                            productState.setQuantity(productState.getQuantity() + 1);   //增加产品库存
                        }
                    }
                    for(int i = 1; i < 6; i++){
                    }
                }
                if(lineState.getProduceState() > 0){
                    lineState.setProduceState(lineState.getProduceState() - 1); //更新生产状态
                }

                //更新生产线建设
                if(lineState.getOwningState() == 0){
                    lineState.setOwningState(1);    //安装完成
                }
                if (lineState.getProduceState() == -1){
                    lineState.setProduceState(0);   //转产完成
                }
            }
        }



        return runningState;
    }

}
