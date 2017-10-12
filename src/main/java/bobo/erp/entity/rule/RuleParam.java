package bobo.erp.entity.rule;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/20.
 */
@Entity
public class RuleParam {
    public RuleParam() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ruleParamId;

    @OneToOne(mappedBy = "ruleParam")
    private Rule rule;

    private Double paramPenatly;                //违约金
    private Double paramLoanRatio;              //贷款额倍数
    private Double paramProductSaleRatio;       //产品紧急出售折价率
    private Double paramMaterailSaleRatio;      //原料紧急出售折价率
    private Double paramLongTermLoanRates;      //长贷利率
    private Double paramShortTermLoanRates;     //短贷利率
    private Double paramShortTermDiscountRates; //1、2期贴现利率
    private Double paramLongTermDiscountRates;  //3、4期贴现利率
    private Integer paramInitialCash;            //初始现金
    private Integer paramManagementCost;         //管理费
    private Integer paramInfomationCost;         //信息费
    private Double paramTaxRate;                //所得税率
    private Integer paramLongTermLoanTimeLimit;  //最大长贷年份
    private Integer paramAdvertisingMinFee;      //最小广告额
    private Double paramProductBuyRation;       //产品紧急采购溢价率
    private Double paramMaterailBuyRation;      //原料紧急采购溢价率
    private Integer paramSelectOrderTime;        //选单时间
    private Integer paramFirstSelectOrderTime;   //首位选单补时
    private Integer paramMarketSametimeOpenNum;  //市场同开数量
    private Integer paramHaveMarketLeader;       //市场老大有无
    private Integer paramBidTime;                //竞单时间
    private Integer paramBidSametimeNum;         //竞单同竞数
    private Integer paramFactoryMaxNum;          //最大厂房数量
    private Integer paramDiscountMode;           //贴现方式
    private Integer paramAllowUserReturnSeason;  //允许用户还原本季
    private Integer paramAllowUserReturnYear;    //允许用户还原本年

    public Integer getRuleParamId() {
        return ruleParamId;
    }

    public void setRuleParamId(Integer ruleParamId) {
        this.ruleParamId = ruleParamId;
    }

    @JsonBackReference
    public Rule getRule() {
        return rule;
    }

    @JsonBackReference
    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Double getParamPenatly() {
        return paramPenatly;
    }

    public void setParamPenatly(Double paramPenatly) {
        this.paramPenatly = paramPenatly;
    }

    public Double getParamLoanRatio() {
        return paramLoanRatio;
    }

    public void setParamLoanRatio(Double paramLoanRatio) {
        this.paramLoanRatio = paramLoanRatio;
    }

    public Double getParamProductSaleRatio() {
        return paramProductSaleRatio;
    }

    public void setParamProductSaleRatio(Double paramProductSaleRatio) {
        this.paramProductSaleRatio = paramProductSaleRatio;
    }

    public Double getParamMaterailSaleRatio() {
        return paramMaterailSaleRatio;
    }

    public void setParamMaterailSaleRatio(Double paramMaterailSaleRatio) {
        this.paramMaterailSaleRatio = paramMaterailSaleRatio;
    }

    public Double getParamLongTermLoanRates() {
        return paramLongTermLoanRates;
    }

    public void setParamLongTermLoanRates(Double paramLongTermLoanRates) {
        this.paramLongTermLoanRates = paramLongTermLoanRates;
    }

    public Double getParamShortTermLoanRates() {
        return paramShortTermLoanRates;
    }

    public void setParamShortTermLoanRates(Double paramShortTermLoanRates) {
        this.paramShortTermLoanRates = paramShortTermLoanRates;
    }

    public Double getParamShortTermDiscountRates() {
        return paramShortTermDiscountRates;
    }

    public void setParamShortTermDiscountRates(Double paramShortTermDiscountRates) {
        this.paramShortTermDiscountRates = paramShortTermDiscountRates;
    }

    public Double getParamLongTermDiscountRates() {
        return paramLongTermDiscountRates;
    }

    public void setParamLongTermDiscountRates(Double paramLongTermDiscountRates) {
        this.paramLongTermDiscountRates = paramLongTermDiscountRates;
    }

    public Integer getParamInitialCash() {
        return paramInitialCash;
    }

