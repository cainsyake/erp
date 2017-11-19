package bobo.erp.entity.rule;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by 59814 on 2017/7/20.
 */
@Entity
public class Rule {
    public Rule() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String ruleUploader;
    private Date ruleAlterTime;
    private Integer ruleUserCount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_id")
    private List<RuleLine> ruleLineList;    //生产线规则

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_id")
    private List<RuleFactory> ruleFactoryList;  //厂房规则

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_id")
    private List<RuleMarket> ruleMarketList;    //区域规则

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_id")
    private List<RuleIso> ruleIsoList;      //资质规则

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_id")
    private List<RuleMaterial> ruleMaterialList;    //原料规则

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_id")
    private List<RuleProductBom> ruleProductBomList;    //产品结构规则

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_id")
    private List<RuleProduct> ruleProductList;    //产品规则

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_param_id")
    private RuleParam ruleParam;    //详细参数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuleUploader() {
        return ruleUploader;
    }

    public void setRuleUploader(String ruleUploader) {
        this.ruleUploader = ruleUploader;
    }

    public Date getRuleAlterTime() {
        return ruleAlterTime;
    }

    public void setRuleAlterTime(Date ruleAlterTime) {
        this.ruleAlterTime = ruleAlterTime;
    }

    public Integer getRuleUserCount() {
        return ruleUserCount;
    }

    public void setRuleUserCount(Integer ruleUserCount) {
        this.ruleUserCount = ruleUserCount;
    }

    public List<RuleLine> getRuleLineList() {
        return ruleLineList;
    }

    public void setRuleLineList(List<RuleLine> ruleLineList) {
        this.ruleLineList = ruleLineList;
    }

    public List<RuleFactory> getRuleFactoryList() {
        return ruleFactoryList;
    }

    public void setRuleFactoryList(List<RuleFactory> ruleFactoryList) {
        this.ruleFactoryList = ruleFactoryList;
    }

    public List<RuleMarket> getRuleMarketList() {
        return ruleMarketList;
    }

    public void setRuleMarketList(List<RuleMarket> ruleMarketList) {
        this.ruleMarketList = ruleMarketList;
    }

    public List<RuleIso> getRuleIsoList() {
        return ruleIsoList;
    }

    public void setRuleIsoList(List<RuleIso> ruleIsoList) {
        this.ruleIsoList = ruleIsoList;
    }

    public List<RuleMaterial> getRuleMaterialList() {
        return ruleMaterialList;
    }

    public void setRuleMaterialList(List<RuleMaterial> ruleMaterialList) {
        this.ruleMaterialList = ruleMaterialList;
    }

    public List<RuleProductBom> getRuleProductBomList() {
        return ruleProductBomList;
    }

    public void setRuleProductBomList(List<RuleProductBom> ruleProductBomList) {
        this.ruleProductBomList = ruleProductBomList;
    }

    public List<RuleProduct> getRuleProductList() {
        return ruleProductList;
    }

    public void setRuleProductList(List<RuleProduct> ruleProductList) {
        this.ruleProductList = ruleProductList;
    }

    public RuleParam getRuleParam() {
        return ruleParam;
    }

    public void setRuleParam(RuleParam ruleParam) {
        this.ruleParam = ruleParam;
    }
}
