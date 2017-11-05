package bobo.erp.service.running;

import bobo.erp.entity.state.RunningState;
import bobo.erp.entity.state.finance.FinancialStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * Created by 59814 on 2017/8/7.
 */
@Service
public class OperateFinancialStatementService {
    private Logger logger = LoggerFactory.getLogger(OperateFinancialStatementService.class);

    @Transactional
    public RunningState write(String attribute, Integer value, RunningState runningState){
        //根据runningState中的年份查表
        Integer year = runningState.getBaseState().getTimeYear();
//        logger.info("第 {}年报表写入,科目：{},金额:{}", year, attribute, value);
        List<FinancialStatement> financialStatementList = runningState.getFinanceState().getFinancialStatementList();
        Iterator<FinancialStatement> financialStatementIterator = financialStatementList.iterator();
        while (financialStatementIterator.hasNext()){
            FinancialStatement financialStatement = financialStatementIterator.next();
            if (year == financialStatement.getYear()){
                switch (attribute){
                    case "year":
                        financialStatement.setYear(value);
                        break;
                    case "managementCost":
                        financialStatement.setManagementCost(value);
                        break;
                    case "advertisingCost":
                        financialStatement.setAdvertisingCost(value);
                        break;
                    case "upkeepCost":
                        financialStatement.setUpkeepCost(value);
                        break;
                    case "lostCost":
                        financialStatement.setLostCost(value);
                        break;
                    case "transferCost":
                        financialStatement.setTransferCost(value);
                        break;
                    case "factoryRent":
                        financialStatement.setFactoryRent(value);
                        break;
                    case "marketDevCost":
                        financialStatement.setMarketDevCost(value);
                        break;
                    case "isoDevCost":
                        financialStatement.setIsoDevCost(value);
                        break;
                    case "productDevCost":
                        financialStatement.setProductDevCost(value);
                        break;
                    case "infomationCost":
                        financialStatement.setInfomationCost(value);
                        break;
                    case "omnibusCost":
                        financialStatement.setOmnibusCost(value);
                        break;
                    case "salesIncome":
                        financialStatement.setSalesIncome (value);
                        break;
                    case "directCost":
                        financialStatement.setDirectCost (value);
                        break;
                    case "grossProfit":
                        financialStatement.setGrossProfit(value);
                        break;
                    case "profitBeforeDepreciation":
                        financialStatement.setProfitBeforeDepreciation(value);
                        break;
                    case "depreciation":
                        financialStatement.setDepreciation(value);
                        break;
                    case "profitBeforeIntetest":
                        financialStatement.setProfitBeforeIntetest(value);
                        break;
                    case "financialCost":
                        financialStatement.setFinancialCost(value);
                        break;
                    case "profitBeforeTax":
                        financialStatement.setProfitBeforeTax(value);
                        break;
                    case "incomeTax":
                        financialStatement.setIncomeTax(value);
                        break;
                    case "netProfit":
                        financialStatement.setNetProfit(value);
                        break;
                    case "cashAmount":
                        financialStatement.setCashAmount(value);
                        break;
                    case "receivableTotal":
                        financialStatement.setReceivableTotal(value);
                        break;
                    case "wipValue":
                        financialStatement.setWipValue(value);
                        break;
                    case "finishedProductValue":
                        financialStatement.setFinishedProductValue(value);
                        break;
                    case "materialVaule":
                        financialStatement.setMaterialVaule(value);
                        break;
                    case "currentAssets":
                        financialStatement.setCurrentAssets(value);
                        break;
                    case "factoryValue":
                        financialStatement.setFactoryValue(value);
                        break;
                    case "equipmentValue":
                        financialStatement.setEquipmentValue(value);
                        break;
                    case "constructionInProgressValue":
                        financialStatement.setConstructionInProgressValue(value);
                        break;
                    case "fixedAssets":
                        financialStatement.setFixedAssets(value);
                        break;
                    case "assetsTotal":
                        financialStatement.setAssetsTotal(value);
                        break;
                    case "longTermDebt":
                        financialStatement.setLongTermDebt(value);
                        break;
                    case "shortTermDebt":
                        financialStatement.setShortTermDebt(value);
                        break;
                    case "duesTotal":
                        financialStatement.setDuesTotal(value);
                        break;
                    case "debtTotal":
                        financialStatement.setDebtTotal(value);
                        break;
                    case "equityCapital":
                        financialStatement.setEquityCapital(value);
                        break;
                    case "profitRetention":
                        financialStatement.setProfitRetention(value);
                        break;
                    case "ownersEquity":
                        financialStatement.setOwnersEquity(value);
                        break;
                    case "debtTotalAndOwnersEquity":
                        financialStatement.setDebtTotalAndOwnersEquity(value);
                        break;
                    default:
                        System.out.println("没有找到科目：" + attribute + " 金额：" + value);
                        logger.error("没有找到科目:{},请检查入口参数", attribute);
                        break;
                }
            }
        }
        return runningState;
    }

