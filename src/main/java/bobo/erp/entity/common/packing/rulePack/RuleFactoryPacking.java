package bobo.erp.entity.common.packing.rulePack;

import bobo.erp.entity.rule.RuleFactory;

/**
 * Created by bobo on 2017/12/5.
 */
public class RuleFactoryPacking {
    private String username;
    private RuleFactory[] factories;
    private String ruleId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RuleFactory[] getFactories() {
        return factories;
    }

    public void setFactories(RuleFactory[] factories) {
        this.factories = factories;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
}