    public void setParamInitialCash(Integer paramInitialCash) {
        this.paramInitialCash = paramInitialCash;
    }

    public Integer getParamManagementCost() {
        return paramManagementCost;
    }

    public void setParamManagementCost(Integer paramManagementCost) {
        this.paramManagementCost = paramManagementCost;
    }

    public Integer getParamInfomationCost() {
        return paramInfomationCost;
    }

    public void setParamInfomationCost(Integer paramInfomationCost) {
        this.paramInfomationCost = paramInfomationCost;
    }

    public Double getParamTaxRate() {
        return paramTaxRate;
    }

    public void setParamTaxRate(Double paramTaxRate) {
        this.paramTaxRate = paramTaxRate;
    }

    public Integer getParamLongTermLoanTimeLimit() {
        return paramLongTermLoanTimeLimit;
    }

    public void setParamLongTermLoanTimeLimit(Integer paramLongTermLoanTimeLimit) {
        this.paramLongTermLoanTimeLimit = paramLongTermLoanTimeLimit;
    }

    public Integer getParamAdvertisingMinFee() {
        return paramAdvertisingMinFee;
    }

    public void setParamAdvertisingMinFee(Integer paramAdvertisingMinFee) {
        this.paramAdvertisingMinFee = paramAdvertisingMinFee;
    }

    public Double getParamProductBuyRation() {
        return paramProductBuyRation;
    }

    public void setParamProductBuyRation(Double paramProductBuyRation) {
        this.paramProductBuyRation = paramProductBuyRation;
    }

    public Double getParamMaterailBuyRation() {
        return paramMaterailBuyRation;
    }

    public void setParamMaterailBuyRation(Double paramMaterailBuyRation) {
        this.paramMaterailBuyRation = paramMaterailBuyRation;
    }

    public Integer getParamSelectOrderTime() {
        return paramSelectOrderTime;
    }

    public void setParamSelectOrderTime(Integer paramSelectOrderTime) {
        this.paramSelectOrderTime = paramSelectOrderTime;
    }

    public Integer getParamFirstSelectOrderTime() {
        return paramFirstSelectOrderTime;
    }

    public void setParamFirstSelectOrderTime(Integer paramFirstSelectOrderTime) {
        this.paramFirstSelectOrderTime = paramFirstSelectOrderTime;
    }

    public Integer getParamMarketSametimeOpenNum() {
        return paramMarketSametimeOpenNum;
    }

    public void setParamMarketSametimeOpenNum(Integer paramMarketSametimeOpenNum) {
        this.paramMarketSametimeOpenNum = paramMarketSametimeOpenNum;
    }

    public Integer getParamHaveMarketLeader() {
        return paramHaveMarketLeader;
    }

    public void setParamHaveMarketLeader(Integer paramHaveMarketLeader) {
        this.paramHaveMarketLeader = paramHaveMarketLeader;
    }

    public Integer getParamBidTime() {
        return paramBidTime;
    }

    public void setParamBidTime(Integer paramBidTime) {
        this.paramBidTime = paramBidTime;
    }

    public Integer getParamBidSametimeNum() {
        return paramBidSametimeNum;
    }

    public void setParamBidSametimeNum(Integer paramBidSametimeNum) {
        this.paramBidSametimeNum = paramBidSametimeNum;
    }

    public Integer getParamFactoryMaxNum() {
        return paramFactoryMaxNum;
    }

    public void setParamFactoryMaxNum(Integer paramFactoryMaxNum) {
        this.paramFactoryMaxNum = paramFactoryMaxNum;
    }

    public Integer getParamDiscountMode() {
        return paramDiscountMode;
    }

    public void setParamDiscountMode(Integer paramDiscountMode) {
        this.paramDiscountMode = paramDiscountMode;
    }

    public Integer getParamAllowUserReturnSeason() {
        return paramAllowUserReturnSeason;
    }

    public void setParamAllowUserReturnSeason(Integer paramAllowUserReturnSeason) {
        this.paramAllowUserReturnSeason = paramAllowUserReturnSeason;
    }

    public Integer getParamAllowUserReturnYear() {
        return paramAllowUserReturnYear;
    }

    public void setParamAllowUserReturnYear(Integer paramAllowUserReturnYear) {
        this.paramAllowUserReturnYear = paramAllowUserReturnYear;
    }
}
