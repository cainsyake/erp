package bobo.erp.service.running;

import bobo.erp.domain.rule.Rule;
import bobo.erp.domain.rule.RuleLine;
import bobo.erp.domain.rule.RuleMaterial;
import bobo.erp.domain.state.FactoryState;
import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.factory.LineState;
import bobo.erp.domain.state.finance.DebtState;
import bobo.erp.domain.state.finance.FinancialStatement;
import bobo.erp.domain.state.marketing.AdvertisingState;
import bobo.erp.domain.state.stock.ProductState;
import bobo.erp.domain.state.stock.PurchaseState;
import bobo.erp.repository.state.RunningStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

            runningState.getBaseState().setMsg(""); //清空MSG

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
        if(debtState.getDebtType() == 1){
            runningState.getBaseState().getOperateState().setShortLoan(1);
        }
        logger.info("用户：{} 申请贷款：{}W 类型：{} 还款期：{}", username, debtState.getAmounts(), debtState.getDebtType(), debtState.getRepaymentPeriod());
        runningState.getBaseState().setMsg(""); //清空MSG
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
                        if(balance < 0){
                            runningState.getBaseState().setMsg("现金不足");
                            return runningState;
                        }
                        for(FinancialStatement financialStatement : financialStatementList){
                            if(financialStatement.getYear() == timeYear){
                                financialStatement.setFinancialCost(financialStatement.getFinancialCost() + interest);   //记录利息至财务报表中
                            }
                        }
                        balance -= debtState.getAmounts();  //归还到期短贷本金
                        debtStateIterator.remove(); //从数据库中删除相应的记录
                        if(balance < 0){
                            runningState.getBaseState().setMsg("现金不足");
                            return runningState;
                        }
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
        runningState.getBaseState().setMsg(""); //清空MSG
        return runningState;
    }

    @Transactional
    public RunningState updatePurchase(String username){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        List<PurchaseState> purchaseStateList = runningState.getStockState().getPurchaseStateList();
        Iterator<PurchaseState> purchaseStateIterator = purchaseStateList.iterator();
        Integer balance = runningState.getFinanceState().getCashAmount();
        Integer purchaseTotalAmounts = 0;
        while (purchaseStateIterator.hasNext()){
            PurchaseState purchaseState = purchaseStateIterator.next();
            Integer tempType = purchaseState.getType();
            Integer tempQuantity = purchaseState.getQuantity();
            Integer tempDeliveryTime = purchaseState.getDeliveryTime();
            if(tempDeliveryTime == 1){
                if(tempType == 1){
                    Integer price = rule.getRuleMaterial().getMaterial1Price();
                    purchaseTotalAmounts += tempQuantity * price;
                }
                if(tempType == 2){
                    Integer price = rule.getRuleMaterial().getMaterial2Price();
                    purchaseTotalAmounts += tempQuantity * price;
                }
                if(tempType == 3){
                    Integer price = rule.getRuleMaterial().getMaterial3Price();
                    purchaseTotalAmounts += tempQuantity * price;
                }
                if(tempType == 4){
                    Integer price = rule.getRuleMaterial().getMaterial4Price();
                    purchaseTotalAmounts += tempQuantity * price;
                }
                if(tempType == 5){
                    Integer price = rule.getRuleMaterial().getMaterial5Price();
                    purchaseTotalAmounts += tempQuantity * price;
                }
                if(purchaseTotalAmounts > balance){
                    runningState.getBaseState().setMsg("现金不足");
                    return runningState;
                }else {
                    balance -= purchaseTotalAmounts;
                    purchaseStateIterator.remove();     //从数据库中删除相应采购记录
                }

            }else {
                purchaseState.setDeliveryTime(tempDeliveryTime - 1); ;     //更新收货期
            }
        }

        //时间轴变换
        runningState.getBaseState().setState(12);   //跳转至季中
        runningState.getBaseState().getOperateState().setShortLoan(0);  //允许申请短贷 解控
        runningState.getBaseState().getOperateState().setAddPurchase(0);
        runningState.getBaseState().getOperateState().setBuildLine(0);
        runningState.getBaseState().getOperateState().setContinueChange(0);
        runningState.getBaseState().getOperateState().setSaleLine(0);
        runningState.getBaseState().getOperateState().setBeginProduction(0);

        runningState.getBaseState().setMsg(""); //清空MSG
        return runningState;
    }

    @Transactional
    public RunningState addPurchase(String username, List<Integer> list){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        RuleMaterial ruleMaterial = rule.getRuleMaterial();
        Integer listSize = list.size();
        List<PurchaseState> purchaseStateList = runningState.getStockState().getPurchaseStateList();
        if (listSize > 0 && list.get(0) > 0){
            PurchaseState purchaseState = new PurchaseState();
            purchaseState.setType(1);
            purchaseState.setQuantity(list.get(0));
            purchaseState.setDeliveryTime(ruleMaterial.getMaterial1Time());
            purchaseStateList.add(purchaseState);
        }
        if (listSize > 1 && list.get(1) > 0){
            PurchaseState purchaseState = new PurchaseState();
            purchaseState.setType(2);
            purchaseState.setQuantity(list.get(1));
            purchaseState.setDeliveryTime(ruleMaterial.getMaterial2Time());
            purchaseStateList.add(purchaseState);
        }
        if (listSize > 2 && list.get(2) > 0){
            PurchaseState purchaseState = new PurchaseState();
            purchaseState.setType(3);
            purchaseState.setQuantity(list.get(2));
            purchaseState.setDeliveryTime(ruleMaterial.getMaterial3Time());
            purchaseStateList.add(purchaseState);
        }
        if (listSize > 3 && list.get(3) > 0){
            PurchaseState purchaseState = new PurchaseState();
            purchaseState.setType(4);
            purchaseState.setQuantity(list.get(3));
            purchaseState.setDeliveryTime(ruleMaterial.getMaterial4Time());
            purchaseStateList.add(purchaseState);
        }
        if (listSize > 4 && list.get(4) > 0){
            PurchaseState purchaseState = new PurchaseState();
            purchaseState.setType(5);
            purchaseState.setQuantity(list.get(4));
            purchaseState.setDeliveryTime(ruleMaterial.getMaterial5Time());
            purchaseStateList.add(purchaseState);
        }

        runningState.getBaseState().getOperateState().setAddPurchase(1);    //时间轴：关闭采购原料

        return runningState;
    }

    @Transactional
    public RunningState newFactory(String username, FactoryState factoryState){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        if(rule.getRuleParam().getParamFactoryMaxNum() < runningState.getFactoryStateList().size()){
            runningState.getBaseState().setMsg("厂房数量已经达到上限");
            return runningState;
        }
        Integer value = 0;
        Integer balance = runningState.getFinanceState().getCashAmount();
        if (factoryState.getType() == 1){
            if (factoryState.getOwningState() == 0){
                value = rule.getRuleFactory().getFactory1RentPrice();
            }else {
                value = rule.getRuleFactory().getFactory1BuyPrice();
            }
        }
        if (factoryState.getType() == 2){
            if (factoryState.getOwningState() == 0){
                value = rule.getRuleFactory().getFactory2RentPrice();
            }else {
                value = rule.getRuleFactory().getFactory2BuyPrice();
            }
        }
        if (factoryState.getType() == 3){
            if (factoryState.getOwningState() == 0){
                value = rule.getRuleFactory().getFactory3RentPrice();
            }else {
                value = rule.getRuleFactory().getFactory3BuyPrice();
            }
        }
        if (factoryState.getType() == 4){
            if (factoryState.getOwningState() == 0){
                value = rule.getRuleFactory().getFactory4RentPrice();
            }else {
                value = rule.getRuleFactory().getFactory4BuyPrice();
            }
        }if (factoryState.getType() == 5){
            if (factoryState.getOwningState() == 0){
                value = rule.getRuleFactory().getFactory5RentPrice();
            }else {
                value = rule.getRuleFactory().getFactory5BuyPrice();
            }
        }
        balance -= value;
        if (balance < 0){
            runningState.getBaseState().setMsg("现金不足");
        }else {
            runningState.getFinanceState().setCashAmount(balance);
            factoryState.setValue(value);
            factoryState.setContent(0);
            factoryState.setFinalPaymentTime(runningState.getBaseState().getTimeYear() * 10 + runningState.getBaseState().getTimeQuarter());    //十位是年份，个位是季度
            List<LineState> lineStateList = new ArrayList<LineState>();
            factoryState.setLineStateList(lineStateList);
            runningState.getFactoryStateList().add(factoryState);
            runningState.getBaseState().setMsg(""); //清空MSG
            logger.info("购租厂房，Type:{}，Owning:{}", factoryState.getType(), factoryState.getOwningState());
        }
        return runningState;
    }

    @Transactional
    public RunningState newLine(String username, Integer factoryId, LineState lineState){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Iterator<FactoryState> factoryStateIterator = factoryStateList.iterator();
        while (factoryStateIterator.hasNext()){
            FactoryState factoryState = factoryStateIterator.next();
            if(factoryState.getId() == factoryId){  //找到放置的厂房
                Integer lineType = lineState.getType();
                Integer lineUnitInvest = 0;
                Integer line1InstalTime = 0;
                switch (lineType){
                    case 1:
                        lineUnitInvest = rule.getRuleLine().getLine1UnitInvest();
                        line1InstalTime = rule.getRuleLine().getLine1InstallTime();
                        break;
                    case 2:
                        lineUnitInvest = rule.getRuleLine().getLine2UnitInvest();
                        line1InstalTime = rule.getRuleLine().getLine2InstallTime();
                        break;
                    case 3:
                        lineUnitInvest = rule.getRuleLine().getLine3UnitInvest();
                        line1InstalTime = rule.getRuleLine().getLine3InstallTime();
                        break;
                    case 4:
                        lineUnitInvest = rule.getRuleLine().getLine4UnitInvest();
                        line1InstalTime = rule.getRuleLine().getLine4InstallTime();
                        break;
                    case 5:
                        lineUnitInvest = rule.getRuleLine().getLine5UnitInvest();
                        line1InstalTime = rule.getRuleLine().getLine5InstallTime();
                        break;
                    default:
                        break;
                }
                balance -= lineUnitInvest;
                if (balance < 0){
                    runningState.getBaseState().setMsg("现金不足");
                }else {
                    runningState.getFinanceState().setCashAmount(balance);
                    lineState.setValue(lineUnitInvest);
                    lineState.setOwningState( 1 - line1InstalTime);
                    factoryState.getLineStateList().add(lineState);
                    factoryState.setContent(factoryState.getLineStateList().size());
                    runningState.getBaseState().setMsg(""); //清空MSG
                    logger.info("新建生产线成功");
                }
            }
        }
        return runningState;
    }

    @Transactional
    public RunningState buildLine(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        RuleLine ruleLine = rule.getRuleLine();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
//            System.out.println("测试，对比数组中的值：" + list.get(i));
        }

        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Iterator<FactoryState> factoryStateIterator = factoryStateList.iterator();
        while (factoryStateIterator.hasNext()){
            FactoryState factoryState = factoryStateIterator.next();
            if(factoryState.getContent() != 0){
                List<LineState> lineStateList = factoryState.getLineStateList();
                Iterator<LineState> lineStateIterator = lineStateList.iterator();
                while (lineStateIterator.hasNext()){
                    LineState lineState = lineStateIterator.next();
                    if(lineState.getOwningState() < 0){
                        Integer lineId = lineState.getId();
//                        System.out.println("测试，遍历生产线ID：" + lineId);
                        Iterator<Integer> iterator = list.iterator();
                        while (iterator.hasNext()){
                            Integer tempid = iterator.next();
//                            System.out.println("测试，遍历tempID：" + tempid);
                            if (lineId == tempid){
//                                System.out.println("测试，通过比对测试，ID：" + lineId);
                                if(lineState.getType() == 1){
                                    balance -= ruleLine.getLine1UnitInvest();
                                }
                                if(lineState.getType() == 2){
                                    balance -= ruleLine.getLine2UnitInvest();
                                }
                                if(lineState.getType() == 3){
                                    balance -= ruleLine.getLine3UnitInvest();
                                }
                                if(lineState.getType() == 4){
                                    balance -= ruleLine.getLine4UnitInvest();
                                }
                                if(lineState.getType() == 5){
                                    balance -= ruleLine.getLine5UnitInvest();
                                }
                                lineState.setOwningState(lineState.getOwningState() + 1);
                            }
                        }
                    }
                }
            }
        }
        if(balance < 0){
            runningState.getBaseState().setMsg("现金不足");
        }else {
            runningState.getFinanceState().setCashAmount(balance);
            runningState.getBaseState().setMsg("");

            runningState.getBaseState().getOperateState().setBuildLine(1); //时间轴： 关闭在建生产线
        }
        return runningState;
    }
}