    @Transactional
    public Integer read(String attribute, RunningState runningState){
        //根据runningState中的年份查表
        Integer year = runningState.getBaseState().getTimeYear();
//        logger.info("第 {}年报表读取,科目：{}", year, attribute);
        List<FinancialStatement> financialStatementList = runningState.getFinanceState().getFinancialStatementList();
        Iterator<FinancialStatement> financialStatementIterator = financialStatementList.iterator();
        Integer result = 0;
        while (financialStatementIterator.hasNext()){
            FinancialStatement financialStatement = financialStatementIterator.next();
            if (year == financialStatement.getYear()){
                switch (attribute){
                    case "year":
                        result = financialStatement.getYear();
                        break;

                    case "managementCost":
                        result = financialStatement.getManagementCost();
                        break;

                    case "advertisingCost":
                        result = financialStatement.getAdvertisingCost();
                        break;

                    case "upkeepCost":
                        result = financialStatement.getUpkeepCost();
                        break;

                    case "lostCost":
                        result = financialStatement.getLostCost();
                        break;

                    case "transferCost":
                        result = financialStatement.getTransferCost();
                        break;

                    case "factoryRent":
                        result = financialStatement.getFactoryRent();
                        break;

                    case "marketDevCost":
                        result = financialStatement.getMarketDevCost();
                        break;

                    case "isoDevCost":
                        result = financialStatement.getIsoDevCost();
                        break;

                    case "productDevCost":
                        result = financialStatement.getProductDevCost();
                        break;
                    case "infomationCost":
                        result = financialStatement.getInfomationCost();
                        break;

                    case "omnibusCost":
                        result = financialStatement.getOmnibusCost();
                        break;

                    case "salesIncome":
                        result = financialStatement.getSalesIncome ();
                        break;

                    case "directCost":
                        result = financialStatement.getDirectCost ();
                        break;

                    case "grossProfit":
                        result = financialStatement.getGrossProfit();
                        break;

                    case "profitBeforeDepreciation":
                        result = financialStatement.getProfitBeforeDepreciation();
                        break;

                    case "depreciation":
                        result = financialStatement.getDepreciation();
                        break;

                    case "profitBeforeIntetest":
                        result = financialStatement.getProfitBeforeIntetest();
                        break;

                    case "financialCost":
                        result = financialStatement.getFinancialCost();
                        break;

                    case "profitBeforeTax":
                        result = financialStatement.getProfitBeforeTax();
                        break;

                    case "incomeTax":
                        result = financialStatement.getIncomeTax();
                        break;

                    case "netProfit":
                        result = financialStatement.getNetProfit();
                        break;

                    case "cashAmount":
                        result = financialStatement.getCashAmount();
                        break;

                    case "receivableTotal":
                        result = financialStatement.getReceivableTotal();
                        break;

                    case "wipValue":
                        result = financialStatement.getWipValue();
                        break;

                    case "finishedProductValue":
                        result = financialStatement.getFinishedProductValue();
                        break;

                    case "materialVaule":
                        result = financialStatement.getMaterialVaule();
                        break;

                    case "currentAssets":
                        result = financialStatement.getCurrentAssets();
                        break;

                    case "factoryValue":
                        result = financialStatement.getFactoryValue();
                        break;

                    case "equipmentValue":
                        result = financialStatement.getEquipmentValue();
                        break;

                    case "constructionInProgressValue":
                        result = financialStatement.getConstructionInProgressValue();
                        break;

                    case "fixedAssets":
                        result = financialStatement.getFixedAssets();
                        break;

                    case "assetsTotal":
                        result = financialStatement.getAssetsTotal();
                        break;

                    case "longTermDebt":
                        result = financialStatement.getLongTermDebt();
                        break;

                    case "shortTermDebt":
                        result = financialStatement.getShortTermDebt();
                        break;

                    case "duesTotal":
                        result = financialStatement.getDuesTotal();
                        break;

                    case "debtTotal":
                        result = financialStatement.getDebtTotal();
                        break;

                    case "equityCapital":
                        result = financialStatement.getEquityCapital();
                        break;

                    case "profitRetention":
                        result = financialStatement.getProfitRetention();
                        break;

                    case "ownersEquity":
                        result = financialStatement.getOwnersEquity();
                        break;

                    case "debtTotalAndOwnersEquity":
                        result = financialStatement.getDebtTotalAndOwnersEquity();
                        break;

                    default:
                        result = 0;
                        break;
                }
            }
        }
        if (result == null){
            result = 0;
        }
        return result;
    }

