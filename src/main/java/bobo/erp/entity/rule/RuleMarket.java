package bobo.erp.entity.rule;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/20.
 */
@Entity
public class RuleMarket {
    public RuleMarket() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ruleMarketId;

    @OneToOne(mappedBy = "ruleMarket")
    private Rule rule;

    private String market1Name;         //市场名
    private Integer market1UnitInvest;   //单位开发投资
    private Integer market1DevTime;     //开发周期
    private Integer market1Score;       //潜力分数

    private String market2Name;
    private Integer market2UnitInvest;
    private Integer market2DevTime;
    private Integer market2Score;

    private String market3Name;
    private Integer market3UnitInvest;
    private Integer market3DevTime;
    private Integer market3Score;

    private String market4Name;
    private Integer market4UnitInvest;
    private Integer market4DevTime;
    private Integer market4Score;

    private String market5Name;
    private Integer market5UnitInvest;
    private Integer market5DevTime;
    private Integer market5Score;

    public Integer getRuleMarketId() {
        return ruleMarketId;
    }

    public void setRuleMarketId(Integer ruleMarketId) {
        this.ruleMarketId = ruleMarketId;
    }

    @JsonBackReference
    public Rule getRule() {
        return rule;
    }

    @JsonBackReference
    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getMarket1Name() {
        return market1Name;
    }

    public void setMarket1Name(String market1Name) {
        this.market1Name = market1Name;
    }

    public Integer getMarket1UnitInvest() {
        return market1UnitInvest;
    }

    public void setMarket1UnitInvest(Integer market1UnitInvest) {
        this.market1UnitInvest = market1UnitInvest;
    }

    public Integer getMarket1DevTime() {
        return market1DevTime;
    }

    public void setMarket1DevTime(Integer market1DevTime) {
        this.market1DevTime = market1DevTime;
    }

    public Integer getMarket1Score() {
        return market1Score;
    }

    public void setMarket1Score(Integer market1Score) {
        this.market1Score = market1Score;
    }

    public String getMarket2Name() {
        return market2Name;
    }

    public void setMarket2Name(String market2Name) {
        this.market2Name = market2Name;
    }

    public Integer getMarket2UnitInvest() {
        return market2UnitInvest;
    }

    public void setMarket2UnitInvest(Integer market2UnitInvest) {
        this.market2UnitInvest = market2UnitInvest;
    }

    public Integer getMarket2DevTime() {
        return market2DevTime;
    }

    public void setMarket2DevTime(Integer market2DevTime) {
        this.market2DevTime = market2DevTime;
    }

    public Integer getMarket2Score() {
        return market2Score;
    }

    public void setMarket2Score(Integer market2Score) {
        this.market2Score = market2Score;
    }

    public String getMarket3Name() {
        return market3Name;
    }

    public void setMarket3Name(String market3Name) {
        this.market3Name = market3Name;
    }

    public Integer getMarket3UnitInvest() {
        return market3UnitInvest;
    }

    public void setMarket3UnitInvest(Integer market3UnitInvest) {
        this.market3UnitInvest = market3UnitInvest;
    }

    public Integer getMarket3DevTime() {
        return market3DevTime;
    }

    public void setMarket3DevTime(Integer market3DevTime) {
        this.market3DevTime = market3DevTime;
    }

    public Integer getMarket3Score() {
        return market3Score;
    }

    public void setMarket3Score(Integer market3Score) {
        this.market3Score = market3Score;
    }

    public String getMarket4Name() {
        return market4Name;
    }

    public void setMarket4Name(String market4Name) {
        this.market4Name = market4Name;
    }

    public Integer getMarket4UnitInvest() {
        return market4UnitInvest;
    }

    public void setMarket4UnitInvest(Integer market4UnitInvest) {
        this.market4UnitInvest = market4UnitInvest;
    }

    public Integer getMarket4DevTime() {
        return market4DevTime;
    }

    public void setMarket4DevTime(Integer market4DevTime) {
        this.market4DevTime = market4DevTime;
    }

    public Integer getMarket4Score() {
        return market4Score;
    }

    public void setMarket4Score(Integer market4Score) {
        this.market4Score = market4Score;
    }

    public String getMarket5Name() {
        return market5Name;
    }

    public void setMarket5Name(String market5Name) {
        this.market5Name = market5Name;
    }

    public Integer getMarket5UnitInvest() {
        return market5UnitInvest;
    }

    public void setMarket5UnitInvest(Integer market5UnitInvest) {
        this.market5UnitInvest = market5UnitInvest;
    }

    public Integer getMarket5DevTime() {
        return market5DevTime;
    }

    public void setMarket5DevTime(Integer market5DevTime) {
        this.market5DevTime = market5DevTime;
    }

    public Integer getMarket5Score() {
        return market5Score;
    }

    public void setMarket5Score(Integer market5Score) {
        this.market5Score = market5Score;
    }
}
