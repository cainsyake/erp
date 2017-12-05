package bobo.erp.entity.common.packing;

import bobo.erp.entity.rule.RuleMaterial;

/**
 * Created by bobo on 2017/12/5.
 */
public class RuleMaterialPacking {
    private String username;
    private String ruleId;
    private RuleMaterial[] materials;

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

    public RuleMaterial[] getMaterials() {
        return materials;
    }

    public void setMaterials(RuleMaterial[] materials) {
        this.materials = materials;
    }
}