    @Transactional
    public RunningState writeWithTime(Integer year, String attribute, Integer value, RunningState runningState){
        List<FinancialStatement> financialStatementList = runningState.getFinanceState().getFinancialStatementList();
        Iterator<FinancialStatement> financialStatementIterator = financialStatementList.iterator();
        while (financialStatementIterator.hasNext()){
            FinancialStatement financialStatement = financialStatementIterator.next();
            if (year == financialStatement.getYear()){
                switch (attribute){
                    case "year":
                        financialStatement.setYear(value);
                        System.out.println("修改年份");
                        break;
                    case "managementCost":
                        financialStatement.setManagementCost(value);
                        break;
                    case "advertisingCost":
                        financialStatement.setAdvertisingCost(value);
                        break;
                    case "upkeepCost":
                        financialStatement.setUpkeepCost(value);
                        break;
                    case "lostCost":
                        financialStatement.setLostCost(value);
                        break;
                    case "transferCost":
                        financialStatement.setTransferCost(value);
                        break;
                    case "factoryRent":
                        financialStatement.setFactoryRent(value);
                        break;
                    case "marketDevCost":
                        financialStatement.setMarketDevCost(value);
                        break;
                    case "isoDevCost":
                        financialStatement.setIsoDevCost(value);
                        break;
                    case "productDevCost":
                        financialStatement.setProductDevCost(value);
                        break;
                    case "infomationCost":
                        financialStatement.setInfomationCost(value);
                        break;
                    case "omnibusCost":
                        financialStatement.setOmnibusCost(value);
                        break;
                    case "salesIncome":
                        financialStatement.setSalesIncome (value);
                        break;
                    case "directCost":
                        financialStatement.setDirectCost (value);
                        break;
                    case "grossProfit":
                        financialStatement.setGrossProfit(value);
                        break;
                    case "profitBeforeDepreciation":
                        financialStatement.setProfitBeforeDepreciation(value);
                        break;
                    case "depreciation":
                        financialStatement.setDepreciation(value);
                        break;
                    case "profitBeforeIntetest":
                        financialStatement.setProfitBeforeIntetest(value);
                        break;
                    case "financialCost":
                        financialStatement.setFinancialCost(value);
                        break;
                    case "profitBeforeTax":
                        financialStatement.setProfitBeforeTax(value);
                        break;
                    case "incomeTax":
                        financialStatement.setIncomeTax(value);
                        break;
                    case "netProfit":
                        financialStatement.setNetProfit(value);
                        break;
                    case "cashAmount":
                        financialStatement.setCashAmount(value);
                        break;
                    case "receivableTotal":
                        financialStatement.setReceivableTotal(value);
                        break;
                    case "wipValue":
                        financialStatement.setWipValue(value);
                        break;
                    case "finishedProductValue":
                        financialStatement.setFinishedProductValue(value);
                        break;
                    case "materialVaule":
                        financialStatement.setMaterialVaule(value);
                        break;
                    case "currentAssets":
                        financialStatement.setCurrentAssets(value);
                        break;
                    case "factoryValue":
                        financialStatement.setFactoryValue(value);
                        break;
                    case "equipmentValue":
                        financialStatement.setEquipmentValue(value);
                        break;
                    case "constructionInProgressValue":
                        financialStatement.setConstructionInProgressValue(value);
                        break;
                    case "fixedAssets":
                        financialStatement.setFixedAssets(value);
                        break;
                    case "assetsTotal":
                        financialStatement.setAssetsTotal(value);
                        break;
                    case "longTermDebt":
                        financialStatement.setLongTermDebt(value);
                        break;
                    case "shortTermDebt":
                        financialStatement.setShortTermDebt(value);
                        break;
                    case "duesTotal":
                        financialStatement.setDuesTotal(value);
                        break;
                    case "debtTotal":
                        financialStatement.setDebtTotal(value);
                        break;
                    case "equityCapital":
                        financialStatement.setEquityCapital(value);
                        break;
                    case "profitRetention":
                        financialStatement.setProfitRetention(value);
                        break;
                    case "ownersEquity":
                        financialStatement.setOwnersEquity(value);
                        break;
                    case "debtTotalAndOwnersEquity":
                        financialStatement.setDebtTotalAndOwnersEquity(value);
                        break;
                    default:
                        break;
                }
            }
        }
        return runningState;
    }

