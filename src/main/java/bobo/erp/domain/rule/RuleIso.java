package bobo.erp.domain.rule;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/20.
 */
@Entity
public class RuleIso {
    public RuleIso() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ruleIsoId;

    @OneToOne(mappedBy = "ruleIso")
    private Rule rule;

    private String iso1Name;        //ISO名
    private Integer iso1UnitInvest;  //单位周期开发投资
    private Integer iso1DevTime;    //开发周期
    private Integer iso1Score;      //潜力分数

    private String iso2Name;
    private Integer iso2UnitInvest;
    private Integer iso2DevTime;
    private Integer iso2Score;

    public Integer getRuleIsoId() {
        return ruleIsoId;
    }

    public void setRuleIsoId(Integer ruleIsoId) {
        this.ruleIsoId = ruleIsoId;
    }

    @JsonBackReference
    public Rule getRule() {
        return rule;
    }

    @JsonBackReference
    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getIso1Name() {
        return iso1Name;
    }

    public void setIso1Name(String iso1Name) {
        this.iso1Name = iso1Name;
    }

    public Integer getIso1UnitInvest() {
        return iso1UnitInvest;
    }

    public void setIso1UnitInvest(Integer iso1UnitInvest) {
        this.iso1UnitInvest = iso1UnitInvest;
    }

    public Integer getIso1DevTime() {
        return iso1DevTime;
    }

    public void setIso1DevTime(Integer iso1DevTime) {
        this.iso1DevTime = iso1DevTime;
    }

    public Integer getIso1Score() {
        return iso1Score;
    }

    public void setIso1Score(Integer iso1Score) {
        this.iso1Score = iso1Score;
    }

    public String getIso2Name() {
        return iso2Name;
    }

    public void setIso2Name(String iso2Name) {
        this.iso2Name = iso2Name;
    }

    public Integer getIso2UnitInvest() {
        return iso2UnitInvest;
    }

    public void setIso2UnitInvest(Integer iso2UnitInvest) {
        this.iso2UnitInvest = iso2UnitInvest;
    }

    public Integer getIso2DevTime() {
        return iso2DevTime;
    }

    public void setIso2DevTime(Integer iso2DevTime) {
        this.iso2DevTime = iso2DevTime;
    }

    public Integer getIso2Score() {
        return iso2Score;
    }

    public void setIso2Score(Integer iso2Score) {
        this.iso2Score = iso2Score;
    }
}
