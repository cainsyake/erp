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
    private List<RuleArea> ruleAreaList;    //区域规则

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_id")
    private List<RuleQualification> ruleQualificationList;      //资质规则

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

    private Integer lineQuantity;
    private Integer factoryQuantity;
    private Integer areaQuantity;
    private Integer qualificationQuantity;
    private Integer materialQuantity;
    private Integer productQuantity;


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

    public List<RuleArea> getRuleAreaList() {
        return ruleAreaList;
    }

    public void setRuleAreaList(List<RuleArea> ruleAreaList) {
        this.ruleAreaList = ruleAreaList;
    }

    public List<RuleQualification> getRuleQualificationList() {
        return ruleQualificationList;
    }

    public void setRuleQualificationList(List<RuleQualification> ruleQualificationList) {
        this.ruleQualificationList = ruleQualificationList;
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

    public Integer getLineQuantity() {
        return lineQuantity;
    }

    public void setLineQuantity(Integer lineQuantity) {
        this.lineQuantity = lineQuantity;
    }

    public Integer getFactoryQuantity() {
        return factoryQuantity;
    }

    public void setFactoryQuantity(Integer factoryQuantity) {
        this.factoryQuantity = factoryQuantity;
    }

    public Integer getAreaQuantity() {
        return areaQuantity;
    }

    public void setAreaQuantity(Integer areaQuantity) {
        this.areaQuantity = areaQuantity;
    }

    public Integer getQualificationQuantity() {
        return qualificationQuantity;
    }

    public void setQualificationQuantity(Integer qualificationQuantity) {
        this.qualificationQuantity = qualificationQuantity;
    }

    public Integer getMaterialQuantity() {
        return materialQuantity;
    }

    public void setMaterialQuantity(Integer materialQuantity) {
        this.materialQuantity = materialQuantity;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }
}
