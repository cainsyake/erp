package bobo.erp.service.running;

import bobo.erp.domain.rule.*;
import bobo.erp.domain.state.FactoryState;
import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.dev.MarketDevState;
import bobo.erp.domain.state.dev.ProductDevState;
import bobo.erp.domain.state.dev.QualificationDevState;
import bobo.erp.domain.state.factory.LineState;
import bobo.erp.domain.state.finance.DebtState;
import bobo.erp.domain.state.finance.FinancialStatement;
import bobo.erp.domain.state.finance.ReceivableState;
import bobo.erp.domain.state.marketing.AdvertisingState;
import bobo.erp.domain.state.marketing.OrderState;
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
    private OperateFinancialStatementService operateFinancialStatementService;

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
            Integer tax = operateFinancialStatementService.readWithTime((timeYear - 1), "incomeTax", runningState);
            if(tax > balance){
                runningState.getBaseState().setMsg("现金不足");
                return runningState;
            }else {
                balance -= tax;
                operateFinancialStatementService.write("incomeTax", tax, runningState);
            }
//            List<FinancialStatement> financialStatementList = runningState.getFinanceState().getFinancialStatementList();
//            for(FinancialStatement financialStatement : financialStatementList){
//                if(financialStatement.getYear() == timeYear - 1){
//                    balance -= financialStatement.getIncomeTax();   //扣除应缴税金
//                }
//            }

            List<DebtState> debtStateList = runningState.getFinanceState().getDebtStateList();
            Iterator<DebtState> debtStateIterator = debtStateList.iterator();
            while (debtStateIterator.hasNext()){
                DebtState debtState = debtStateIterator.next();
                if (debtState.getDebtType() == 2){
                    Integer interest = (int)Math.round(debtState.getAmounts() * rule.getRuleParam().getParamLongTermLoanRates());
                    balance -= interest;   //扣除长贷利息
                    operateFinancialStatementService.write("financialCost", operateFinancialStatementService.read("financialCost", runningState) + interest, runningState);
//                    for(FinancialStatement financialStatement : financialStatementList){
//                        if(financialStatement.getYear() == timeYear){
//                            financialStatement.setFinancialCost(financialStatement.getFinancialCost() + interest);   //记录利息至财务报表中
//                        }
//                    }
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

            if(timeYear > 1){
                FinancialStatement financialStatement = new FinancialStatement();   //投广告时新建一个财务报表
                financialStatement.setAdvertisingCost(advertisingState.getAd0());
                financialStatement.setYear(timeYear);
                runningState.getFinanceState().getFinancialStatementList().add(financialStatement); //保存数据至财务报表
            }else if(timeYear == 1){
                operateFinancialStatementService.write("advertisingCost", advertisingState.getAd0(), runningState);
                
//                List<FinancialStatement> financialStatementList = runningState.getFinanceState().getFinancialStatementList();
//                Iterator<FinancialStatement> financialStatementIterator = financialStatementList.iterator();
//                while (financialStatementIterator.hasNext()){
//                    FinancialStatement financialStatement = financialStatementIterator.next();
//                    if(financialStatement.getYear() == timeYear){
//                        financialStatement.setAdvertisingCost(advertisingState.getAd0());
//                    }
//                }
            }

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
    public RunningState startQuarter(String username){
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
        List<DebtState> debtStateList = runningState.getFinanceState().getDebtStateList();
        List<FinancialStatement> financialStatementList = runningState.getFinanceState().getFinancialStatementList();
        Iterator<DebtState> debtStateIterator = debtStateList.iterator();
        while (debtStateIterator.hasNext()){
            DebtState debtState = debtStateIterator.next();
            if (debtState.getDebtType() == 1){
                if(debtState.getRepaymentPeriod() == 1){
                    Integer interest = (int)Math.round(debtState.getAmounts() * rule.getRuleParam().getParamShortTermLoanRates());
                    logger.info("测试扣除利息前的余额：{}", balance);
                    balance -= interest;   //扣除短贷利息
                    if(balance < 0){
                        runningState.getBaseState().setMsg("现金不足");
                        return runningState;
                    }
                    //记录利息至财务报表中
                    operateFinancialStatementService.write("financialCost", operateFinancialStatementService.read("financialCost", runningState) + interest, runningState);
                    balance -= debtState.getAmounts();  //归还到期短贷本金
                    logger.info("测试归还本金后的余额：{}", balance);
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
                if(lineState.getOwningState() > 0){
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
                    if (lineState.getProduceState() == -1){
                        lineState.setProduceState(0);   //转产完成
                    }
                }

                //更新生产线建设
                if(lineState.getOwningState() == 0){
                    lineState.setOwningState(1);    //安装完成
                }

            }
        }
        runningState.getFinanceState().setCashAmount(balance);
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

        runningState.getFinanceState().setCashAmount(balance);
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
            if(factoryState.getOwningState() == 0){
                operateFinancialStatementService.write("factoryRent", value, runningState);
            }
            runningState.getFinanceState().setCashAmount(balance);
            factoryState.setValue(value);
            factoryState.setContent(0);
            factoryState.setFinalPaymentYear(runningState.getBaseState().getTimeYear());
            factoryState.setFinanPaymentQuarter(runningState.getBaseState().getTimeQuarter());
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

    @Transactional
    public RunningState changeLine(String username, Integer changeType, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        RuleLine ruleLine = rule.getRuleLine();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
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
                    if(lineState.getOwningState() > 0 && lineState.getProduceState() == 0){
                        Integer lineId = lineState.getId();
                        Iterator<Integer> iterator = list.iterator();
                        while (iterator.hasNext()){
                            Integer tempid = iterator.next();
                            Integer changeTime = 0;
                            if (lineId == tempid){
                                if(lineState.getType() == 1){
                                    balance -= ruleLine.getLine1ChangeInvest();
                                    changeTime = ruleLine.getLine1ChangeTime();
                                }
                                if(lineState.getType() == 2){
                                    balance -= ruleLine.getLine2ChangeInvest();
                                    changeTime = ruleLine.getLine2ChangeTime();
                                }
                                if(lineState.getType() == 3){
                                    balance -= ruleLine.getLine3ChangeInvest();
                                    changeTime = ruleLine.getLine3ChangeTime();
                                }
                                if(lineState.getType() == 4){
                                    balance -= ruleLine.getLine4ChangeInvest();
                                    changeTime = ruleLine.getLine4ChangeTime();
                                }
                                if(lineState.getType() == 5){
                                    balance -= ruleLine.getLine5ChangeInvest();
                                    changeTime = ruleLine.getLine5ChangeTime();
                                }
                                if(balance < 0){
                                    runningState.getBaseState().setMsg("现金不足");
                                    logger.info("用户：{} 执行转产失败", username);
                                    return runningState;
                                }else {
                                    lineState.setProduceState(-changeTime);
                                    lineState.setProductType(changeType);
                                }

                            }
                        }
                    }
                }
            }
        }
        if(balance >= 0){
            runningState.getFinanceState().setCashAmount(balance);
            runningState.getBaseState().setMsg("");     //清空MSG
        }
        return runningState;
    }

    @Transactional
    public RunningState continueChange(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        RuleLine ruleLine = rule.getRuleLine();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
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
                    if(lineState.getOwningState() > 0 && lineState.getProduceState() == 0){
                        Integer lineId = lineState.getId();
                        Iterator<Integer> iterator = list.iterator();
                        while (iterator.hasNext()){
                            Integer tempid = iterator.next();
                            if (lineId == tempid){
                                if(lineState.getType() == 1){
                                    balance -= ruleLine.getLine1ChangeInvest();
                                }
                                if(lineState.getType() == 2){
                                    balance -= ruleLine.getLine2ChangeInvest();
                                }
                                if(lineState.getType() == 3){
                                    balance -= ruleLine.getLine3ChangeInvest();
                                }
                                if(lineState.getType() == 4){
                                    balance -= ruleLine.getLine4ChangeInvest();
                                }
                                if(lineState.getType() == 5){
                                    balance -= ruleLine.getLine5ChangeInvest();
                                }
                                if(balance < 0){
                                    runningState.getBaseState().setMsg("现金不足");
                                    logger.info("用户：{} 执行继续转产失败", username);
                                    return runningState;
                                }else {
                                    lineState.setProduceState(lineState.getProduceState() + 1);
                                }

                            }
                        }
                    }
                }
            }
        }
        if(balance >= 0){
            runningState.getFinanceState().setCashAmount(balance);
            runningState.getBaseState().setMsg("");     //清空MSG
            runningState.getBaseState().getOperateState().setContinueChange(1);     //时间轴：关闭继续转产
            logger.info("用户：{} 执行继续转产", username);
        }
        return runningState;
    }

    @Transactional
    public RunningState saleLine(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        RuleLine ruleLine = rule.getRuleLine();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Iterator<FactoryState> factoryStateIterator = factoryStateList.iterator();
        List<FinancialStatement> financialStatementList = runningState.getFinanceState().getFinancialStatementList();
        Integer timeYear = runningState.getBaseState().getTimeYear();
        while (factoryStateIterator.hasNext()){
            FactoryState factoryState = factoryStateIterator.next();
            if(factoryState.getContent() != 0){
                List<LineState> lineStateList = factoryState.getLineStateList();
                Iterator<LineState> lineStateIterator = lineStateList.iterator();
                while (lineStateIterator.hasNext()){
                    LineState lineState = lineStateIterator.next();
                    if(lineState.getOwningState() > 0 && lineState.getProduceState() == 0){
                        Integer lineId = lineState.getId();
                        Iterator<Integer> iterator = list.iterator();
                        while (iterator.hasNext()){
                            Integer tempid = iterator.next();
                            if (lineId == tempid){
                                if(lineState.getType() == 1){
                                    balance += ruleLine.getLine1ScrapValue();
                                    operateFinancialStatementService.write("lostCost", operateFinancialStatementService.read("lostCost", runningState) + (lineState.getValue() - ruleLine.getLine1ScrapValue()), runningState);
//                                    for(FinancialStatement financialStatement : financialStatementList){
//                                        if(financialStatement.getYear() == timeYear){
//                                            financialStatement.setLostCost(financialStatement.getLostCost() + (lineState.getValue() - ruleLine.getLine1ScrapValue()));   //记录净值与残值的差额至损失
//                                        }
//                                    }
                                }
                                if(lineState.getType() == 2){
                                    balance += ruleLine.getLine2ScrapValue();
                                    operateFinancialStatementService.write("lostCost", operateFinancialStatementService.read("lostCost", runningState) + (lineState.getValue() - ruleLine.getLine2ScrapValue()), runningState);
//                                    for(FinancialStatement financialStatement : financialStatementList){
//                                        if(financialStatement.getYear() == timeYear){
//                                            financialStatement.setLostCost(financialStatement.getLostCost() + (lineState.getValue() - ruleLine.getLine2ScrapValue()));   //记录净值与残值的差额至损失
//                                        }
//                                    }
                                }
                                if(lineState.getType() == 3){
                                    balance += ruleLine.getLine3ScrapValue();
                                    operateFinancialStatementService.write("lostCost", operateFinancialStatementService.read("lostCost", runningState) + (lineState.getValue() - ruleLine.getLine3ScrapValue()), runningState);
//                                    for(FinancialStatement financialStatement : financialStatementList){
//                                        if(financialStatement.getYear() == timeYear){
//                                            financialStatement.setLostCost(financialStatement.getLostCost() + (lineState.getValue() - ruleLine.getLine3ScrapValue()));   //记录净值与残值的差额至损失
//                                        }
//                                    }
                                }
                                if(lineState.getType() == 4){
                                    balance += ruleLine.getLine4ScrapValue();
                                    operateFinancialStatementService.write("lostCost", operateFinancialStatementService.read("lostCost", runningState) + (lineState.getValue() - ruleLine.getLine4ScrapValue()), runningState);
//                                    for(FinancialStatement financialStatement : financialStatementList){
//                                        if(financialStatement.getYear() == timeYear){
//                                            financialStatement.setLostCost(financialStatement.getLostCost() + (lineState.getValue() - ruleLine.getLine4ScrapValue()));   //记录净值与残值的差额至损失
//                                        }
//                                    }
                                }
                                if(lineState.getType() == 5){
                                    balance += ruleLine.getLine5ScrapValue();
                                    operateFinancialStatementService.write("lostCost", operateFinancialStatementService.read("lostCost", runningState) + (lineState.getValue() - ruleLine.getLine5ScrapValue()), runningState);
//                                    for(FinancialStatement financialStatement : financialStatementList){
//                                        if(financialStatement.getYear() == timeYear){
//                                            financialStatement.setLostCost(financialStatement.getLostCost() + (lineState.getValue() - ruleLine.getLine5ScrapValue()));   //记录净值与残值的差额至损失
//                                        }
//                                    }
                                }
                                if(balance < 0){
                                    runningState.getBaseState().setMsg("现金不足");
                                    logger.info("用户：{} 执行出售生产线失败", username);
                                    return runningState;
                                }else {
                                    iterator.remove();
                                    lineStateIterator.remove();
                                    factoryState.setContent(factoryState.getContent() - 1);
                                }

                            }
                        }
                    }
                }
            }
        }
        if(balance >= 0){
            runningState.getFinanceState().setCashAmount(balance);
            runningState.getBaseState().setMsg("");     //清空MSG
            runningState.getBaseState().getOperateState().setSaleLine(1);   //时间轴：关闭出售生产线
            logger.info("用户：{} 执行出售生产线", username);
        }
        return runningState;
    }

    @Transactional
    public RunningState beginProduction(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        RuleLine ruleLine = rule.getRuleLine();
        RuleProduct ruleProduct = rule.getRuleProduct();
        List<ProductDevState> productDevStateList = runningState.getDevState().getProductDevStateList();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
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
                    if(lineState.getOwningState() > 0 && lineState.getProduceState() == 0){
                        Integer lineId = lineState.getId();
                        Iterator<Integer> iterator = list.iterator();
                        while (iterator.hasNext()){
                            Integer tempid = iterator.next();
                            Integer produceTime = 0;
                            Integer devState = 0;
                            if (lineId == tempid){
                                if(lineState.getType() == 1){
                                    produceTime = ruleLine.getLine1ProduceTime();
                                }
                                if(lineState.getType() == 2){
                                    produceTime = ruleLine.getLine2ProduceTime();
                                }
                                if(lineState.getType() == 3){
                                    produceTime = ruleLine.getLine3ProduceTime();
                                }
                                if(lineState.getType() == 4){
                                    produceTime = ruleLine.getLine4ProduceTime();
                                }
                                if(lineState.getType() == 5){
                                    produceTime = ruleLine.getLine5ProduceTime();
                                }
                                if(lineState.getProductType() == 1){
                                    balance -= ruleProduct.getProduct1ProcCost();
//                                    for(ProductDevState productDevState : productDevStateList){
//                                        if (productDevState.getType() == 1){
//                                            devState = productDevState.getState();
//                                        }
//                                    }
                                }
                                if(lineState.getProductType() == 2){
                                    balance -= ruleProduct.getProduct2ProcCost();
//                                    for(ProductDevState productDevState : productDevStateList){
//                                        if (productDevState.getType() == 2){
//                                            devState = productDevState.getState();
//                                        }
//                                    }
                                }
                                if(lineState.getProductType() == 3){
                                    balance -= ruleProduct.getProduct3ProcCost();
//                                    for(ProductDevState productDevState : productDevStateList){
//                                        if (productDevState.getType() == 3){
//                                            devState = productDevState.getState();
//                                        }
//                                    }
                                }
                                if(lineState.getProductType() == 4){
                                    balance -= ruleProduct.getProduct4ProcCost();
//                                    for(ProductDevState productDevState : productDevStateList){
//                                        if (productDevState.getType() == 4){
//                                            devState = productDevState.getState();
//                                        }
//                                    }
                                }
                                if(lineState.getProductType() == 5){
                                    balance -= ruleProduct.getProduct5ProcCost();
//                                    for(ProductDevState productDevState : productDevStateList){
//                                        if (productDevState.getType() == 5){
//                                            devState = productDevState.getState();
//                                        }
//                                    }
                                }
                                if(balance < 0){
                                    runningState.getBaseState().setMsg("现金不足");
                                    logger.info("用户：{} 执行开始生产失败", username);
                                }else {
                                    lineState.setProduceState(produceTime);
                                    iterator.remove();  //清除List中该线ID
                                    logger.info("用户：{} 执行开始生产,生产线ID：{}，产品：{}", username, lineId, lineState.getProductType());
                                }
                            }
                        }
                    }
                }
            }
        }

        if(balance >= 0){
            runningState.getFinanceState().setCashAmount(balance);
            runningState.getBaseState().setMsg("");     //清空MSG
            runningState.getBaseState().getOperateState().setBeginProduction(1);    //时间轴：关闭 开始生产
        }
        return runningState;
    }

    @Transactional
    public RunningState updateReceivable(String username){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        List<ReceivableState> receivableStateList = runningState.getFinanceState().getReceivableStateList();
        Iterator<ReceivableState> receivableStateIterator = receivableStateList.iterator();
        Integer balance = runningState.getFinanceState().getCashAmount();
        Integer amountsTotal = 0;
        while (receivableStateIterator.hasNext()){
            ReceivableState receivableState = receivableStateIterator.next();
            Integer accountPeriod = receivableState.getAccountPeriod();
            if (accountPeriod == 1){
                amountsTotal += receivableState.getAmounts();
//                receivableStateIterator.remove();
                receivableState.setAmounts(0);
                receivableState.setAccountPeriod(4);
            }else {
                receivableState.setAccountPeriod(accountPeriod - 1);
//                ReceivableState newReceivableState = new ReceivableState();
//                newReceivableState.setAccountPeriod(4);
//                newReceivableState.setAmounts(0);
//                receivableStateList.add(newReceivableState);
            }
        }

        runningState.getFinanceState().setCashAmount(balance + amountsTotal);

        //时间轴变换
        runningState.getBaseState().setState(13);   //跳转至季末
        runningState.getBaseState().getOperateState().setProductDev(0);     //允许 产品研发
        runningState.getBaseState().getOperateState().setMarketDev(0);     //允许 市场研发
        runningState.getBaseState().getOperateState().setQualificationDev(0);     //允许 资质研发
        runningState.getBaseState().getOperateState().setAddPurchase(0);    //解除控制 采购原料
        runningState.getBaseState().getOperateState().setBuildLine(0);      //解除控制 在建生产线
        runningState.getBaseState().getOperateState().setContinueChange(0); //解除控制 继续转产
        runningState.getBaseState().getOperateState().setSaleLine(0);       //解除控制 出售生产线
        runningState.getBaseState().getOperateState().setBeginProduction(0);    ////解除控制 开始生产

//        return runningState;
        return testAddOrder(runningState);  //每次更新应收款都用这个函数增加订单以供测试
    }

    @Transactional
    public RunningState delivery(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();

        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        List<OrderState> orderStateList = runningState.getMarketingState().getOrderStateList();
        Iterator<OrderState> orderStateIterator = orderStateList.iterator();
        List<ProductState> productStateList = runningState.getStockState().getProductStateList();
        List<ReceivableState> receivableStateList = runningState.getFinanceState().getReceivableStateList();
        while (orderStateIterator.hasNext()){
            OrderState orderState = orderStateIterator.next();
            Iterator<Integer> iterator = list.iterator();
            Integer id = orderState.getId();
            while (iterator.hasNext()){
                Integer tempId = iterator.next();
                if(id == tempId){
                    for(int i = 0; i < 5; i++){
                        if(orderState.getTypeId() == i){
                            Iterator<ProductState> productStateIterator = productStateList.iterator();
                            while (productStateIterator.hasNext()){
                                ProductState productState = productStateIterator.next();
                                if(productState.getType() == i){
                                    if(orderState.getQuantity() > productState.getQuantity()){
                                        runningState.getBaseState().setMsg("产品数量不足");
                                        return runningState;
                                    }else {
                                        productState.setQuantity(productState.getQuantity() - orderState.getQuantity());
                                        if(orderState.getAccountPeriod() == 0){
                                            balance += orderState.getTotalPrice();
                                        }else {
                                            Iterator<ReceivableState> receivableStateIterator = receivableStateList.iterator();
                                            while (receivableStateIterator.hasNext()){
                                                ReceivableState receivableState = receivableStateIterator.next();
                                                if(receivableState.getAccountPeriod() == orderState.getAccountPeriod()){
                                                    receivableState.setAmounts(receivableState.getAmounts() + orderState.getTotalPrice());
                                                }
                                            }
                                        }
                                        orderState.setExecution(1);
                                        orderState.setFinishTime(runningState.getBaseState().getTimeQuarter());

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        runningState.getFinanceState().setCashAmount(balance);
        logger.info("交单请求执行完毕");
        return runningState;
    }

    @Transactional
    public RunningState productDev(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        RuleProduct ruleProduct = rule.getRuleProduct();
        Integer balance = runningState.getFinanceState().getCashAmount();
        List<ProductDevState> productDevStateList = runningState.getDevState().getProductDevStateList();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()){
            Integer devType = iterator.next();
            Integer check = 0;  //检查标记
            Iterator<ProductDevState> productDevStateIterator = productDevStateList.iterator();
            while (productDevStateIterator.hasNext()){
                ProductDevState productDevState = productDevStateIterator.next();
                Integer tempType = productDevState.getType();
                Integer devInvest = 0;
                if(tempType == 1){
                    devInvest = ruleProduct.getProduct1DevInvest();
                }
                if(tempType == 2){
                    devInvest = ruleProduct.getProduct2DevInvest();
                }
                if(tempType == 3){
                    devInvest = ruleProduct.getProduct3DevInvest();
                }
                if(tempType == 4){
                    devInvest = ruleProduct.getProduct4DevInvest();
                }
                if(tempType == 5){
                    devInvest = ruleProduct.getProduct5DevInvest();
                }
                if (devType == tempType){
                    check = 1;
                    if(devInvest > balance){
                        runningState.getBaseState().setMsg("现金不足");
                        return runningState;
                    }else {
                        balance -= devInvest;
                        productDevState.setState(productDevState.getState() + 1);
                    }

                }
            }
            if(check == 0){
                //如果检查标记仍然是0，表示在现有DevList中还没有该类型的DevState
                Integer devInvest = 0;
                Integer devTime = 0;
                if(devType == 1){
                    devInvest = ruleProduct.getProduct1DevInvest();
                    devTime = ruleProduct.getProduct1DevTime();
                }
                if(devType == 2){
                    devInvest = ruleProduct.getProduct2DevInvest();
                    devTime = ruleProduct.getProduct2DevTime();
                }
                if(devType == 3){
                    devInvest = ruleProduct.getProduct3DevInvest();
                    devTime = ruleProduct.getProduct3DevTime();
                }
                if(devType == 4){
                    devInvest = ruleProduct.getProduct4DevInvest();
                    devTime = ruleProduct.getProduct4DevTime();
                }
                if(devType == 5){
                    devInvest = ruleProduct.getProduct5DevInvest();
                    devTime = ruleProduct.getProduct5DevTime();
                }
                if(devInvest > balance){
                    runningState.getBaseState().setMsg("现金不足");
                    return runningState;
                }else {
                    balance -= devInvest;
//                    Integer rec = operateFinancialStatementService.read("productDevCost", runningState) + devInvest;
                    runningState = operateFinancialStatementService.write("productDevCost", operateFinancialStatementService.read("productDevCost", runningState) + devInvest, runningState);
                    ProductDevState productDevState = new ProductDevState();
                    productDevState.setState(2-devTime);
                    productDevState.setType(devType);
                    productDevStateList.add(productDevState);
                }

            }
        }

        runningState.getFinanceState().setCashAmount(balance);
        runningState.getBaseState().setMsg(""); //清空MSG
        runningState.getBaseState().getOperateState().setProductDev(1);     //时间轴： 关闭产品研发
        logger.info("用户：{} 成功执行产品研发", username);
        return runningState;
    }

    @Transactional
    public RunningState saleFactory(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        RuleFactory ruleFactory = rule.getRuleFactory();
        Integer balance = runningState.getFinanceState().getCashAmount();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Iterator<FactoryState> factoryStateIterator = factoryStateList.iterator();

        while (factoryStateIterator.hasNext()){
            FactoryState factoryState = factoryStateIterator.next();

            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()){
                Integer tempId = iterator.next();
                if (factoryState.getId() == tempId){
                    Integer factorySalePrice = 0;
                    Integer factoryRentPrice = 0;
                    if (factoryState.getType() == 1){
                        factoryRentPrice = ruleFactory.getFactory1RentPrice();
                        factorySalePrice = ruleFactory.getFactory1SalePrice();
                    }
                    if (factoryState.getType() == 2){
                        factoryRentPrice = ruleFactory.getFactory2RentPrice();
                        factorySalePrice = ruleFactory.getFactory2SalePrice();
                    }
                    if (factoryState.getType() == 3){
                        factoryRentPrice = ruleFactory.getFactory3RentPrice();
                        factorySalePrice = ruleFactory.getFactory3SalePrice();
                    }
                    if (factoryState.getType() == 4){
                        factoryRentPrice = ruleFactory.getFactory4RentPrice();
                        factorySalePrice = ruleFactory.getFactory4SalePrice();
                    }
                    if (factoryState.getType() == 5){
                        factoryRentPrice = ruleFactory.getFactory5RentPrice();
                        factorySalePrice = ruleFactory.getFactory5SalePrice();
                    }

                    if(factoryState.getContent() == 0){
                        //厂房为空，直接出售
                        List<ReceivableState> receivableStateList = runningState.getFinanceState().getReceivableStateList();
                        Iterator<ReceivableState> receivableStateIterator = receivableStateList.iterator();
                        while (receivableStateIterator.hasNext()){
                            ReceivableState receivableState = receivableStateIterator.next();
                            if(receivableState.getAccountPeriod() == 4){
                                receivableState.setAmounts(receivableState.getAmounts() + factorySalePrice);    //售房款增加至应收款4期中
                            }
                        }
                        factoryStateIterator.remove();  //从数据库中删除该厂房信息
                    }else {
                        //厂房非空，转租赁
                        if(factoryRentPrice > balance){
                            runningState.getBaseState().setMsg("现金不足");
                            return runningState;
                        }else {
                            balance -= factoryRentPrice;
                            factoryState.setOwningState(0); //转租赁
                            factoryState.setFinalPaymentYear(runningState.getBaseState().getTimeYear());    //设置支付租金时间
                            factoryState.setFinanPaymentQuarter(runningState.getBaseState().getTimeQuarter());//设置支付租金时间
                            List<ReceivableState> receivableStateList = runningState.getFinanceState().getReceivableStateList();
                            Iterator<ReceivableState> receivableStateIterator = receivableStateList.iterator();
                            while (receivableStateIterator.hasNext()){
                                ReceivableState receivableState = receivableStateIterator.next();
                                if(receivableState.getAccountPeriod() == 4){
                                    receivableState.setAmounts(receivableState.getAmounts() + factorySalePrice);    //售房款增加至应收款4期中
                                }
                            }
                        }
                    }


                }
            }
        }
        runningState.getBaseState().setMsg("");     //清空MSG
        runningState.getFinanceState().setCashAmount(balance);
        return runningState;
    }

    @Transactional
    public RunningState exitRent(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Iterator<FactoryState> factoryStateIterator = factoryStateList.iterator();
        while (factoryStateIterator.hasNext()){
            FactoryState factoryState = factoryStateIterator.next();
            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()){
                Integer tempId = iterator.next();
                if (factoryState.getId() == tempId){
                    if(factoryState.getContent() == 0){
                        factoryStateIterator.remove();  //从数据库中删除该厂房信息
                    }
                }
            }
        }
        runningState.getBaseState().setMsg("");     //清空MSG
        return runningState;
    }

    @Transactional
    public RunningState rentToBuy(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        RuleFactory ruleFactory = rule.getRuleFactory();
        Integer balance = runningState.getFinanceState().getCashAmount();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Iterator<FactoryState> factoryStateIterator = factoryStateList.iterator();

        while (factoryStateIterator.hasNext()){
            FactoryState factoryState = factoryStateIterator.next();

            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()){
                Integer tempId = iterator.next();
                if (factoryState.getId() == tempId){
                    Integer factoryBuyPrice = 0;
                    if (factoryState.getType() == 1){
                        factoryBuyPrice = ruleFactory.getFactory1BuyPrice();
                    }
                    if (factoryState.getType() == 2){
                        factoryBuyPrice = ruleFactory.getFactory2BuyPrice();
                    }
                    if (factoryState.getType() == 3){
                        factoryBuyPrice = ruleFactory.getFactory3BuyPrice();
                    }
                    if (factoryState.getType() == 4){
                        factoryBuyPrice = ruleFactory.getFactory4BuyPrice();
                    }
                    if (factoryState.getType() == 5){
                        factoryBuyPrice = ruleFactory.getFactory5BuyPrice();
                    }
                    if(factoryBuyPrice > balance){
                        runningState.getBaseState().setMsg("现金不足");
                        return runningState;
                    }else {
                        balance -= factoryBuyPrice;
                        factoryState.setOwningState(1); //转购买
                        factoryState.setFinalPaymentYear(runningState.getBaseState().getTimeYear());    //设置支付租金时间
                        factoryState.setFinanPaymentQuarter(runningState.getBaseState().getTimeQuarter());//设置支付租金时间
                    }
                }
            }
        }
        runningState.getBaseState().setMsg("");     //清空MSG
        runningState.getFinanceState().setCashAmount(balance);
        return runningState;
    }

    @Transactional
    public RunningState endQuarter(String username){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        RuleFactory ruleFactory = rule.getRuleFactory();
        Integer balance = runningState.getFinanceState().getCashAmount();

        //时间轴变换
        runningState.getBaseState().setTimeQuarter(runningState.getBaseState().getTimeQuarter() + 1);   //跳转至下个季度
        runningState.getBaseState().setState(10);   //跳转至季初
        runningState.getBaseState().getOperateState().setProductDev(0);  //解除控制 产品研发

        /**
         * 自动进行的业务操作：
         * 1.支付行政管理费
         * 2.厂房续租
         * 3.更新建设
         * 4.违约记录
         */
        if(rule.getRuleParam().getParamManagementCost() > balance){
            runningState.getBaseState().setMsg("现金不足");
        }else {
            balance -= rule.getRuleParam().getParamManagementCost();//支付行政管理费
            operateFinancialStatementService.write("managementCost", rule.getRuleParam().getParamManagementCost(), runningState);
        }

        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        for (FactoryState factoryState : factoryStateList){
            if((factoryState.getFinalPaymentYear() != runningState.getBaseState().getTimeYear()) && (factoryState.getFinanPaymentQuarter() == runningState.getBaseState().getTimeQuarter())){
                Integer rentPrice = 0;
                if(factoryState.getType() == 1){
                    rentPrice = ruleFactory.getFactory1RentPrice();
                }
                if(factoryState.getType() == 2){
                    rentPrice = ruleFactory.getFactory2RentPrice();
                }
                if(factoryState.getType() == 3){
                    rentPrice = ruleFactory.getFactory3RentPrice();
                }
                if(factoryState.getType() == 4){
                    rentPrice = ruleFactory.getFactory4RentPrice();
                }
                if(factoryState.getType() == 5){
                    rentPrice = ruleFactory.getFactory5RentPrice();
                }
                if(rentPrice > balance){
                    runningState.getBaseState().setMsg("现金不足");
                    return runningState;
                }else {
                    balance -= rentPrice;   //支付续租费用
                    factoryState.setFinalPaymentYear(runningState.getBaseState().getTimeYear());
                    operateFinancialStatementService.write("factoryRent", rentPrice, runningState);
                }
            }

        }
        runningState.getFinanceState().setCashAmount(balance);
        runningState.getBaseState().setMsg(""); //清空MSG
        return runningState;
    }

    @Transactional
    public RunningState marketDev(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        RuleMarket ruleMarket = rule.getRuleMarket();
        Integer balance = runningState.getFinanceState().getCashAmount();
        List<MarketDevState> marketDevStateList = runningState.getDevState().getMarketDevStateList();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()){
            Integer devType = iterator.next();
            Integer check = 0;  //检查标记
            Iterator<MarketDevState> marketDevStateIterator = marketDevStateList.iterator();
            while (marketDevStateIterator.hasNext()){
                MarketDevState marketDevState = marketDevStateIterator.next();
                Integer tempType = marketDevState.getType();
                Integer devInvest = 0;
                if(tempType == 1){
                    devInvest = ruleMarket.getMarket1UnitInvest();
                }
                if(tempType == 2){
                    devInvest = ruleMarket.getMarket2UnitInvest();
                }
                if(tempType == 3){
                    devInvest = ruleMarket.getMarket3UnitInvest();
                }
                if(tempType == 4){
                    devInvest = ruleMarket.getMarket4UnitInvest();
                }
                if(tempType == 5){
                    devInvest = ruleMarket.getMarket5UnitInvest();
                }
                if (devType == tempType){
                    check = 1;
                    if(devInvest > balance){
                        runningState.getBaseState().setMsg("现金不足 ");
                        return runningState;
                    }else {
                        balance -= devInvest;
                        marketDevState.setState(marketDevState.getState() + 1);
                    }

                }
            }
            if(check == 0){
                //如果检查标记仍然是0，表示在现有DevList中还没有该类型的DevState
                Integer devInvest = 0;
                Integer devTime = 0;
                if(devType == 1){
                    devInvest = ruleMarket.getMarket1UnitInvest();
                    devTime = ruleMarket.getMarket1DevTime();
                }
                if(devType == 2){
                    devInvest = ruleMarket.getMarket2UnitInvest();
                    devTime = ruleMarket.getMarket3DevTime();
                }
                if(devType == 3){
                    devInvest = ruleMarket.getMarket2UnitInvest();
                    devTime = ruleMarket.getMarket3DevTime();
                }
                if(devType == 4){
                    devInvest = ruleMarket.getMarket4UnitInvest();
                    devTime = ruleMarket.getMarket4DevTime();
                }
                if(devType == 5){
                    devInvest = ruleMarket.getMarket5UnitInvest();
                    devTime = ruleMarket.getMarket5DevTime();
                }
                if(devInvest > balance){
                    runningState.getBaseState().setMsg("现金不足");
                    return runningState;
                }else {
                    balance -= devInvest;
                    runningState = operateFinancialStatementService.write("marketDevCost", operateFinancialStatementService.read("marketDevCost", runningState) + devInvest, runningState);
                    MarketDevState marketDevState = new MarketDevState();
                    marketDevState.setState(2-devTime);
                    marketDevState.setType(devType);
                    marketDevStateList.add(marketDevState);
                }

            }
        }

        runningState.getFinanceState().setCashAmount(balance);
        runningState.getBaseState().setMsg(""); //清空MSG
        runningState.getBaseState().getOperateState().setMarketDev(1);     //时间轴： 关闭市场开拓
        logger.info("用户：{} 成功执行市场开拓", username);
        return runningState;
    }

    @Transactional
    public RunningState qualificationDev(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        RuleIso ruleIso = rule.getRuleIso();
        Integer balance = runningState.getFinanceState().getCashAmount();
        List<QualificationDevState> qualificationDevStateList = runningState.getDevState().getQualificationDevStateList();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()){
            Integer devType = iterator.next();
            Integer check = 0;  //检查标记
            Iterator<QualificationDevState> qualificationDevStateIterator = qualificationDevStateList.iterator();
            while (qualificationDevStateIterator.hasNext()){
                QualificationDevState qualificationDevState = qualificationDevStateIterator.next();
                Integer tempType = qualificationDevState.getType();
                Integer devInvest = 0;
                if(tempType == 1){
                    devInvest = ruleIso.getIso1UnitInvest();
                }
                if(tempType == 2){
                    devInvest = ruleIso.getIso2UnitInvest();
                }
                if (devType == tempType){
                    check = 1;
                    if(devInvest > balance){
                        runningState.getBaseState().setMsg("现金不足  ");
                        return runningState;
                    }else {
                        balance -= devInvest;
                        qualificationDevState.setState(qualificationDevState.getState() + 1);
                    }

                }
            }
            if(check == 0){
                //如果检查标记仍然是0，表示在现有DevList中还没有该类型的DevState
                Integer devInvest = 0;
                Integer devTime = 0;
                if(devType == 1){
                    devInvest = ruleIso.getIso1UnitInvest();
                    devTime = ruleIso.getIso1DevTime();
                }
                if(devType == 2){
                    devInvest = ruleIso.getIso2UnitInvest();
                    devTime = ruleIso.getIso2DevTime();
                }
                if(devInvest > balance){
                    runningState.getBaseState().setMsg("现金不足 ");
                    return runningState;
                }else {
                    balance -= devInvest;
                    runningState = operateFinancialStatementService.write("isoDevCost", operateFinancialStatementService.read("isoDevCost", runningState) + devInvest, runningState);
                    QualificationDevState qualificationDevState = new QualificationDevState();
                    qualificationDevState.setState(2-devTime);
                    qualificationDevState.setType(devType);
                    qualificationDevStateList.add(qualificationDevState);
                }
            }
        }

        runningState.getFinanceState().setCashAmount(balance);
        runningState.getBaseState().setMsg(""); //清空MSG
        runningState.getBaseState().getOperateState().setQualificationDev(1);     //时间轴： 关闭资质认证
        logger.info("用户：{} 成功执行资质认证", username);
        return runningState;
    }

    @Transactional
    public RunningState endYear(String username){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        RuleFactory ruleFactory = rule.getRuleFactory();
        Integer balance = runningState.getFinanceState().getCashAmount();

        //时间轴变换
        runningState.getBaseState().setTimeYear(runningState.getBaseState().getTimeYear() + 1);     //跳转至下一年
        runningState.getBaseState().setTimeQuarter(0);   //跳转至年初
        runningState.getBaseState().setState(1);   //跳转至季初
        runningState.getBaseState().getOperateState().setProductDev(0);  //解除控制 产品研发
        runningState.getBaseState().getOperateState().setMarketDev(0);  //解除控制 市场开拓
        runningState.getBaseState().getOperateState().setQualificationDev(0);   //解除控制 资质认证
        runningState.getBaseState().getOperateState().setReport(0); //允许 填写报表

        /**
         * 自动进行的业务操作：
         * 1.支付行政管理费
         * 2.厂房续租
         * 3.支付设备维修费
         * 4.计提折旧
         * 5.违约扣款
         */
        if(rule.getRuleParam().getParamManagementCost() > balance){
            runningState.getBaseState().setMsg("现金不足");
        }else {
            balance -= rule.getRuleParam().getParamManagementCost();//支付行政管理费
            operateFinancialStatementService.write("managementCost", rule.getRuleParam().getParamManagementCost(), runningState);
        }

        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        for (FactoryState factoryState : factoryStateList){
            if((factoryState.getFinalPaymentYear() != runningState.getBaseState().getTimeYear()) && (factoryState.getFinanPaymentQuarter() == runningState.getBaseState().getTimeQuarter())){
                Integer rentPrice = 0;
                if(factoryState.getType() == 1){
                    rentPrice = ruleFactory.getFactory1RentPrice();
                }
                if(factoryState.getType() == 2){
                    rentPrice = ruleFactory.getFactory2RentPrice();
                }
                if(factoryState.getType() == 3){
                    rentPrice = ruleFactory.getFactory3RentPrice();
                }
                if(factoryState.getType() == 4){
                    rentPrice = ruleFactory.getFactory4RentPrice();
                }
                if(factoryState.getType() == 5){
                    rentPrice = ruleFactory.getFactory5RentPrice();
                }
                if(rentPrice > balance){
                    runningState.getBaseState().setMsg("现金不足");
                    return runningState;
                }else {
                    balance -= rentPrice;   //支付续租费用
                    factoryState.setFinalPaymentYear(runningState.getBaseState().getTimeYear());
                    operateFinancialStatementService.write("factoryRent", rentPrice, runningState);
                }
            }

        }
        runningState.getFinanceState().setCashAmount(balance);
        runningState.getBaseState().setMsg(""); //清空MSG
        return runningState;
    }

    @Transactional
    public RunningState testAddOrder(RunningState runningState){

        List<OrderState> orderStateList = runningState.getMarketingState().getOrderStateList();
        OrderState orderState1 = new OrderState();
        orderState1.setAccountPeriod(1);
        orderState1.setArea(1);
        orderState1.setDeliveryTime(3);
        orderState1.setExecution(0);
        orderState1.setOrderId(1);
        orderState1.setOwner("XY1");
        orderState1.setQualificate(0);
        orderState1.setTotalPrice(50);
        orderState1.setTypeId(1);
        orderState1.setQuantity(1);
        orderState1.setUnitPrice(50);
        orderState1.setYear(1);
        orderStateList.add(orderState1);

        return runningState;
    }
}
