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
import bobo.erp.domain.state.stock.MaterialState;
import bobo.erp.domain.state.stock.ProductState;
import bobo.erp.domain.state.stock.PurchaseState;
import bobo.erp.repository.state.RunningStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.Line;
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
        Integer tempTotalAmount = 0;
        Integer timeYear = runningState.getBaseState().getTimeYear();

        //支出金额计算
        if(timeYear > 1){
            Integer tax = operateFinancialStatementService.readWithTime((timeYear - 1), "incomeTax", runningState);
            tempTotalAmount += tax;
            List<DebtState> debtStateList = runningState.getFinanceState().getDebtStateList();
            Iterator<DebtState> debtStateIterator = debtStateList.iterator();
            Integer interest = 0;
            while (debtStateIterator.hasNext()){
                DebtState debtState = debtStateIterator.next();
                if (debtState.getDebtType() == 2){
                    interest += (int)Math.round(debtState.getAmounts() * rule.getRuleParam().getParamLongTermLoanRates());
                    tempTotalAmount += interest;
                    if(debtState.getRepaymentPeriod() == 1){
                        tempTotalAmount += debtState.getAmounts();
                    }
                }
            }
        }
        tempTotalAmount += advertisingState.getAd0();

        //业务处理部分
        if(tempTotalAmount > balance){
            runningState.getBaseState().setMsg("现金不足");
            return runningState;
        }else {
            balance -= tempTotalAmount; //统一扣费

            runningState.getFinanceState().setCashAmount(balance);
            advertisingState.setYear(runningState.getBaseState().getTimeYear());
            runningState.getMarketingState().getAdvertisingStateList().add(advertisingState);   //保存数据至广告记录

            if(timeYear > 1){
                FinancialStatement financialStatement = new FinancialStatement();   //投广告时新建一个财务报表
                financialStatement.setAdvertisingCost(advertisingState.getAd0());
                financialStatement.setYear(timeYear);
                runningState.getFinanceState().getFinancialStatementList().add(financialStatement); //保存数据至财务报表
                operateFinancialStatementService.write("equityCapital", operateFinancialStatementService.readWithTime(timeYear - 1, "equityCapital", runningState), runningState);     //权益 - 股东资本
            }else if(timeYear == 1){
                operateFinancialStatementService.write("advertisingCost", advertisingState.getAd0(), runningState);
                operateFinancialStatementService.write("equityCapital", rule.getRuleParam().getParamInitialCash(), runningState);     //权益 - 股东资本
            }

            if(timeYear > 1){
                List<DebtState> debtStateList = runningState.getFinanceState().getDebtStateList();
                Iterator<DebtState> debtStateIterator = debtStateList.iterator();
                Integer interest = 0;
                while (debtStateIterator.hasNext()){
                    DebtState debtState = debtStateIterator.next();
                    if (debtState.getDebtType() == 2){
                        interest += (int)Math.round(debtState.getAmounts() * rule.getRuleParam().getParamLongTermLoanRates());
                        if(debtState.getRepaymentPeriod() == 1){
                            debtStateIterator.remove();     //删除数据库中的相应记录
                        }else {
                            debtState.setRepaymentPeriod(debtState.getRepaymentPeriod()-1);
                        }
                    }
                }
                operateFinancialStatementService.write("financialCost", interest, runningState);
            }

            runningState.getBaseState().setMsg(""); //清空MSG
            runningState.getBaseState().getOperateState().setAd(1); //时间轴：关闭广告投放
            runningState.getBaseState().getOperateState().setOrderMeeting(0);   //时间轴：允许订货会
            runningState.getBaseState().getOperateState().setLongLoan(0);   //时间轴：允许长贷申请


        }
        return runningState;
    }

    @Transactional
    public RunningState applyDebt(String username, DebtState debtState){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        Integer debtLimit = 0;
        Integer timeYear = runningState.getBaseState().getTimeYear();
        if (timeYear == 1){
            debtLimit = (int)(rule.getRuleParam().getParamInitialCash() * rule.getRuleParam().getParamLoanRatio());
        }else {
            debtLimit = (int)(operateFinancialStatementService.readWithTime(timeYear - 1, "ownersEquity", runningState) * rule.getRuleParam().getParamLoanRatio());
        }
        if (debtState.getAmounts() > debtLimit){
            runningState.getBaseState().setMsg("贷款额度不足");
        }else {
            balance += debtState.getAmounts();
            runningState.getFinanceState().setCashAmount(balance);  //修改现金余额
            runningState.getFinanceState().getDebtStateList().add(debtState);   //存入贷款记录表
            if(debtState.getDebtType() == 1){
                runningState.getBaseState().getOperateState().setShortLoan(1);
            }
            logger.info("用户：{} 申请贷款：{}W 类型：{} 还款期：{}", username, debtState.getAmounts(), debtState.getDebtType(), debtState.getRepaymentPeriod());
            runningState.getBaseState().setMsg(""); //清空MSG
        }
        return runningState;
    }

    @Transactional
    public RunningState startYear(String username){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        runningState.getBaseState().setTimeQuarter(1); //跳转至第一季度
        runningState.getBaseState().setState(10);   //跳转至季度开始前
        runningState.getBaseState().getOperateState().setLongLoan(1);   //时间轴： 关闭长贷申请
        runningState.getBaseState().getOperateState().setOrderMeeting(1);   //时间轴： 关闭订货会
        runningState.getBaseState().getOperateState().setBidMeeting(1);   //时间轴： 关闭竞单会
        runningState.getBaseState().getOperateState().setAd(0);     //解除控制 广告投放
        return runningState;
    }

    @Transactional
    public RunningState startQuarter(String username){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        /**
         * 自动进行的业务操作：
         * 1.更新短贷
         * 2.更新生产
         * 3.更新建设
         * 4.时间轴变换
         */
        Integer balance = runningState.getFinanceState().getCashAmount();
        Integer interest = 0;
        Integer tempTotalAmount = 0;
        Integer timeYear = runningState.getBaseState().getTimeYear();
        List<DebtState> debtStateList = runningState.getFinanceState().getDebtStateList();
        List<FinancialStatement> financialStatementList = runningState.getFinanceState().getFinancialStatementList();
        Iterator<DebtState> debtStateIterator = debtStateList.iterator();
        //计算利息和到期本金总额
        for (DebtState debtState : debtStateList){
            if (debtState.getDebtType() == 1){
                if(debtState.getRepaymentPeriod() == 1){
                    interest += (int)Math.round(debtState.getAmounts() * rule.getRuleParam().getParamShortTermLoanRates());
                    tempTotalAmount += interest;
                    tempTotalAmount += debtState.getAmounts();
                }
            }
        }

        //检测是否可以继续执行当季开始
        if (tempTotalAmount > balance){
            runningState.getBaseState().setMsg("现金不足");
            logger.info("用户：{} 现金不足返回 操作：开始当季", username);
            return runningState;
        }else {
            balance -= tempTotalAmount;
            operateFinancialStatementService.write("financialCost", operateFinancialStatementService.read("financialCost", runningState) + interest, runningState); //记录利息至财务报表中
            while (debtStateIterator.hasNext()){
                DebtState debtState = debtStateIterator.next();
                if (debtState.getDebtType() == 1){
                    if(debtState.getRepaymentPeriod() == 1){
                        debtStateIterator.remove(); //从数据库中删除相应的记录
                    }else {
                        debtState.setRepaymentPeriod(debtState.getRepaymentPeriod()-1);
                    }
                }
            }
        }
        List<ProductState> productStateList = runningState.getStockState().getProductStateList();

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
                        Iterator<ProductState> productStateIterator = productStateList.iterator();
                        while (productStateIterator.hasNext()){
                            ProductState productState = productStateIterator.next();
                            if (productState.getType() == productType){
                                productState.setQuantity(productState.getQuantity() + 1);   //增加产品库存
                            }
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
                if (lineState.getOwningState() < 0){
                    lineState.setProduceState(0);   //允许续建
                }

            }
        }
        //时间轴变换
        runningState.getBaseState().setState(11);   //跳转至季初
        runningState.getBaseState().getOperateState().setShortLoan(0);  //允许申请短贷

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

        Integer tempTotalAmount = 0;
        //计算要支付的原料货款总额
        for (PurchaseState purchaseState : purchaseStateList){
            Integer tempType = purchaseState.getType();
            Integer tempQuantity = purchaseState.getQuantity();
            Integer tempDeliveryTime = purchaseState.getDeliveryTime();
            Integer purchaseTotalAmounts = 0;
            if(tempDeliveryTime == 1){
                if(tempType == 1){
                    Integer price = rule.getRuleMaterial().getMaterial1Price();
                    purchaseTotalAmounts = tempQuantity * price;
                }
                if(tempType == 2){
                    Integer price = rule.getRuleMaterial().getMaterial2Price();
                    purchaseTotalAmounts = tempQuantity * price;
                }
                if(tempType == 3){
                    Integer price = rule.getRuleMaterial().getMaterial3Price();
                    purchaseTotalAmounts = tempQuantity * price;
                }
                if(tempType == 4){
                    Integer price = rule.getRuleMaterial().getMaterial4Price();
                    purchaseTotalAmounts = tempQuantity * price;
                }
                if(tempType == 5){
                    Integer price = rule.getRuleMaterial().getMaterial5Price();
                    purchaseTotalAmounts = tempQuantity * price;
                }
                tempTotalAmount += purchaseTotalAmounts;
            }
        }

        //检测是否有足够现金
        if (tempTotalAmount > balance){
            runningState.getBaseState().setMsg("现金不足");
            return runningState;
        }
        //执行原材料入库及更新采购
        balance -= tempTotalAmount;
        while (purchaseStateIterator.hasNext()){
            PurchaseState purchaseState = purchaseStateIterator.next();
            Integer tempType = purchaseState.getType();
            Integer tempQuantity = purchaseState.getQuantity();
            Integer tempDeliveryTime = purchaseState.getDeliveryTime();
            if(tempDeliveryTime == 1){
                List<MaterialState> materialStateList = runningState.getStockState().getMaterialStateList();
                for (MaterialState materialState : materialStateList){
                    if(materialState.getType() == tempType){
                        Integer tempMaterialQuantity = materialState.getQuantity();
                        materialState.setQuantity(tempMaterialQuantity + tempQuantity);     //增加原料库存
                    }
                }
                purchaseStateIterator.remove();     //从数据库中删除相应采购记录
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
                operateFinancialStatementService.write("factoryRent", operateFinancialStatementService.read("factoryRent", runningState) + value, runningState);
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
        Integer tempTotalAmount = 0;
        RuleLine ruleLine = rule.getRuleLine();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Iterator<FactoryState> factoryStateIterator = factoryStateList.iterator();

        //计算续建支出总额
        for (FactoryState factoryState : factoryStateList){
            if(factoryState.getContent() != 0){
                List<LineState> lineStateList = factoryState.getLineStateList();
                Iterator<LineState> lineStateIterator = lineStateList.iterator();
                while (lineStateIterator.hasNext()){
                    LineState lineState = lineStateIterator.next();
                    if(lineState.getOwningState() < 0){
                        Integer lineId = lineState.getId();
                        Iterator<Integer> iterator = list.iterator();
                        while (iterator.hasNext()){
                            Integer tempid = iterator.next();
                            if (lineId.intValue() == tempid.intValue()){
                                if(lineState.getType() == 1){
                                    tempTotalAmount += ruleLine.getLine1UnitInvest();
                                }
                                if(lineState.getType() == 2){
                                    tempTotalAmount += ruleLine.getLine2UnitInvest();
                                }
                                if(lineState.getType() == 3){
                                    tempTotalAmount += ruleLine.getLine3UnitInvest();
                                }
                                if(lineState.getType() == 4){
                                    tempTotalAmount += ruleLine.getLine4UnitInvest();
                                }
                                if(lineState.getType() == 5){
                                    tempTotalAmount += ruleLine.getLine5UnitInvest();
                                }
                            }
                        }
                    }
                }
            }
        }

        //检测是否有足够现金
        if (tempTotalAmount > balance){
            runningState.getBaseState().setMsg("现金不足");
            return runningState;
        }
        balance -= tempTotalAmount;

        while (factoryStateIterator.hasNext()){
            FactoryState factoryState = factoryStateIterator.next();
            if(factoryState.getContent() != 0){
                List<LineState> lineStateList = factoryState.getLineStateList();
                Iterator<LineState> lineStateIterator = lineStateList.iterator();
                while (lineStateIterator.hasNext()){
                    LineState lineState = lineStateIterator.next();
                    if(lineState.getOwningState() < 0){
                        Integer lineId = lineState.getId();
                        Iterator<Integer> iterator = list.iterator();
                        while (iterator.hasNext()){
                            Integer tempid = iterator.next();
                            if (lineId.intValue() == tempid.intValue()){
                                Integer addValue = 0;
                                if(lineState.getType() == 1){
                                    addValue = ruleLine.getLine1UnitInvest();
                                }
                                if(lineState.getType() == 2){
                                    addValue = ruleLine.getLine1UnitInvest();
                                }
                                if(lineState.getType() == 3){
                                    addValue = ruleLine.getLine1UnitInvest();
                                }
                                if(lineState.getType() == 4){
                                    addValue = ruleLine.getLine1UnitInvest();
                                }
                                if(lineState.getType() == 5){
                                    addValue = ruleLine.getLine1UnitInvest();
                                }
                                lineState.setOwningState(lineState.getOwningState() + 1);
                                Integer tempValue = lineState.getValue() + addValue;
                                lineState.setValue(tempValue);
                            }
                        }
                    }
                }
            }
        }
        runningState.getFinanceState().setCashAmount(balance);
        runningState.getBaseState().setMsg("");     //清空MSG
        runningState.getBaseState().getOperateState().setBuildLine(1); //时间轴： 关闭在建生产线
        return runningState;
    }

    @Transactional
    public RunningState changeLine(String username, Integer changeType, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        Integer tempTotalAmount = 0;
        RuleLine ruleLine = rule.getRuleLine();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Iterator<FactoryState> factoryStateIterator = factoryStateList.iterator();
        for (FactoryState factoryState : factoryStateList){
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
                            if (lineId.intValue() == tempid.intValue()){
                                Integer changeInvest = 0;
                                if(lineState.getType() == 1){
                                    changeInvest = ruleLine.getLine1ChangeInvest();
                                }
                                if(lineState.getType() == 2){
                                    changeInvest = ruleLine.getLine2ChangeInvest();
                                }
                                if(lineState.getType() == 3){
                                    changeInvest = ruleLine.getLine3ChangeInvest();
                                }
                                if(lineState.getType() == 4){
                                    changeInvest = ruleLine.getLine4ChangeInvest();
                                }
                                if(lineState.getType() == 5){
                                    changeInvest = ruleLine.getLine5ChangeInvest();
                                }
                                tempTotalAmount += changeInvest;
                            }
                        }
                    }
                }
            }
        }

        if (tempTotalAmount > balance){
            runningState.getBaseState().setMsg("现金不足");
            return runningState;
        }
        balance -= tempTotalAmount;
        operateFinancialStatementService.write("transferCost", operateFinancialStatementService.read("transferCost", runningState) + tempTotalAmount, runningState);

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
                            if (lineId.intValue() == tempid.intValue()){
                                if(lineState.getType() == 1){
                                    changeTime = ruleLine.getLine1ChangeTime();
                                }
                                if(lineState.getType() == 2){
                                    changeTime = ruleLine.getLine2ChangeTime();
                                }
                                if(lineState.getType() == 3){
                                    changeTime = ruleLine.getLine3ChangeTime();
                                }
                                if(lineState.getType() == 4){
                                    changeTime = ruleLine.getLine4ChangeTime();
                                }
                                if(lineState.getType() == 5){
                                    changeTime = ruleLine.getLine5ChangeTime();
                                }
                                lineState.setProduceState(-changeTime);
                                lineState.setProductType(changeType);
                            }
                        }
                    }
                }
            }
        }
        runningState.getFinanceState().setCashAmount(balance);
        runningState.getBaseState().setMsg("");     //清空MSG
        return runningState;
    }

    @Transactional
    public RunningState continueChange(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        Integer tempTotalAmount = 0;
        RuleLine ruleLine = rule.getRuleLine();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Iterator<FactoryState> factoryStateIterator = factoryStateList.iterator();

        for (FactoryState factoryState : factoryStateList){
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
                            if (lineId.intValue() == tempid.intValue()){
                                Integer changeInvest = 0;
                                if(lineState.getType() == 1){
                                    changeInvest = ruleLine.getLine1ChangeInvest();
                                }
                                if(lineState.getType() == 2){
                                    changeInvest = ruleLine.getLine2ChangeInvest();
                                }
                                if(lineState.getType() == 3){
                                    changeInvest = ruleLine.getLine3ChangeInvest();
                                }
                                if(lineState.getType() == 4){
                                    changeInvest = ruleLine.getLine4ChangeInvest();
                                }
                                if(lineState.getType() == 5){
                                    changeInvest = ruleLine.getLine5ChangeInvest();
                                }
                                tempTotalAmount += changeInvest;
                            }
                        }
                    }
                }
            }
        }

        if (tempTotalAmount > balance){
            runningState.getBaseState().setMsg("现金不足");
            return runningState;
        }
        balance -= tempTotalAmount;
        operateFinancialStatementService.write("transferCost", operateFinancialStatementService.read("transferCost", runningState) + tempTotalAmount, runningState);

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
                            if (lineId.intValue() == tempid.intValue()){
                                lineState.setProduceState(lineState.getProduceState() + 1);
                            }
                        }
                    }
                }
            }
        }
        runningState.getFinanceState().setCashAmount(balance);
        runningState.getBaseState().setMsg("");     //清空MSG
        runningState.getBaseState().getOperateState().setContinueChange(1);     //时间轴：关闭继续转产
        logger.info("用户：{} 执行继续转产", username);
        return runningState;
    }

    @Transactional
    public RunningState saleLine(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        Integer tempTotalAmount = 0;
        Integer lostCostTotal = 0;
        RuleLine ruleLine = rule.getRuleLine();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Iterator<FactoryState> factoryStateIterator = factoryStateList.iterator();

        for (FactoryState factoryState : factoryStateList){
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
                            if (lineId.intValue() == tempid.intValue()){
                                if (lineState.getType() == 1){
                                    tempTotalAmount += ruleLine.getLine1ScrapValue();
                                    lostCostTotal += lineState.getValue() - ruleLine.getLine1ScrapValue();
                                }
                                if (lineState.getType() == 2){
                                    tempTotalAmount += ruleLine.getLine2ScrapValue();
                                    lostCostTotal += lineState.getValue() - ruleLine.getLine2ScrapValue();
                                }
                                if (lineState.getType() == 3){
                                    tempTotalAmount += ruleLine.getLine3ScrapValue();
                                    lostCostTotal += lineState.getValue() - ruleLine.getLine3ScrapValue();
                                }
                                if (lineState.getType() == 4){
                                    tempTotalAmount += ruleLine.getLine4ScrapValue();
                                    lostCostTotal += lineState.getValue() - ruleLine.getLine4ScrapValue();
                                }
                                if (lineState.getType() == 5){
                                    tempTotalAmount += ruleLine.getLine5ScrapValue();
                                    lostCostTotal += lineState.getValue() - ruleLine.getLine5ScrapValue();
                                }
                            }
                        }
                    }
                }
            }
        }

        if (tempTotalAmount > balance){
            runningState.getBaseState().setMsg("现金不足");
            return runningState;
        }
        balance += tempTotalAmount;
        operateFinancialStatementService.write("lostCost", operateFinancialStatementService.read("lostCost", runningState) + lostCostTotal, runningState);


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
                            if (lineId.intValue() == tempid.intValue()){
                                iterator.remove();
                                lineStateIterator.remove();
                                factoryState.setContent(factoryState.getContent() - 1);
                            }
                        }
                    }
                }
            }
        }
        runningState.getFinanceState().setCashAmount(balance);
        runningState.getBaseState().setMsg("");     //清空MSG
        runningState.getBaseState().getOperateState().setSaleLine(1);   //时间轴：关闭出售生产线
        logger.info("用户：{} 执行出售生产线", username);
        return runningState;
    }

    @Transactional
    public RunningState beginProduction(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        Integer balance = runningState.getFinanceState().getCashAmount();
        RuleLine ruleLine = rule.getRuleLine();
        RuleProduct ruleProduct = rule.getRuleProduct();
        RuleProductMix ruleProductMix = rule.getRuleProductMix();
        List<ProductDevState> productDevStateList = runningState.getDevState().getProductDevStateList();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Iterator<FactoryState> factoryStateIterator = factoryStateList.iterator();

        List<Integer> mixMaterialList = new ArrayList<Integer>();
        List<Integer> mixProductList = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++){
            mixMaterialList.add(0);
            mixProductList.add(0);
        }
        Integer productCost = 0;

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
                            if (lineId.intValue() == tempid.intValue()){
                                if(lineState.getProductType() == 1){
                                    productCost += ruleProduct.getProduct1ProcCost();
                                    if (ruleProductMix.getProduct1MixR1() != null){
                                        mixMaterialList.set(0, mixMaterialList.get(0) + ruleProductMix.getProduct1MixR1());
                                    }
                                    if (ruleProductMix.getProduct1MixR2() != null){
                                        mixMaterialList.set(1, mixMaterialList.get(1) + ruleProductMix.getProduct1MixR2());
                                    }
                                    if (ruleProductMix.getProduct1MixR3() != null){
                                        mixMaterialList.set(2, mixMaterialList.get(2) + ruleProductMix.getProduct1MixR3());
                                    }
                                    if (ruleProductMix.getProduct1MixR4() != null){
                                        mixMaterialList.set(3, mixMaterialList.get(3) + ruleProductMix.getProduct1MixR4());
                                    }
                                    if (ruleProductMix.getProduct1MixR5() != null){
                                        mixMaterialList.set(4, mixMaterialList.get(4) + ruleProductMix.getProduct1MixR5());
                                    }
                                    if (ruleProductMix.getProduct1MixP1() != null){
                                        mixProductList.set(0, mixProductList.get(0) + ruleProductMix.getProduct1MixP1());
                                    }
                                    if (ruleProductMix.getProduct1MixP2() != null){
                                        mixProductList.set(1, mixProductList.get(1) + ruleProductMix.getProduct1MixP2());
                                    }
                                    if (ruleProductMix.getProduct1MixP3() != null){
                                        mixProductList.set(2, mixProductList.get(2) + ruleProductMix.getProduct1MixP3());
                                    }
                                    if (ruleProductMix.getProduct1MixP4() != null){
                                        mixProductList.set(3, mixProductList.get(3) + ruleProductMix.getProduct1MixP4());
                                    }
                                    if (ruleProductMix.getProduct1MixP5() != null){
                                        mixProductList.set(4, mixProductList.get(4) + ruleProductMix.getProduct1MixP5());
                                    }
                                }
                                if(lineState.getProductType() == 2){
                                    productCost += ruleProduct.getProduct2ProcCost();
                                    if (ruleProductMix.getProduct2MixR1() != null){
                                        mixMaterialList.set(0, mixMaterialList.get(0) + ruleProductMix.getProduct2MixR1());
                                    }
                                    if (ruleProductMix.getProduct2MixR2() != null){
                                        mixMaterialList.set(1, mixMaterialList.get(1) + ruleProductMix.getProduct2MixR2());
                                    }
                                    if (ruleProductMix.getProduct2MixR3() != null){
                                        mixMaterialList.set(2, mixMaterialList.get(2) + ruleProductMix.getProduct2MixR3());
                                    }
                                    if (ruleProductMix.getProduct2MixR4() != null){
                                        mixMaterialList.set(3, mixMaterialList.get(3) + ruleProductMix.getProduct2MixR4());
                                    }
                                    if (ruleProductMix.getProduct2MixR5() != null){
                                        mixMaterialList.set(4, mixMaterialList.get(4) + ruleProductMix.getProduct2MixR5());
                                    }
                                    if (ruleProductMix.getProduct2MixP1() != null){
                                        mixProductList.set(0, mixProductList.get(0) + ruleProductMix.getProduct2MixP1());
                                    }
                                    if (ruleProductMix.getProduct2MixP2() != null){
                                        mixProductList.set(1, mixProductList.get(1) + ruleProductMix.getProduct2MixP2());
                                    }
                                    if (ruleProductMix.getProduct2MixP3() != null){
                                        mixProductList.set(2, mixProductList.get(2) + ruleProductMix.getProduct2MixP3());
                                    }
                                    if (ruleProductMix.getProduct2MixP4() != null){
                                        mixProductList.set(3, mixProductList.get(3) + ruleProductMix.getProduct2MixP4());
                                    }
                                    if (ruleProductMix.getProduct2MixP5() != null){
                                        mixProductList.set(4, mixProductList.get(4) + ruleProductMix.getProduct2MixP5());
                                    }
                                }
                                if(lineState.getProductType() == 3){
                                    productCost += ruleProduct.getProduct3ProcCost();
                                    if (ruleProductMix.getProduct3MixR1() != null){
                                        mixMaterialList.set(0, mixMaterialList.get(0) + ruleProductMix.getProduct3MixR1());
                                    }
                                    if (ruleProductMix.getProduct3MixR2() != null){
                                        mixMaterialList.set(1, mixMaterialList.get(1) + ruleProductMix.getProduct3MixR2());
                                    }
                                    if (ruleProductMix.getProduct3MixR3() != null){
                                        mixMaterialList.set(2, mixMaterialList.get(2) + ruleProductMix.getProduct3MixR3());
                                    }
                                    if (ruleProductMix.getProduct3MixR4() != null){
                                        mixMaterialList.set(3, mixMaterialList.get(3) + ruleProductMix.getProduct3MixR4());
                                    }
                                    if (ruleProductMix.getProduct3MixR5() != null){
                                        mixMaterialList.set(4, mixMaterialList.get(4) + ruleProductMix.getProduct3MixR5());
                                    }
                                    if (ruleProductMix.getProduct3MixP1() != null){
                                        mixProductList.set(0, mixProductList.get(0) + ruleProductMix.getProduct3MixP1());
                                    }
                                    if (ruleProductMix.getProduct3MixP2() != null){
                                        mixProductList.set(1, mixProductList.get(1) + ruleProductMix.getProduct3MixP2());
                                    }
                                    if (ruleProductMix.getProduct3MixP3() != null){
                                        mixProductList.set(2, mixProductList.get(2) + ruleProductMix.getProduct3MixP3());
                                    }
                                    if (ruleProductMix.getProduct3MixP4() != null){
                                        mixProductList.set(3, mixProductList.get(3) + ruleProductMix.getProduct3MixP4());
                                    }
                                    if (ruleProductMix.getProduct3MixP5() != null){
                                        mixProductList.set(4, mixProductList.get(4) + ruleProductMix.getProduct3MixP5());
                                    }
                                }
                                if(lineState.getProductType() == 4){
                                    productCost += ruleProduct.getProduct4ProcCost();
                                    if (ruleProductMix.getProduct4MixR1() != null){
                                        mixMaterialList.set(0, mixMaterialList.get(0) + ruleProductMix.getProduct4MixR1());
                                    }
                                    if (ruleProductMix.getProduct4MixR2() != null){
                                        mixMaterialList.set(1, mixMaterialList.get(1) + ruleProductMix.getProduct4MixR2());
                                    }
                                    if (ruleProductMix.getProduct4MixR3() != null){
                                        mixMaterialList.set(2, mixMaterialList.get(2) + ruleProductMix.getProduct4MixR3());
                                    }
                                    if (ruleProductMix.getProduct4MixR4() != null){
                                        mixMaterialList.set(3, mixMaterialList.get(3) + ruleProductMix.getProduct4MixR4());
                                    }
                                    if (ruleProductMix.getProduct4MixR5() != null){
                                        mixMaterialList.set(4, mixMaterialList.get(4) + ruleProductMix.getProduct4MixR5());
                                    }
                                    if (ruleProductMix.getProduct4MixP1() != null){
                                        mixProductList.set(0, mixProductList.get(0) + ruleProductMix.getProduct4MixP1());
                                    }
                                    if (ruleProductMix.getProduct4MixP2() != null){
                                        mixProductList.set(1, mixProductList.get(1) + ruleProductMix.getProduct4MixP2());
                                    }
                                    if (ruleProductMix.getProduct4MixP3() != null){
                                        mixProductList.set(2, mixProductList.get(2) + ruleProductMix.getProduct4MixP3());
                                    }
                                    if (ruleProductMix.getProduct4MixP4() != null){
                                        mixProductList.set(3, mixProductList.get(3) + ruleProductMix.getProduct4MixP4());
                                    }
                                    if (ruleProductMix.getProduct4MixP5() != null){
                                        mixProductList.set(4, mixProductList.get(4) + ruleProductMix.getProduct4MixP5());
                                    }
                                }
                                if(lineState.getProductType() == 5){
                                    productCost += ruleProduct.getProduct5ProcCost();
                                    if (ruleProductMix.getProduct5MixR1() != null){
                                        mixMaterialList.set(0, mixMaterialList.get(0) + ruleProductMix.getProduct5MixR1());
                                    }
                                    if (ruleProductMix.getProduct5MixR2() != null){
                                        mixMaterialList.set(1, mixMaterialList.get(1) + ruleProductMix.getProduct5MixR2());
                                    }
                                    if (ruleProductMix.getProduct5MixR3() != null){
                                        mixMaterialList.set(2, mixMaterialList.get(2) + ruleProductMix.getProduct5MixR3());
                                    }
                                    if (ruleProductMix.getProduct5MixR4() != null){
                                        mixMaterialList.set(3, mixMaterialList.get(3) + ruleProductMix.getProduct5MixR4());
                                    }
                                    if (ruleProductMix.getProduct5MixR5() != null){
                                        mixMaterialList.set(4, mixMaterialList.get(4) + ruleProductMix.getProduct5MixR5());
                                    }
                                    if (ruleProductMix.getProduct5MixP1() != null){
                                        mixProductList.set(0, mixProductList.get(0) + ruleProductMix.getProduct5MixP1());
                                    }
                                    if (ruleProductMix.getProduct5MixP2() != null){
                                        mixProductList.set(1, mixProductList.get(1) + ruleProductMix.getProduct5MixP2());
                                    }
                                    if (ruleProductMix.getProduct5MixP3() != null){
                                        mixProductList.set(2, mixProductList.get(2) + ruleProductMix.getProduct5MixP3());
                                    }
                                    if (ruleProductMix.getProduct5MixP4() != null){
                                        mixProductList.set(3, mixProductList.get(3) + ruleProductMix.getProduct5MixP4());
                                    }
                                    if (ruleProductMix.getProduct5MixP5() != null){
                                        mixProductList.set(4, mixProductList.get(4) + ruleProductMix.getProduct5MixP5());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //开始进行条件检查
        if (productCost > balance){
            runningState.getBaseState().setMsg("现金不足");
            return runningState;
        }else {
            runningState = decuction(mixMaterialList, mixProductList, runningState);
            if (runningState.getBaseState().getMsg().isEmpty()){
                balance -= productCost;
                //设置生产线生产状态
                for (FactoryState factoryState : factoryStateList){
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
                                    if (lineId.intValue() == tempid.intValue()){
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
                                        lineState.setProduceState(produceTime);
                                        iterator.remove();  //清除List中该线ID
                                        logger.info("用户：{} 执行开始生产,生产线ID：{}，产品：{}", username, lineId, lineState.getProductType());
                                    }
                                }
                            }
                        }
                    }
                }
            }else {
                return runningState;
            }
        }
        runningState.getFinanceState().setCashAmount(balance);
        runningState.getBaseState().setMsg("");     //清空MSG
        runningState.getBaseState().getOperateState().setBeginProduction(1);    //时间轴：关闭 开始生产
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
        //TODO:需改进增加测试订单的方法
        if(runningState.getBaseState().getTimeQuarter() == 1){
            return testAddOrder(runningState);  //每年第一季度更新应收款都用这个函数增加订单以供测试
        }else {
            return runningState;
        }

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
        for (Integer tempId : list){
            for (OrderState orderState : orderStateList){

                Integer id = orderState.getId();
                if (id.intValue() == tempId.intValue()){
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
                                        Integer directCost = 0;
                                        if(orderState.getTypeId() == 1){
                                            directCost = orderState.getQuantity() * rule.getRuleProduct().getProduct1FinalCost();
                                        }
                                        if(orderState.getTypeId() == 2){
                                            directCost = orderState.getQuantity() * rule.getRuleProduct().getProduct2FinalCost();
                                        }
                                        if(orderState.getTypeId() == 3){
                                            directCost = orderState.getQuantity() * rule.getRuleProduct().getProduct3FinalCost();
                                        }
                                        if(orderState.getTypeId() == 4){
                                            directCost = orderState.getQuantity() * rule.getRuleProduct().getProduct4FinalCost();
                                        }
                                        if(orderState.getTypeId() == 5){
                                            directCost = orderState.getQuantity() * rule.getRuleProduct().getProduct5FinalCost();
                                        }
                                        Integer grossProfit = orderState.getTotalPrice() - directCost;
                                        operateFinancialStatementService.write("salesIncome", operateFinancialStatementService.read("salesIncome", runningState) + orderState.getTotalPrice(), runningState);
                                        operateFinancialStatementService.write("directCost", operateFinancialStatementService.read("directCost", runningState) + directCost, runningState);
                                        operateFinancialStatementService.write("grossProfit", operateFinancialStatementService.read("grossProfit", runningState) + grossProfit, runningState);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        runningState.getFinanceState().setCashAmount(balance);
        logger.info("用户：{} 交单请求执行完毕", username);
        return runningState;
    }

    @Transactional
    public RunningState productDev(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        RuleProduct ruleProduct = rule.getRuleProduct();
        Integer balance = runningState.getFinanceState().getCashAmount();
        Integer tempTotalAmount = 0;
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
                        operateFinancialStatementService.write("productDevCost", operateFinancialStatementService.read("productDevCost", runningState) + devInvest, runningState);
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
                    operateFinancialStatementService.write("productDevCost", operateFinancialStatementService.read("productDevCost", runningState) + devInvest, runningState);
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
                            operateFinancialStatementService.write("factoryRent", operateFinancialStatementService.read("factoryRent", runningState) + factoryRentPrice, runningState);
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



        /**
         * 自动进行的业务操作：
         * 1.支付行政管理费
         * 2.厂房续租
         * 3.更新建设
         * 4.违约记录
         * 5.时间轴变换
         */
        if(rule.getRuleParam().getParamManagementCost() > balance){
            runningState.getBaseState().setMsg("现金不足");
        }else {
            balance -= rule.getRuleParam().getParamManagementCost();//支付行政管理费
            operateFinancialStatementService.write("managementCost", operateFinancialStatementService.read("managementCost", runningState) + rule.getRuleParam().getParamManagementCost(), runningState);
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
                    runningState.getBaseState().setMsg("现金不足 ");
                    return runningState;
                }else {
                    balance -= rentPrice;   //支付续租费用
                    factoryState.setFinalPaymentYear(runningState.getBaseState().getTimeYear());
                    operateFinancialStatementService.write("factoryRent", operateFinancialStatementService.read("factoryRent", runningState) + rentPrice, runningState);
                }
            }

        }
        List<OrderState> orderStateList = runningState.getMarketingState().getOrderStateList();
        Iterator<OrderState> orderStateIterator = orderStateList.iterator();
        Integer timeYear = runningState.getBaseState().getTimeYear();
        Integer timeQuarter = runningState.getBaseState().getTimeQuarter();
        while (orderStateIterator.hasNext()){
            OrderState orderState = orderStateIterator.next();
            if((orderState.getYear() == timeYear) && (orderState.getDeliveryTime() == timeQuarter)){
                if(orderState.getExecution() == 0){
                    orderState.setExecution(2); //设置该订单为违约
                    Integer lostCost = (int)Math.round (rule.getRuleParam().getParamPenatly() * orderState.getTotalPrice());
                    operateFinancialStatementService.write("lostCost", operateFinancialStatementService.read("lostCost", runningState) + lostCost, runningState);
                }
            }

        }

        //时间轴变换
        runningState.getBaseState().setTimeQuarter(runningState.getBaseState().getTimeQuarter() + 1);   //跳转至下个季度
        runningState.getBaseState().setState(10);   //跳转至季初
        runningState.getBaseState().getOperateState().setProductDev(0);  //解除控制 产品研发

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
        Integer tempTotalAmount = 0;
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
                    devTime = ruleMarket.getMarket2DevTime();
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
        Integer tempTotalAmount = 0;
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
//        Integer tempTotalAmount = 0;

        /**
         * 自动进行的业务操作：
         * 1.支付行政管理费
         * 2.厂房续租
         * 3.支付设备维修费
         * 4.计提折旧
         * 5.违约扣款
         * 6.时间轴变换
         */
        Integer managementCost = rule.getRuleParam().getParamManagementCost();
//        tempTotalAmount += managementCost;
        if(managementCost > balance){
            runningState.getBaseState().setMsg("现金不足");
        }else {
            balance -= managementCost;//支付行政管理费
            operateFinancialStatementService.write("managementCost", operateFinancialStatementService.read("managementCost", runningState) + managementCost, runningState);
        }

        Integer wipValue = 0;   //在制品价值

        List<FactoryState> factoryStateList = runningState.getFactoryStateList();
        Integer factoryValue = 0;   //厂房价值
        Integer equipmentValue = 0; //机器设备价值
        Integer constructionInProgressValue = 0;    //在建工程
        Integer upkeepCost = 0;     //维护费
        for (FactoryState factoryState : factoryStateList){
            if (factoryState.getOwningState() == 0){
                //对租赁状态的厂房进行续租检查
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
                        operateFinancialStatementService.write("factoryRent", operateFinancialStatementService.read("factoryRent", runningState) + rentPrice, runningState);
                    }
                }
            }else {
                //对购买状态的厂房进行价值统计
                if (factoryState.getType() == 1){
                    factoryValue += ruleFactory.getFactory1BuyPrice();
                }
                if (factoryState.getType() == 2){
                    factoryValue += ruleFactory.getFactory2BuyPrice();
                }
                if (factoryState.getType() == 3){
                    factoryValue += ruleFactory.getFactory3BuyPrice();
                }
                if (factoryState.getType() == 4){
                    factoryValue += ruleFactory.getFactory4BuyPrice();
                }
                if (factoryState.getType() == 5){
                    factoryValue += ruleFactory.getFactory5BuyPrice();
                }
            }



            List<LineState> lineStateList = factoryState.getLineStateList();
            for(LineState lineState : lineStateList){
                Integer depreciation = 0;
                if(lineState.getType() == 1){
                    if (lineState.getOwningState() > 0){
                        upkeepCost += rule.getRuleLine().getLine1Upkeep();
                    }
                    if(((lineState.getOwningState() > 1)) && ((lineState.getOwningState() <= rule.getRuleLine().getLine1DepreTime()))){
                        depreciation = rule.getRuleLine().getLine1Depreciation();
                        lineState.setValue(lineState.getValue() - depreciation);
                        operateFinancialStatementService.write("depreciation", operateFinancialStatementService.read("depreciation", runningState) + depreciation, runningState);
                    }
                    if (lineState.getOwningState() > 0){
                        equipmentValue += lineState.getValue();
                    }else {
                        constructionInProgressValue += lineState.getValue();
                    }
                }
                if(lineState.getType() == 2){
                    if (lineState.getOwningState() > 0){
                        upkeepCost += rule.getRuleLine().getLine2Upkeep();
                    }
                    if(((lineState.getOwningState() > 1)) && ((lineState.getOwningState() <= rule.getRuleLine().getLine2DepreTime()))){
                        depreciation = rule.getRuleLine().getLine2Depreciation();
                        lineState.setValue(lineState.getValue() - depreciation);
                        operateFinancialStatementService.write("depreciation", operateFinancialStatementService.read("depreciation", runningState) + depreciation, runningState);
                    }
                    if (lineState.getOwningState() > 0){
                        equipmentValue += lineState.getValue();
                    }else {
                        constructionInProgressValue += lineState.getValue();
                    }
                }
                if(lineState.getType() == 3){
                    if (lineState.getOwningState() > 0){
                        upkeepCost += rule.getRuleLine().getLine3Upkeep();
                    }
                    if(((lineState.getOwningState() > 1)) && ((lineState.getOwningState() <= rule.getRuleLine().getLine3DepreTime()))){
                        depreciation = rule.getRuleLine().getLine3Depreciation();
                        lineState.setValue(lineState.getValue() - depreciation);
                        operateFinancialStatementService.write("depreciation", operateFinancialStatementService.read("depreciation", runningState) + depreciation, runningState);
                    }
                    if (lineState.getOwningState() > 0){
                        equipmentValue += lineState.getValue();
                    }else {
                        constructionInProgressValue += lineState.getValue();
                    }
                }
                if(lineState.getType() == 4){
                    if (lineState.getOwningState() > 0){
                        upkeepCost += rule.getRuleLine().getLine4Upkeep();
                    }
                    if(((lineState.getOwningState() > 1)) && ((lineState.getOwningState() <= rule.getRuleLine().getLine4DepreTime()))){
                        depreciation = rule.getRuleLine().getLine4Depreciation();
                        lineState.setValue(lineState.getValue() - depreciation);
                        operateFinancialStatementService.write("depreciation", operateFinancialStatementService.read("depreciation", runningState) + depreciation, runningState);
                    }
                    if (lineState.getOwningState() > 0){
                        equipmentValue += lineState.getValue();
                    }else {
                        constructionInProgressValue += lineState.getValue();
                    }
                }
                if(lineState.getType() == 5){
                    if (lineState.getOwningState() > 0){
                        upkeepCost += rule.getRuleLine().getLine5Upkeep();
                    }
                    if(((lineState.getOwningState() > 1)) && ((lineState.getOwningState() <= rule.getRuleLine().getLine5DepreTime()))){
                        depreciation = rule.getRuleLine().getLine5Depreciation();
                        lineState.setValue(lineState.getValue() - depreciation);
                        operateFinancialStatementService.write("depreciation", operateFinancialStatementService.read("depreciation", runningState) + depreciation, runningState);
                    }
                    if (lineState.getOwningState() > 0){
                        equipmentValue += lineState.getValue();
                    }else {
                        constructionInProgressValue += lineState.getValue();
                    }
                }
                if(lineState.getOwningState() > 0){
                    lineState.setOwningState(lineState.getOwningState() + 1);   //更新生产线owningState
                }
                if(lineState.getOwningState() > 0 && lineState.getProduceState() > 0){
                    if (lineState.getProductType() == 1){
                        wipValue += rule.getRuleProduct().getProduct1FinalCost();
                    }
                    if (lineState.getProductType() == 2){
                        wipValue += rule.getRuleProduct().getProduct2FinalCost();
                    }
                    if (lineState.getProductType() == 3){
                        wipValue += rule.getRuleProduct().getProduct3FinalCost();
                    }
                    if (lineState.getProductType() == 4){
                        wipValue += rule.getRuleProduct().getProduct4FinalCost();
                    }
                    if (lineState.getProductType() == 5){
                        wipValue += rule.getRuleProduct().getProduct5FinalCost();
                    }
                }
            }
        }
        if (upkeepCost > balance){
            runningState.getBaseState().setMsg("现金不足");
            return runningState;
        }else {
            balance -= upkeepCost;  //一次性扣缴设备维护费
            operateFinancialStatementService.write("upkeepCost", upkeepCost, runningState);
        }

        List<OrderState> orderStateList = runningState.getMarketingState().getOrderStateList();
        Iterator<OrderState> orderStateIterator = orderStateList.iterator();
        Integer timeYear = runningState.getBaseState().getTimeYear();
        Integer timeQuarter = runningState.getBaseState().getTimeQuarter();
        while (orderStateIterator.hasNext()){
            OrderState orderState = orderStateIterator.next();
            if((orderState.getYear() == timeYear) && (orderState.getDeliveryTime() == timeQuarter)){
                if(orderState.getExecution() == 0){
                    System.out.println("年末记录订单违约");
                    orderState.setExecution(2); //设置该订单为违约
                    Integer lostCost = (int)Math.round (rule.getRuleParam().getParamPenatly() * orderState.getTotalPrice());
                    operateFinancialStatementService.write("lostCost", operateFinancialStatementService.read("lostCost", runningState) + lostCost, runningState);
                }
            }
        }
        Integer lostCost = operateFinancialStatementService.read("lostCost", runningState);
        if(lostCost > balance){
            runningState.getBaseState().setMsg("现金不足");
            return runningState;
        }else {
            balance -= lostCost;    //一次性扣缴违约金
        }

        //财务报表汇总计算
        //综合费用表各科目覆写，用 0 替换掉NULL
        operateFinancialStatementService.write("advertisingCost", operateFinancialStatementService.read("advertisingCost", runningState), runningState);
        operateFinancialStatementService.write("upkeepCost", operateFinancialStatementService.read("upkeepCost", runningState), runningState);
        operateFinancialStatementService.write("lostCost", operateFinancialStatementService.read("lostCost", runningState), runningState);
        operateFinancialStatementService.write("transferCost", operateFinancialStatementService.read("transferCost", runningState), runningState);
        operateFinancialStatementService.write("factoryRent", operateFinancialStatementService.read("factoryRent", runningState), runningState);
        operateFinancialStatementService.write("marketDevCost", operateFinancialStatementService.read("marketDevCost", runningState), runningState);
        operateFinancialStatementService.write("isoDevCost", operateFinancialStatementService.read("isoDevCost", runningState), runningState);
        operateFinancialStatementService.write("productDevCost", operateFinancialStatementService.read("productDevCost", runningState), runningState);
        operateFinancialStatementService.write("infomationCost", operateFinancialStatementService.read("infomationCost", runningState), runningState);

        operateFinancialStatementService.write("omnibusCost",
                operateFinancialStatementService.read("managementCost", runningState) +
                        operateFinancialStatementService.read("advertisingCost", runningState) +
                        operateFinancialStatementService.read("upkeepCost", runningState) +
                        operateFinancialStatementService.read("lostCost", runningState) +
                        operateFinancialStatementService.read("transferCost", runningState) +
                        operateFinancialStatementService.read("factoryRent", runningState) +
                        operateFinancialStatementService.read("marketDevCost", runningState) +
                        operateFinancialStatementService.read("isoDevCost", runningState) +
                        operateFinancialStatementService.read("productDevCost", runningState) +
                        operateFinancialStatementService.read("infomationCost", runningState),
                runningState);      //综合费用合计

        operateFinancialStatementService.write("salesIncome", operateFinancialStatementService.read("salesIncome", runningState), runningState);    //销售收入合计
        operateFinancialStatementService.write("directCost", operateFinancialStatementService.read("directCost", runningState), runningState);      //直接成本合计
        operateFinancialStatementService.write("grossProfit", operateFinancialStatementService.read("grossProfit", runningState), runningState);    //毛利合计

        operateFinancialStatementService.write("profitBeforeDepreciation",
                operateFinancialStatementService.read("grossProfit",runningState) - operateFinancialStatementService.read("omnibusCost", runningState),
                runningState);      //折旧前利润
        operateFinancialStatementService.write("depreciation", operateFinancialStatementService.read("depreciation", runningState), runningState);  //覆写 折旧
        operateFinancialStatementService.write("profitBeforeIntetest",
                operateFinancialStatementService.read("profitBeforeDepreciation", runningState) - operateFinancialStatementService.read("depreciation", runningState),
                runningState);      //利前利润
        operateFinancialStatementService.write("financialCost", operateFinancialStatementService.read("financialCost", runningState), runningState);    //覆写 财务费用

        operateFinancialStatementService.write("profitBeforeTax",
                operateFinancialStatementService.read("profitBeforeIntetest", runningState) - operateFinancialStatementService.read("financialCost", runningState),
                runningState);      //税前利润

        Integer incomeTax = 0;  //所得税计算
        if(timeYear > 1){
            if((operateFinancialStatementService.read("profitBeforeTax", runningState) + operateFinancialStatementService.readWithTime(timeYear -1, "ownersEquity", runningState)) > rule.getRuleParam().getParamInitialCash()){
                incomeTax = (int)Math.round(operateFinancialStatementService.read("profitBeforeTax", runningState) * rule.getRuleParam().getParamTaxRate());
                operateFinancialStatementService.write("incomeTax", incomeTax, runningState);
            }else {
                operateFinancialStatementService.write("incomeTax", 0, runningState);
            }
        }else {
            if(operateFinancialStatementService.read("profitBeforeTax", runningState) > rule.getRuleParam().getParamInitialCash()){
                incomeTax = (int)Math.round(operateFinancialStatementService.read("profitBeforeTax", runningState) * rule.getRuleParam().getParamTaxRate());
                operateFinancialStatementService.write("incomeTax", incomeTax, runningState);
            }else {
                operateFinancialStatementService.write("incomeTax", 0, runningState);
            }
        }
        operateFinancialStatementService.write("netProfit",
                operateFinancialStatementService.read("profitBeforeTax", runningState) - operateFinancialStatementService.read("incomeTax", runningState),
                runningState);      //净利润

        operateFinancialStatementService.write("cashAmount", balance, runningState); //流动资产 - 现金

        Integer receivableTotal = 0;
        List<ReceivableState> receivableStateList = runningState.getFinanceState().getReceivableStateList();
        for(ReceivableState receivableState : receivableStateList){
            if(receivableState.getAmounts() > 0){
                receivableTotal += receivableState.getAmounts();
            }
        }
        operateFinancialStatementService.write("receivableTotal", receivableTotal, runningState);   //流动资产 - 应收款
        operateFinancialStatementService.write("wipValue", wipValue, runningState);     //流动资产 - 在制品

        Integer finishedProductValue = 0;
        List<ProductState> productStateList = runningState.getStockState().getProductStateList();
        for(ProductState productState : productStateList){
            if(productState.getQuantity() > 0){
                if (productState.getType() == 1){
                    finishedProductValue += productState.getQuantity() * rule.getRuleProduct().getProduct1FinalCost();
                }
                if (productState.getType() == 2){
                    finishedProductValue += productState.getQuantity() * rule.getRuleProduct().getProduct2FinalCost();
                }
                if (productState.getType() == 3){
                    finishedProductValue += productState.getQuantity() * rule.getRuleProduct().getProduct3FinalCost();
                }
                if (productState.getType() == 4){
                    finishedProductValue += productState.getQuantity() * rule.getRuleProduct().getProduct4FinalCost();
                }
                if (productState.getType() == 5){
                    finishedProductValue += productState.getQuantity() * rule.getRuleProduct().getProduct5FinalCost();
                }
            }
        }
        operateFinancialStatementService.write("finishedProductValue", finishedProductValue, runningState);     //流动资产 - 产成品

        Integer materialVaule = 0;
        List<MaterialState> materialStateList = runningState.getStockState().getMaterialStateList();
        for (MaterialState materialState : materialStateList){
            if(materialState.getQuantity() > 0){
                if (materialState.getType() == 1){
                    materialVaule += materialState.getQuantity() * rule.getRuleMaterial().getMaterial1Price();
                }
                if (materialState.getType() == 2){
                    materialVaule += materialState.getQuantity() * rule.getRuleMaterial().getMaterial2Price();
                }
                if (materialState.getType() == 3){
                    materialVaule += materialState.getQuantity() * rule.getRuleMaterial().getMaterial3Price();
                }
                if (materialState.getType() == 4){
                    materialVaule += materialState.getQuantity() * rule.getRuleMaterial().getMaterial4Price();
                }
                if (materialState.getType() == 5){
                    materialVaule += materialState.getQuantity() * rule.getRuleMaterial().getMaterial5Price();
                }
            }
        }
        operateFinancialStatementService.write("materialVaule", materialVaule, runningState);       //流动资产 - 原材料

        operateFinancialStatementService.write("currentAssets",
                operateFinancialStatementService.read("cashAmount", runningState) +
                        operateFinancialStatementService.read("receivableTotal", runningState) +
                        operateFinancialStatementService.read("wipValue", runningState) +
                        operateFinancialStatementService.read("finishedProductValue", runningState) +
                        operateFinancialStatementService.read("materialVaule", runningState),
                runningState);      //流动资产合计

        operateFinancialStatementService.write("factoryValue", factoryValue, runningState);     //固定资产 - 厂房
        operateFinancialStatementService.write("equipmentValue", equipmentValue, runningState);     //固定资产 - 机器设备
        operateFinancialStatementService.write("constructionInProgressValue", constructionInProgressValue, runningState);     //固定资产 - 在建工程
        operateFinancialStatementService.write("fixedAssets",
                operateFinancialStatementService.read("factoryValue", runningState) +
                        operateFinancialStatementService.read("equipmentValue", runningState) +
                        operateFinancialStatementService.read("constructionInProgressValue", runningState),
                runningState);      //固定资产合计

        operateFinancialStatementService.write("assetsTotal",
                operateFinancialStatementService.read("currentAssets", runningState) +
                        operateFinancialStatementService.read("fixedAssets", runningState),
                runningState);      //资产合计

        Integer longTermDebt = 0;
        Integer shortTermDebt = 0;
        List<DebtState> debtStateList = runningState.getFinanceState().getDebtStateList();
        for (DebtState debtState : debtStateList){
            if (debtState.getDebtType() == 1){
                shortTermDebt += debtState.getAmounts();
            }
            if (debtState.getDebtType() == 2){
                longTermDebt += debtState.getAmounts();
            }
        }
        operateFinancialStatementService.write("longTermDebt", longTermDebt, runningState);     //负债 - 长期负债
        operateFinancialStatementService.write("shortTermDebt", shortTermDebt, runningState);     //负债 - 短期负债
        operateFinancialStatementService.write("duesTotal", 0, runningState);     //负债 - 应付款
        operateFinancialStatementService.write("debtTotal",
                operateFinancialStatementService.read("longTermDebt", runningState) +
                        operateFinancialStatementService.read("shortTermDebt", runningState) +
                        operateFinancialStatementService.read("duesTotal", runningState),
                runningState);      //负债合计

        if (timeYear == 1){
            operateFinancialStatementService.write("profitRetention", 0, runningState); //权益 - 利润留存
        }else {
            operateFinancialStatementService.write("profitRetention",
                    operateFinancialStatementService.readWithTime(timeYear - 1, "profitRetention", runningState) +
                            operateFinancialStatementService.readWithTime(timeYear - 1, "netProfit", runningState),
                    runningState);
        }
        operateFinancialStatementService.write("ownersEquity",
                operateFinancialStatementService.read("equityCapital", runningState) +
                        operateFinancialStatementService.read("profitRetention", runningState) +
                        operateFinancialStatementService.read("netProfit", runningState),
                runningState);      //权益合计

        operateFinancialStatementService.write("debtTotalAndOwnersEquity",
                operateFinancialStatementService.read("debtTotal", runningState) +
                        operateFinancialStatementService.read("ownersEquity", runningState),
                runningState);      //负债权益合计


        //时间轴变换
        runningState.getBaseState().setTimeYear(runningState.getBaseState().getTimeYear() + 1);     //跳转至下一年
        runningState.getBaseState().setTimeQuarter(0);   //跳转至年初
        runningState.getBaseState().setState(1);   //跳转至季初
        runningState.getBaseState().getOperateState().setProductDev(0);  //解除控制 产品研发
        runningState.getBaseState().getOperateState().setMarketDev(0);  //解除控制 市场开拓
        runningState.getBaseState().getOperateState().setQualificationDev(0);   //解除控制 资质认证
        runningState.getBaseState().getOperateState().setReport(0); //允许 填写报表

        runningState.getFinanceState().setCashAmount(balance);
        runningState.getBaseState().setMsg(""); //清空MSG
        return runningState;
    }

    @Transactional
    public RunningState discount(String username, String[] arrays){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        RuleParam ruleParam = rule.getRuleParam();
        Integer balance = runningState.getFinanceState().getCashAmount();
        List<ReceivableState> receivableStateList = runningState.getFinanceState().getReceivableStateList();
        Iterator<ReceivableState> receivableStateIterator = receivableStateList.iterator();
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        if(ruleParam.getParamDiscountMode() == 0){
            //独立贴现
            for(int i = 1; i < 5; i++){
                while (receivableStateIterator.hasNext()){
                    ReceivableState receivableState = receivableStateIterator.next();
                    if(receivableState.getAccountPeriod() == i){
                        Integer tempAmounts = list.get(i - 1);
                        Integer interest = 0;
                        if (tempAmounts > receivableState.getAmounts()){
                            runningState.getBaseState().setMsg("应收款不足");
                            return runningState;
                        }else {
                            receivableState.setAmounts(receivableState.getAmounts() - tempAmounts);
                            if(i <= 2){
                                interest = (int)Math.ceil(tempAmounts * ruleParam.getParamShortTermDiscountRates());    //贴息向上取整
                            }else {
                                interest = (int)Math.ceil(tempAmounts * ruleParam.getParamLongTermDiscountRates());
                            }
                            balance += tempAmounts - interest;
                            operateFinancialStatementService.write("financialCost", interest, runningState);
                        }
                    }
                }
            }
        }else {
            Integer tempShortTotal = 0;
            Integer tempLongTotal = 0;
            for(int i = 1; i < 5; i++){
                while (receivableStateIterator.hasNext()){
                    ReceivableState receivableState = receivableStateIterator.next();
                    if(receivableState.getAccountPeriod() == i){
                        Integer tempAmounts = list.get(i - 1);
                        if(i <= 2){
                            tempShortTotal += tempAmounts;
                        }else {
                            tempLongTotal += tempAmounts;
                        }
                        Integer interest = 0;
                        if (tempAmounts > receivableState.getAmounts()){
                            runningState.getBaseState().setMsg("应收款不足");
                            return runningState;
                        }else {
                            receivableState.setAmounts(receivableState.getAmounts() - tempAmounts);
                            if(i == 2){
                                interest = (int)Math.ceil(tempShortTotal * ruleParam.getParamShortTermDiscountRates());    //贴息向上取整
                                balance += tempShortTotal - interest;
                                operateFinancialStatementService.write("financialCost", interest, runningState);
                            }
                            if(i == 4){
                                interest = (int)Math.ceil(tempLongTotal * ruleParam.getParamLongTermDiscountRates());
                                balance += tempLongTotal - interest;
                                operateFinancialStatementService.write("financialCost", interest, runningState);
                            }
                        }
                    }
                }
            }
        }



        runningState.getFinanceState().setCashAmount(balance);
        return runningState;
    }

    @Transactional
    public RunningState decuction(List<Integer> reduceMaterialList, List<Integer> reduceProductList, RunningState runningState){
        runningState.getBaseState().setMsg("");
        List<MaterialState> materialStateList = runningState.getStockState().getMaterialStateList();
        List<ProductState> productStateList = runningState.getStockState().getProductStateList();
        int check = 0;

        //检查是否有足够库存
        for (MaterialState materialState : materialStateList){
            int type = materialState.getType();
            if (reduceMaterialList.get(type - 1) > materialState.getQuantity()){
                check = 1;
            }
        }
        for (ProductState productState : productStateList){
            int type = productState.getType();
            if (reduceProductList.get(type - 1) > productState.getQuantity()){
                check = 1;
            }
        }
        if (check != 0){
            runningState.getBaseState().setMsg("原料或产品不足");
            return runningState;
        }else {
            for (MaterialState materialState : materialStateList){
                int type = materialState.getType();
                materialState.setQuantity(materialState.getQuantity() - reduceMaterialList.get(type - 1));
            }
            for (ProductState productState : productStateList){
                int type = productState.getType();
                productState.setQuantity(productState.getQuantity() - reduceProductList.get(type - 1));
            }
        }
        return runningState;
    }

    @Transactional
    public RunningState addition(List<Integer> addMaterialList, List<Integer> addProductList, RunningState runningState){
        List<MaterialState> materialStateList = runningState.getStockState().getMaterialStateList();
        List<ProductState> productStateList = runningState.getStockState().getProductStateList();

        for (MaterialState materialState : materialStateList){
            int type = materialState.getType();
            materialState.setQuantity(materialState.getQuantity() + addMaterialList.get(type - 1));
        }
        for (ProductState productState : productStateList){
            int type = productState.getType();
            productState.setQuantity(productState.getQuantity() + addProductList.get(type - 1));
        }
        return runningState;
    }

    @Transactional
    public RunningState emergencyPurchase(String username, String[] arrays1, String[] arrays2){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        RuleParam ruleParam = rule.getRuleParam();
        Integer balance = runningState.getFinanceState().getCashAmount();
        Integer tempTotalAmount = 0;
        Integer lostCostTotal = 0;

        int arrayLength1 = arrays1.length;
        List<Integer> list1 = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength1; i++){
            list1.add(Integer.parseInt(arrays1[i]));
        }
        int arrayLength2 = arrays2.length;
        List<Integer> list2 = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength2; i++){
            list2.add(Integer.parseInt(arrays2[i]));
        }

        List<MaterialState> materialStateList = runningState.getStockState().getMaterialStateList();
        List<ProductState> productStateList = runningState.getStockState().getProductStateList();
        for (MaterialState materialState : materialStateList){
            Integer type = materialState.getType();
            if (type == 1){
                tempTotalAmount += (int)(rule.getRuleMaterial().getMaterial1Price() * rule.getRuleParam().getParamMaterailBuyRation() * list1.get(type -1));
                lostCostTotal += (int)(rule.getRuleMaterial().getMaterial1Price() * rule.getRuleParam().getParamMaterailBuyRation() * list1.get(type -1) - rule.getRuleMaterial().getMaterial1Price() * list1.get(type -1));
            }
            if (type == 2){
                tempTotalAmount += (int)(rule.getRuleMaterial().getMaterial2Price() * rule.getRuleParam().getParamMaterailBuyRation() * list1.get(type -1));
                lostCostTotal += (int)(rule.getRuleMaterial().getMaterial2Price() * rule.getRuleParam().getParamMaterailBuyRation() * list1.get(type -1) - rule.getRuleMaterial().getMaterial2Price() * list1.get(type -1));
            }
            if (type == 3){
                tempTotalAmount += (int)(rule.getRuleMaterial().getMaterial3Price() * rule.getRuleParam().getParamMaterailBuyRation() * list1.get(type -1));
                lostCostTotal += (int)(rule.getRuleMaterial().getMaterial3Price() * rule.getRuleParam().getParamMaterailBuyRation() * list1.get(type -1) - rule.getRuleMaterial().getMaterial3Price() * list1.get(type -1));
            }
            if (type == 4){
                tempTotalAmount += (int)(rule.getRuleMaterial().getMaterial4Price() * rule.getRuleParam().getParamMaterailBuyRation() * list1.get(type -1));
                lostCostTotal += (int)(rule.getRuleMaterial().getMaterial4Price() * rule.getRuleParam().getParamMaterailBuyRation() * list1.get(type -1) - rule.getRuleMaterial().getMaterial4Price() * list1.get(type -1));
            }
            if (type == 5){
                tempTotalAmount += (int)(rule.getRuleMaterial().getMaterial5Price() * rule.getRuleParam().getParamMaterailBuyRation() * list1.get(type -1));
                lostCostTotal += (int)(rule.getRuleMaterial().getMaterial5Price() * rule.getRuleParam().getParamMaterailBuyRation() * list1.get(type -1) - rule.getRuleMaterial().getMaterial5Price() * list1.get(type -1));
            }
        }
        for (ProductState productState : productStateList){
            Integer type = productState.getType();
            if (type == 1){
                tempTotalAmount += (int)(rule.getRuleProduct().getProduct1FinalCost() * rule.getRuleParam().getParamProductBuyRation() * list2.get(type -1));
                lostCostTotal += (int)(rule.getRuleProduct().getProduct1FinalCost() * rule.getRuleParam().getParamProductBuyRation() * list2.get(type -1) - rule.getRuleProduct().getProduct1FinalCost() * list2.get(type -1));
            }
            if (type == 2){
                tempTotalAmount += (int)(rule.getRuleProduct().getProduct2FinalCost() * rule.getRuleParam().getParamProductBuyRation() * list2.get(type -1));
                lostCostTotal += (int)(rule.getRuleProduct().getProduct2FinalCost() * rule.getRuleParam().getParamProductBuyRation() * list2.get(type -1) - rule.getRuleProduct().getProduct2FinalCost() * list2.get(type -1));
            }
            if (type == 3){
                tempTotalAmount += (int)(rule.getRuleProduct().getProduct3FinalCost() * rule.getRuleParam().getParamProductBuyRation() * list2.get(type -1));
                lostCostTotal += (int)(rule.getRuleProduct().getProduct3FinalCost() * rule.getRuleParam().getParamProductBuyRation() * list2.get(type -1) - rule.getRuleProduct().getProduct3FinalCost() * list2.get(type -1));
            }
            if (type == 4){
                tempTotalAmount += (int)(rule.getRuleProduct().getProduct4FinalCost() * rule.getRuleParam().getParamProductBuyRation() * list2.get(type -1));
                lostCostTotal += (int)(rule.getRuleProduct().getProduct4FinalCost() * rule.getRuleParam().getParamProductBuyRation() * list2.get(type -1) - rule.getRuleProduct().getProduct4FinalCost() * list2.get(type -1));
            }
            if (type == 5){
                tempTotalAmount += (int)(rule.getRuleProduct().getProduct5FinalCost() * rule.getRuleParam().getParamProductBuyRation() * list2.get(type -1));
                lostCostTotal += (int)(rule.getRuleProduct().getProduct5FinalCost() * rule.getRuleParam().getParamProductBuyRation() * list2.get(type -1) - rule.getRuleProduct().getProduct5FinalCost() * list2.get(type -1));
            }
        }

        if (tempTotalAmount > balance){
            runningState.getBaseState().setMsg("现金不足");
            return runningState;
        }
        balance -= tempTotalAmount;
        operateFinancialStatementService.write("lostCost", operateFinancialStatementService.read("lostCost", runningState) + lostCostTotal, runningState);
        runningState.getFinanceState().setCashAmount(balance);
        runningState = addition(list1, list2, runningState);
        return runningState;
    }

    @Transactional
    public RunningState testAddOrder(RunningState runningState){
        if (runningState.getBaseState().getTimeYear() > 1){
            List<OrderState> orderStateList = runningState.getMarketingState().getOrderStateList();
            OrderState orderState1 = new OrderState();
            orderState1.setAccountPeriod(3);
            orderState1.setArea(1);
            orderState1.setDeliveryTime(4);
            orderState1.setExecution(0);
            orderState1.setOrderId(10 + runningState.getBaseState().getTimeYear());
            orderState1.setOwner("OWNER");
            orderState1.setQualificate(0);
            orderState1.setTotalPrice(100);
            orderState1.setTypeId(1);
            orderState1.setQuantity(2);
            orderState1.setUnitPrice(50);
            orderState1.setYear(runningState.getBaseState().getTimeYear());
            orderStateList.add(orderState1);

            OrderState orderState2 = new OrderState();
            orderState2.setAccountPeriod(1);
            orderState2.setArea(1);
            orderState2.setDeliveryTime(3);
            orderState2.setExecution(0);
            orderState2.setOrderId(20 + runningState.getBaseState().getTimeYear());
            orderState2.setOwner("OWNER");
            orderState2.setQualificate(0);
            orderState2.setTotalPrice(140);
            orderState2.setTypeId(2);
            orderState2.setQuantity(2);
            orderState2.setUnitPrice(70);
            orderState2.setYear(runningState.getBaseState().getTimeYear());
            orderStateList.add(orderState2);

            OrderState orderState3 = new OrderState();
            orderState3.setAccountPeriod(1);
            orderState3.setArea(1);
            orderState3.setDeliveryTime(4);
            orderState3.setExecution(0);
            orderState3.setOrderId(30 + runningState.getBaseState().getTimeYear());
            orderState3.setOwner("OWNER");
            orderState3.setQualificate(0);
            orderState3.setTotalPrice(144);
            orderState3.setTypeId(1);
            orderState3.setQuantity(3);
            orderState3.setUnitPrice(48);
            orderState3.setYear(runningState.getBaseState().getTimeYear());
            orderStateList.add(orderState3);
        }
        return runningState;
    }
}