    @Transactional
    public Integer readWithTime(Integer year, String attribute, RunningState runningState){
        List<FinancialStatement> financialStatementList = runningState.getFinanceState().getFinancialStatementList();
        Iterator<FinancialStatement> financialStatementIterator = financialStatementList.iterator();
        Integer result = 0;
        while (financialStatementIterator.hasNext()){
            FinancialStatement financialStatement = financialStatementIterator.next();
            if (year == financialStatement.getYear()){
                switch (attribute){
                    case "year":
                        result = financialStatement.getYear();
                        System.out.println("输出年份");
                        break;

                    case "managementCost":
                        result = financialStatement.getManagementCost();
                        break;

                    case "advertisingCost":
                        result = financialStatement.getAdvertisingCost();
                        break;

                    case "upkeepCost":
                        result = financialStatement.getUpkeepCost();
                        break;

                    case "lostCost":
                        result = financialStatement.getLostCost();
                        break;

                    case "transferCost":
                        result = financialStatement.getTransferCost();
                        break;

                    case "factoryRent":
                        result = financialStatement.getFactoryRent();
                        break;

                    case "marketDevCost":
                        result = financialStatement.getMarketDevCost();
                        break;

                    case "isoDevCost":
                        result = financialStatement.getIsoDevCost();
                        break;

                    case "productDevCost":
                        result = financialStatement.getProductDevCost();
                        break;
                    case "infomationCost":
                        result = financialStatement.getInfomationCost();
                        break;

                    case "omnibusCost":
                        result = financialStatement.getOmnibusCost();
                        break;

                    case "salesIncome":
                        result = financialStatement.getSalesIncome ();
                        break;

                    case "directCost":
                        result = financialStatement.getDirectCost ();
                        break;

                    case "grossProfit":
                        result = financialStatement.getGrossProfit();
                        break;

                    case "profitBeforeDepreciation":
                        result = financialStatement.getProfitBeforeDepreciation();
                        break;

                    case "depreciation":
                        result = financialStatement.getDepreciation();
                        break;

                    case "profitBeforeIntetest":
                        result = financialStatement.getProfitBeforeIntetest();
                        break;

                    case "financialCost":
                        result = financialStatement.getFinancialCost();
                        break;

                    case "profitBeforeTax":
                        result = financialStatement.getProfitBeforeTax();
                        break;

                    case "incomeTax":
                        result = financialStatement.getIncomeTax();
                        break;

                    case "netProfit":
                        result = financialStatement.getNetProfit();
                        break;

                    case "cashAmount":
                        result = financialStatement.getCashAmount();
                        break;

                    case "receivableTotal":
                        result = financialStatement.getReceivableTotal();
                        break;

                    case "wipValue":
                        result = financialStatement.getWipValue();
                        break;

                    case "finishedProductValue":
                        result = financialStatement.getFinishedProductValue();
                        break;

                    case "materialVaule":
                        result = financialStatement.getMaterialVaule();
                        break;

                    case "currentAssets":
                        result = financialStatement.getCurrentAssets();
                        break;

                    case "factoryValue":
                        result = financialStatement.getFactoryValue();
                        break;

                    case "equipmentValue":
                        result = financialStatement.getEquipmentValue();
                        break;

                    case "constructionInProgressValue":
                        result = financialStatement.getConstructionInProgressValue();
                        break;

                    case "fixedAssets":
                        result = financialStatement.getFixedAssets();
                        break;

                    case "assetsTotal":
                        result = financialStatement.getAssetsTotal();
                        break;

                    case "longTermDebt":
                        result = financialStatement.getLongTermDebt();
                        break;

                    case "shortTermDebt":
                        result = financialStatement.getShortTermDebt();
                        break;

                    case "duesTotal":
                        result = financialStatement.getDuesTotal();
                        break;

                    case "debtTotal":
                        result = financialStatement.getDebtTotal();
                        break;

                    case "equityCapital":
                        result = financialStatement.getEquityCapital();
                        break;

                    case "profitRetention":
                        result = financialStatement.getProfitRetention();
                        break;

                    case "ownersEquity":
                        result = financialStatement.getOwnersEquity();
                        break;

                    case "debtTotalAndOwnersEquity":
                        result = financialStatement.getDebtTotalAndOwnersEquity();
                        break;

                    default:
                        result = 0;
                        break;
                }
            }
        }
        if (result == null){
            result = 0;
        }
        return result;
    }
}
