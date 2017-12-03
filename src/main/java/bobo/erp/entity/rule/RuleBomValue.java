package bobo.erp.entity.rule;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by bobo on 2017/12/3.
 */
@Entity
public class RuleBomValue {
    public RuleBomValue() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer value;

    @ManyToOne
    private RuleProductBom ruleProductBom;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @JsonBackReference
    public RuleProductBom getRuleProductBom() {
        return ruleProductBom;
    }

    public void setRuleProductBom(RuleProductBom ruleProductBom) {
        this.ruleProductBom = ruleProductBom;
    }
}
