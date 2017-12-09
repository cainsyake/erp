package bobo.erp.entity.common.packing.rulePack;

import bobo.erp.entity.rule.RuleProductBom;

/**
 * Created by bobo on 2017/12/5.
 */
public class RuleProductBomPacking {
    private String username;
    private String ruleId;
    private RuleProductBom[] productBoms;

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

    public RuleProductBom[] getProductBoms() {
        return productBoms;
    }

    public void setProductBoms(RuleProductBom[] productBoms) {
        this.productBoms = productBoms;
    }
}
