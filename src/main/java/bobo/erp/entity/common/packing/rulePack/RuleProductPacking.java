package bobo.erp.entity.common.packing.rulePack;

import bobo.erp.entity.rule.RuleProduct;

/**
 * Created by bobo on 2017/12/5.
 */
public class RuleProductPacking {
    private String username;
    private String ruleId;
    private RuleProduct[] products;

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

    public RuleProduct[] getProducts() {
        return products;
    }

    public void setProducts(RuleProduct[] products) {
        this.products = products;
    }
}
