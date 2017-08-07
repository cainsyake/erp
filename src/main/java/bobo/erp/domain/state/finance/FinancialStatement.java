package bobo.erp.domain.state.finance;

import bobo.erp.domain.state.FinanceState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/25.
 */
@Entity
public class FinancialStatement {
    public FinancialStatement() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer year;	//年份
    private Integer managementCost;	//管理费
    private Integer advertisingCost;	//广告费
    private Integer upkeepCost;	//维护费
    private Integer lostCost;	//损失费
    private Integer transferCost;	//转产费
    private Integer factoryRent;	//厂房租金费
    private Integer marketDevCost;	//市场开拓费
    private Integer isoDevCost;	//ISO认证费
    private Integer productDevCost;	//产品研发费
    private Integer infomationCost;	//信息费
    private Integer omnibusCost;	//综合费用合计

    private Integer salesIncome;	//销售收入
    private Integer directCost;	//直接成本合计
    private Integer grossProfit;	//毛利
    private Integer profitBeforeDepreciation;	//折旧前利润
    private Integer depreciation;	//折旧
    private Integer profitBeforeIntetest;	//利前利润
    private Integer financialCost ;	//财务费用
    private Integer profitBeforeTax;	//税前利润
    private Integer incomeTax;	//所得税
    private Integer netProfit;	//净利润
    private Integer cashAmount;	//现金
    private Integer receivableTotal;	//应收款
    private Integer wipValue;	//在制品
    private Integer finishedProductValue;	//产成品
    private Integer materialVaule;	//原材料
    private Integer currentAssets;	//流动资产合计
    private Integer factoryValue;	//厂房
    private Integer equipmentValue;	//机器设备
    private Integer constructionInProgressValue;	//在建工程
    private Integer fixedAssets;	//固定资产合计
    private Integer assetsTotal;	//资产合计
    private Integer longTermDebt;	//长期负债
    private Integer shortTermDebt;	//短期负债
    private Integer duesTotal;	//应付款
    private Integer debtTotal;	//负债合计
    private Integer equityCapital;	//股东资本
    private Integer profitRetention;	//利润留存
    private Integer ownersEquity;	//权益合计
    private Integer debtTotalAndOwnersEquity;	//负债权益合计

    @ManyToOne
    private FinanceState financeState;

    @JsonBackReference
    public FinanceState getFinanceState() {
        return financeState;
    }

