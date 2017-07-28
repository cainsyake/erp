package bobo.erp.domain.rule;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/20.
 */
@Entity
public class RuleProduct {
    public RuleProduct() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ruleProductId;

    @OneToOne(mappedBy = "ruleProduct")
    private Rule rule;

    private String product1Name;        //产品名
    private Integer product1ProcCost;    //加工费
    private Integer product1DevInvest;   //单位周期开发费用
    private Integer product1DevTime;    //开发周期
    private Integer product1FinalCost;   //直接成本，含加工费及原料成本
    private Integer product1Score;      //潜力分数

    private String product2Name;
    private Integer product2ProcCost;
    private Integer product2DevInvest;
    private Integer product2DevTime;
    private Integer product2FinalCost;
    private Integer product2Score;

    private String product3Name;
    private Integer product3ProcCost;
    private Integer product3DevInvest;
    private Integer product3DevTime;
    private Integer product3FinalCost;
    private Integer product3Score;

    private String product4Name;
    private Integer product4ProcCost;
    private Integer product4DevInvest;
    private Integer product4DevTime;
    private Integer product4FinalCost;
    private Integer product4Score;

    private String product5Name;
    private Integer product5ProcCost;
    private Integer product5DevInvest;
    private Integer product5DevTime;
    private Integer product5FinalCost;
    private Integer product5Score;

    public Integer getRuleProductId() {
        return ruleProductId;
    }

    public void setRuleProductId(Integer ruleProductId) {
        this.ruleProductId = ruleProductId;
    }

    @JsonBackReference
    public Rule getRule() {
        return rule;
    }

    @JsonBackReference
    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getProduct1Name() {
        return product1Name;
    }

    public void setProduct1Name(String product1Name) {
        this.product1Name = product1Name;
    }

    public Integer getProduct1ProcCost() {
        return product1ProcCost;
    }

    public void setProduct1ProcCost(Integer product1ProcCost) {
        this.product1ProcCost = product1ProcCost;
    }

    public Integer getProduct1DevInvest() {
        return product1DevInvest;
    }

    public void setProduct1DevInvest(Integer product1DevInvest) {
        this.product1DevInvest = product1DevInvest;
    }

    public Integer getProduct1DevTime() {
        return product1DevTime;
    }

    public void setProduct1DevTime(Integer product1DevTime) {
        this.product1DevTime = product1DevTime;
    }

    public Integer getProduct1FinalCost() {
        return product1FinalCost;
    }

    public void setProduct1FinalCost(Integer product1FinalCost) {
        this.product1FinalCost = product1FinalCost;
    }

    public Integer getProduct1Score() {
        return product1Score;
    }

    public void setProduct1Score(Integer product1Score) {
        this.product1Score = product1Score;
    }

    public String getProduct2Name() {
        return product2Name;
    }

    public void setProduct2Name(String product2Name) {
        this.product2Name = product2Name;
    }

    public Integer getProduct2ProcCost() {
        return product2ProcCost;
    }

    public void setProduct2ProcCost(Integer product2ProcCost) {
        this.product2ProcCost = product2ProcCost;
    }

    public Integer getProduct2DevInvest() {
        return product2DevInvest;
    }

    public void setProduct2DevInvest(Integer product2DevInvest) {
        this.product2DevInvest = product2DevInvest;
    }

    public Integer getProduct2DevTime() {
        return product2DevTime;
    }

    public void setProduct2DevTime(Integer product2DevTime) {
        this.product2DevTime = product2DevTime;
    }

    public Integer getProduct2FinalCost() {
        return product2FinalCost;
    }

    public void setProduct2FinalCost(Integer product2FinalCost) {
        this.product2FinalCost = product2FinalCost;
    }

    public Integer getProduct2Score() {
        return product2Score;
    }

    public void setProduct2Score(Integer product2Score) {
        this.product2Score = product2Score;
    }

    public String getProduct3Name() {
        return product3Name;
    }

    public void setProduct3Name(String product3Name) {
        this.product3Name = product3Name;
    }

    public Integer getProduct3ProcCost() {
        return product3ProcCost;
    }

    public void setProduct3ProcCost(Integer product3ProcCost) {
        this.product3ProcCost = product3ProcCost;
    }

    public Integer getProduct3DevInvest() {
        return product3DevInvest;
    }

    public void setProduct3DevInvest(Integer product3DevInvest) {
        this.product3DevInvest = product3DevInvest;
    }

    public Integer getProduct3DevTime() {
        return product3DevTime;
    }

    public void setProduct3DevTime(Integer product3DevTime) {
        this.product3DevTime = product3DevTime;
    }

    public Integer getProduct3FinalCost() {
        return product3FinalCost;
    }

    public void setProduct3FinalCost(Integer product3FinalCost) {
        this.product3FinalCost = product3FinalCost;
    }

    public Integer getProduct3Score() {
        return product3Score;
    }

    public void setProduct3Score(Integer product3Score) {
        this.product3Score = product3Score;
    }

    public String getProduct4Name() {
        return product4Name;
    }

    public void setProduct4Name(String product4Name) {
        this.product4Name = product4Name;
    }

    public Integer getProduct4ProcCost() {
        return product4ProcCost;
    }

    public void setProduct4ProcCost(Integer product4ProcCost) {
        this.product4ProcCost = product4ProcCost;
    }

    public Integer getProduct4DevInvest() {
        return product4DevInvest;
    }

    public void setProduct4DevInvest(Integer product4DevInvest) {
        this.product4DevInvest = product4DevInvest;
    }

    public Integer getProduct4DevTime() {
        return product4DevTime;
    }

    public void setProduct4DevTime(Integer product4DevTime) {
        this.product4DevTime = product4DevTime;
    }

    public Integer getProduct4FinalCost() {
        return product4FinalCost;
    }

    public void setProduct4FinalCost(Integer product4FinalCost) {
        this.product4FinalCost = product4FinalCost;
    }

    public Integer getProduct4Score() {
        return product4Score;
    }

    public void setProduct4Score(Integer product4Score) {
        this.product4Score = product4Score;
    }

    public String getProduct5Name() {
        return product5Name;
    }

    public void setProduct5Name(String product5Name) {
        this.product5Name = product5Name;
    }

    public Integer getProduct5ProcCost() {
        return product5ProcCost;
    }

    public void setProduct5ProcCost(Integer product5ProcCost) {
        this.product5ProcCost = product5ProcCost;
    }

    public Integer getProduct5DevInvest() {
        return product5DevInvest;
    }

    public void setProduct5DevInvest(Integer product5DevInvest) {
        this.product5DevInvest = product5DevInvest;
    }

    public Integer getProduct5DevTime() {
        return product5DevTime;
    }

    public void setProduct5DevTime(Integer product5DevTime) {
        this.product5DevTime = product5DevTime;
    }

    public Integer getProduct5FinalCost() {
        return product5FinalCost;
    }

    public void setProduct5FinalCost(Integer product5FinalCost) {
        this.product5FinalCost = product5FinalCost;
    }

    public Integer getProduct5Score() {
        return product5Score;
    }

    public void setProduct5Score(Integer product5Score) {
        this.product5Score = product5Score;
    }
}
