package bobo.erp.entity.rule;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 59814 on 2017/7/20.
 */
@Entity
public class Rule {
    public Rule() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ruleId;

    private String ruleUploader;
    private Date ruleAlterTime;
    private Integer ruleUserCount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_factory_id")
    private RuleFactory ruleFactory;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_line_id")
    private RuleLine ruleLine;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_market_id")
    private RuleMarket ruleMarket;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_iso_id")
    private RuleIso ruleIso;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_material_id")
    private RuleMaterial ruleMaterial;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_product_id")
    private RuleProduct ruleProduct;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_product_mix_id")
    private RuleProductMix ruleProductMix;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_param_id")
    private RuleParam ruleParam;

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
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


    public RuleFactory getRuleFactory() {
        return ruleFactory;
    }

    public void setRuleFactory(RuleFactory ruleFactory) {
        this.ruleFactory = ruleFactory;
    }

    public RuleLine getRuleLine() {
        return ruleLine;
    }

    public void setRuleLine(RuleLine ruleLine) {
        this.ruleLine = ruleLine;
    }

    public RuleMarket getRuleMarket() {
        return ruleMarket;
    }

    public void setRuleMarket(RuleMarket ruleMarket) {
        this.ruleMarket = ruleMarket;
    }

    public RuleIso getRuleIso() {
        return ruleIso;
    }

    public void setRuleIso(RuleIso ruleIso) {
        this.ruleIso = ruleIso;
    }

    public RuleMaterial getRuleMaterial() {
        return ruleMaterial;
    }

    public void setRuleMaterial(RuleMaterial ruleMaterial) {
        this.ruleMaterial = ruleMaterial;
    }

    public RuleProduct getRuleProduct() {
        return ruleProduct;
    }

    public void setRuleProduct(RuleProduct ruleProduct) {
        this.ruleProduct = ruleProduct;
    }

    public RuleProductMix getRuleProductMix() {
        return ruleProductMix;
    }

    public void setRuleProductMix(RuleProductMix ruleProductMix) {
        this.ruleProductMix = ruleProductMix;
    }

    public RuleParam getRuleParam() {
        return ruleParam;
    }

    public void setRuleParam(RuleParam ruleParam) {
        this.ruleParam = ruleParam;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "ruleId=" + ruleId +
                ", ruleUploader='" + ruleUploader + '\'' +
                ", ruleAlterTime=" + ruleAlterTime +
                ", ruleUserCount=" + ruleUserCount +
                ", ruleFactory=" + ruleFactory +
                ", ruleLine=" + ruleLine +
                ", ruleMarket=" + ruleMarket +
                ", ruleIso=" + ruleIso +
                ", ruleMaterial=" + ruleMaterial +
                ", ruleProduct=" + ruleProduct +
                ", ruleProductMix=" + ruleProductMix +
                ", ruleParam=" + ruleParam +
                '}';
    }
}