    @JsonBackReference
    public void setFinanceState(FinanceState financeState) {
        this.financeState = financeState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getManagementCost() {
        return managementCost;
    }

    public void setManagementCost(Integer managementCost) {
        this.managementCost = managementCost;
    }

    public Integer getAdvertisingCost() {
        return advertisingCost;
    }

    public void setAdvertisingCost(Integer advertisingCost) {
        this.advertisingCost = advertisingCost;
    }

    public Integer getUpkeepCost() {
        return upkeepCost;
    }

    public void setUpkeepCost(Integer upkeepCost) {
        this.upkeepCost = upkeepCost;
    }

    public Integer getLostCost() {
        return lostCost;
    }

    public void setLostCost(Integer lostCost) {
        this.lostCost = lostCost;
    }

    public Integer getTransferCost() {
        return transferCost;
    }

    public void setTransferCost(Integer transferCost) {
        this.transferCost = transferCost;
    }

    public Integer getFactoryRent() {
        return factoryRent;
    }

    public void setFactoryRent(Integer factoryRent) {
        this.factoryRent = factoryRent;
    }

    public Integer getMarketDevCost() {
        return marketDevCost;
    }

    public void setMarketDevCost(Integer marketDevCost) {
        this.marketDevCost = marketDevCost;
    }

    public Integer getIsoDevCost() {
        return isoDevCost;
    }

    public void setIsoDevCost(Integer isoDevCost) {
        this.isoDevCost = isoDevCost;
    }

    public Integer getProductDevCost() {
        return productDevCost;
    }

    public void setProductDevCost(Integer productDevCost) {
        this.productDevCost = productDevCost;
    }

    public Integer getInfomationCost() {
        return infomationCost;
    }

    public void setInfomationCost(Integer infomationCost) {
        this.infomationCost = infomationCost;
    }

    public Integer getOmnibusCost() {
        return omnibusCost;
    }

    public void setOmnibusCost(Integer omnibusCost) {
        this.omnibusCost = omnibusCost;
    }

    public Integer getSalesIncome() {
        return salesIncome;
    }

    public void setSalesIncome(Integer salesIncome) {
        this.salesIncome = salesIncome;
    }

    public Integer getDirectCost() {
        return directCost;
    }

    public void setDirectCost(Integer directCost) {
        this.directCost = directCost;
    }

    public Integer getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(Integer grossProfit) {
        this.grossProfit = grossProfit;
    }

    public Integer getProfitBeforeDepreciation() {
        return profitBeforeDepreciation;
    }

    public void setProfitBeforeDepreciation(Integer profitBeforeDepreciation) {
        this.profitBeforeDepreciation = profitBeforeDepreciation;
    }

    public Integer getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(Integer depreciation) {
        this.depreciation = depreciation;
    }

    public Integer getProfitBeforeIntetest() {
        return profitBeforeIntetest;
    }

    public void setProfitBeforeIntetest(Integer profitBeforeIntetest) {
        this.profitBeforeIntetest = profitBeforeIntetest;
    }

    public Integer getFinancialCost() {
        return financialCost;
    }

    public void setFinancialCost(Integer financialCost) {
        this.financialCost = financialCost;
    }

    public Integer getProfitBeforeTax() {
        return profitBeforeTax;
    }

    public void setProfitBeforeTax(Integer profitBeforeTax) {
        this.profitBeforeTax = profitBeforeTax;
    }

    public Integer getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(Integer incomeTax) {
        this.incomeTax = incomeTax;
    }

    public Integer getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(Integer netProfit) {
        this.netProfit = netProfit;
    }

    public Integer getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Integer cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Integer getReceivableTotal() {
        return receivableTotal;
    }

    public void setReceivableTotal(Integer receivableTotal) {
        this.receivableTotal = receivableTotal;
    }

    public Integer getWipValue() {
        return wipValue;
    }

    public void setWipValue(Integer wipValue) {
        this.wipValue = wipValue;
    }

    public Integer getFinishedProductValue() {
        return finishedProductValue;
    }

    public void setFinishedProductValue(Integer finishedProductValue) {
        this.finishedProductValue = finishedProductValue;
    }

    public Integer getMaterialVaule() {
        return materialVaule;
    }

    public void setMaterialVaule(Integer materialVaule) {
        this.materialVaule = materialVaule;
    }

    public Integer getCurrentAssets() {
        return currentAssets;
    }

    public void setCurrentAssets(Integer currentAssets) {
        this.currentAssets = currentAssets;
    }

    public Integer getFactoryValue() {
        return factoryValue;
    }

    public void setFactoryValue(Integer factoryValue) {
        this.factoryValue = factoryValue;
    }

    public Integer getEquipmentValue() {
        return equipmentValue;
    }

    public void setEquipmentValue(Integer equipmentValue) {
        this.equipmentValue = equipmentValue;
    }

    public Integer getConstructionInProgressValue() {
        return constructionInProgressValue;
    }

    public void setConstructionInProgressValue(Integer constructionInProgressValue) {
        this.constructionInProgressValue = constructionInProgressValue;
    }

    public Integer getFixedAssets() {
        return fixedAssets;
    }

    public void setFixedAssets(Integer fixedAssets) {
        this.fixedAssets = fixedAssets;
    }

    public Integer getAssetsTotal() {
        return assetsTotal;
    }

    public void setAssetsTotal(Integer assetsTotal) {
        this.assetsTotal = assetsTotal;
    }

    public Integer getLongTermDebt() {
        return longTermDebt;
    }

    public void setLongTermDebt(Integer longTermDebt) {
        this.longTermDebt = longTermDebt;
    }

    public Integer getShortTermDebt() {
        return shortTermDebt;
    }

    public void setShortTermDebt(Integer shortTermDebt) {
        this.shortTermDebt = shortTermDebt;
    }

    public Integer getDuesTotal() {
        return duesTotal;
    }

    public void setDuesTotal(Integer duesTotal) {
        this.duesTotal = duesTotal;
    }

    public Integer getDebtTotal() {
        return debtTotal;
    }

    public void setDebtTotal(Integer debtTotal) {
        this.debtTotal = debtTotal;
    }

    public Integer getEquityCapital() {
        return equityCapital;
    }

    public void setEquityCapital(Integer equityCapital) {
        this.equityCapital = equityCapital;
    }

    public Integer getProfitRetention() {
        return profitRetention;
    }

    public void setProfitRetention(Integer profitRetention) {
        this.profitRetention = profitRetention;
    }

    public Integer getOwnersEquity() {
        return ownersEquity;
    }

    public void setOwnersEquity(Integer ownersEquity) {
        this.ownersEquity = ownersEquity;
    }

    public Integer getDebtTotalAndOwnersEquity() {
        return debtTotalAndOwnersEquity;
    }

    public void setDebtTotalAndOwnersEquity(Integer debtTotalAndOwnersEquity) {
        this.debtTotalAndOwnersEquity = debtTotalAndOwnersEquity;
    }
}
