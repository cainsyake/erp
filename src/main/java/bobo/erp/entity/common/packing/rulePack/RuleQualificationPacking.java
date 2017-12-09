package bobo.erp.entity.common.packing.rulePack;

import bobo.erp.entity.rule.RuleQualification;

/**
 * Created by bobo on 2017/12/5.
 */
public class RuleQualificationPacking {
    private String username;
    private RuleQualification[] qualifications;
    private String ruleId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RuleQualification[] getQualifications() {
        return qualifications;
    }

    public void setQualifications(RuleQualification[] qualifications) {
        this.qualifications = qualifications;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
}
