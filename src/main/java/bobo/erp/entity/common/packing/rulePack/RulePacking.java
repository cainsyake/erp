package bobo.erp.entity.common.packing.rulePack;

import bobo.erp.entity.rule.Rule;

/**
 * Created by bobo on 2017/12/4.
 * 包装类：内含Rule及String对象
 * 提供给AddRule Controller使用
 */
public class RulePacking {
    private Rule rule;
    private String username;

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
