package bobo.erp.entity.common.packing;

import bobo.erp.entity.rule.RuleFactory;

import java.util.List;

/**
 * Created by bobo on 2017/12/5.
 */
public class RuleFactoryPacking {
    private String username;
    private RuleFactory[] factories;

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
}
