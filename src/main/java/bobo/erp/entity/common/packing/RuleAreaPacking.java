package bobo.erp.entity.common.packing;

import bobo.erp.entity.rule.RuleArea;

/**
 * Created by bobo on 2017/12/5.
 */
public class RuleAreaPacking {
    private String username;
    private String ruleId;
    private RuleArea[] areas;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public RuleArea[] getAreas() {
        return areas;
    }

    public void setAreas(RuleArea[] areas) {
        this.areas = areas;
    }
}
