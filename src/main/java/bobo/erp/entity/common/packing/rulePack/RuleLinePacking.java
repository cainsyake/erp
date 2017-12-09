package bobo.erp.entity.common.packing.rulePack;

import bobo.erp.entity.rule.RuleLine;

/**
 * Created by bobo on 2017/12/5.
 */
public class RuleLinePacking {
    private String username;
    private RuleLine[] lines;
    private String ruleId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RuleLine[] getLines() {
        return lines;
    }

    public void setLines(RuleLine[] lines) {
        this.lines = lines;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
}
