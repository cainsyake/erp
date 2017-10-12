package bobo.erp.entity.rule;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/20.
 * product x 代表 x产品
 * 最后两位Ry或Py 代表y原料或y半成品原料
 */
@Entity
public class RuleProductMix {
    public RuleProductMix() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ruleProductMixId;

    @OneToOne(mappedBy = "ruleProductMix")
    private Rule rule;

    private Integer product1MixR1;
    private Integer product1MixR2;
    private Integer product1MixR3;
    private Integer product1MixR4;
    private Integer product1MixR5;
    private Integer product1MixP1;
    private Integer product1MixP2;
    private Integer product1MixP3;
    private Integer product1MixP4;
    private Integer product1MixP5;

    private Integer product2MixR1;
    private Integer product2MixR2;
    private Integer product2MixR3;
    private Integer product2MixR4;
    private Integer product2MixR5;
    private Integer product2MixP1;
    private Integer product2MixP2;
    private Integer product2MixP3;
    private Integer product2MixP4;
    private Integer product2MixP5;

    private Integer product3MixR1;
    private Integer product3MixR2;
    private Integer product3MixR3;
    private Integer product3MixR4;
    private Integer product3MixR5;
    private Integer product3MixP1;
    private Integer product3MixP2;
    private Integer product3MixP3;
    private Integer product3MixP4;
    private Integer product3MixP5;

    private Integer product4MixR1;
    private Integer product4MixR2;
    private Integer product4MixR3;
    private Integer product4MixR4;
    private Integer product4MixR5;
    private Integer product4MixP1;
    private Integer product4MixP2;
    private Integer product4MixP3;
    private Integer product4MixP4;
    private Integer product4MixP5;

    private Integer product5MixR1;
    private Integer product5MixR2;
    private Integer product5MixR3;
    private Integer product5MixR4;
    private Integer product5MixR5;
    private Integer product5MixP1;
    private Integer product5MixP2;
    private Integer product5MixP3;
    private Integer product5MixP4;
    private Integer product5MixP5;

    public Integer getRuleProductMixId() {
        return ruleProductMixId;
    }

    public void setRuleProductMixId(Integer ruleProductMixId) {
        this.ruleProductMixId = ruleProductMixId;
    }

    @JsonBackReference
    public Rule getRule() {
        return rule;
    }

    @JsonBackReference
    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Integer getProduct1MixR1() {
        return product1MixR1;
    }

    public void setProduct1MixR1(Integer product1MixR1) {
        this.product1MixR1 = product1MixR1;
    }

    public Integer getProduct1MixR2() {
        return product1MixR2;
    }

    public void setProduct1MixR2(Integer product1MixR2) {
        this.product1MixR2 = product1MixR2;
    }

    public Integer getProduct1MixR3() {
        return product1MixR3;
    }

    public void setProduct1MixR3(Integer product1MixR3) {
        this.product1MixR3 = product1MixR3;
    }

    public Integer getProduct1MixR4() {
        return product1MixR4;
    }

    public void setProduct1MixR4(Integer product1MixR4) {
        this.product1MixR4 = product1MixR4;
    }

    public Integer getProduct1MixR5() {
        return product1MixR5;
    }

    public void setProduct1MixR5(Integer product1MixR5) {
        this.product1MixR5 = product1MixR5;
    }

    public Integer getProduct1MixP1() {
        return product1MixP1;
    }

    public void setProduct1MixP1(Integer product1MixP1) {
        this.product1MixP1 = product1MixP1;
    }

    public Integer getProduct1MixP2() {
        return product1MixP2;
    }

    public void setProduct1MixP2(Integer product1MixP2) {
        this.product1MixP2 = product1MixP2;
    }

    public Integer getProduct1MixP3() {
        return product1MixP3;
    }

    public void setProduct1MixP3(Integer product1MixP3) {
        this.product1MixP3 = product1MixP3;
    }

    public Integer getProduct1MixP4() {
        return product1MixP4;
    }

    public void setProduct1MixP4(Integer product1MixP4) {
        this.product1MixP4 = product1MixP4;
    }

    public Integer getProduct1MixP5() {
        return product1MixP5;
    }

    public void setProduct1MixP5(Integer product1MixP5) {
        this.product1MixP5 = product1MixP5;
    }

    public Integer getProduct2MixR1() {
        return product2MixR1;
    }

    public void setProduct2MixR1(Integer product2MixR1) {
        this.product2MixR1 = product2MixR1;
    }

    public Integer getProduct2MixR2() {
        return product2MixR2;
    }

    public void setProduct2MixR2(Integer product2MixR2) {
        this.product2MixR2 = product2MixR2;
    }

    public Integer getProduct2MixR3() {
        return product2MixR3;
    }

    public void setProduct2MixR3(Integer product2MixR3) {
        this.product2MixR3 = product2MixR3;
    }

    public Integer getProduct2MixR4() {
        return product2MixR4;
    }

    public void setProduct2MixR4(Integer product2MixR4) {
        this.product2MixR4 = product2MixR4;
    }

    public Integer getProduct2MixR5() {
        return product2MixR5;
    }

    public void setProduct2MixR5(Integer product2MixR5) {
        this.product2MixR5 = product2MixR5;
    }

    public Integer getProduct2MixP1() {
        return product2MixP1;
    }

    public void setProduct2MixP1(Integer product2MixP1) {
        this.product2MixP1 = product2MixP1;
    }

    public Integer getProduct2MixP2() {
        return product2MixP2;
    }

    public void setProduct2MixP2(Integer product2MixP2) {
        this.product2MixP2 = product2MixP2;
    }

    public Integer getProduct2MixP3() {
        return product2MixP3;
    }

    public void setProduct2MixP3(Integer product2MixP3) {
        this.product2MixP3 = product2MixP3;
    }

    public Integer getProduct2MixP4() {
        return product2MixP4;
    }

    public void setProduct2MixP4(Integer product2MixP4) {
        this.product2MixP4 = product2MixP4;
    }

    public Integer getProduct2MixP5() {
        return product2MixP5;
    }

    public void setProduct2MixP5(Integer product2MixP5) {
        this.product2MixP5 = product2MixP5;
    }

    public Integer getProduct3MixR1() {
        return product3MixR1;
    }

    public void setProduct3MixR1(Integer product3MixR1) {
        this.product3MixR1 = product3MixR1;
    }

    public Integer getProduct3MixR2() {
        return product3MixR2;
    }

    public void setProduct3MixR2(Integer product3MixR2) {
        this.product3MixR2 = product3MixR2;
    }

    public Integer getProduct3MixR3() {
        return product3MixR3;
    }

    public void setProduct3MixR3(Integer product3MixR3) {
        this.product3MixR3 = product3MixR3;
    }

    public Integer getProduct3MixR4() {
        return product3MixR4;
    }

    public void setProduct3MixR4(Integer product3MixR4) {
        this.product3MixR4 = product3MixR4;
    }

    public Integer getProduct3MixR5() {
        return product3MixR5;
    }

    public void setProduct3MixR5(Integer product3MixR5) {
        this.product3MixR5 = product3MixR5;
    }

    public Integer getProduct3MixP1() {
        return product3MixP1;
    }

    public void setProduct3MixP1(Integer product3MixP1) {
        this.product3MixP1 = product3MixP1;
    }

    public Integer getProduct3MixP2() {
        return product3MixP2;
    }

    public void setProduct3MixP2(Integer product3MixP2) {
        this.product3MixP2 = product3MixP2;
    }

    public Integer getProduct3MixP3() {
        return product3MixP3;
    }

    public void setProduct3MixP3(Integer product3MixP3) {
        this.product3MixP3 = product3MixP3;
    }

    public Integer getProduct3MixP4() {
        return product3MixP4;
    }

    public void setProduct3MixP4(Integer product3MixP4) {
        this.product3MixP4 = product3MixP4;
    }

    public Integer getProduct3MixP5() {
        return product3MixP5;
    }

    public void setProduct3MixP5(Integer product3MixP5) {
        this.product3MixP5 = product3MixP5;
    }

    public Integer getProduct4MixR1() {
        return product4MixR1;
    }

    public void setProduct4MixR1(Integer product4MixR1) {
        this.product4MixR1 = product4MixR1;
    }

    public Integer getProduct4MixR2() {
        return product4MixR2;
    }

    public void setProduct4MixR2(Integer product4MixR2) {
        this.product4MixR2 = product4MixR2;
    }

    public Integer getProduct4MixR3() {
        return product4MixR3;
    }

    public void setProduct4MixR3(Integer product4MixR3) {
        this.product4MixR3 = product4MixR3;
    }

    public Integer getProduct4MixR4() {
        return product4MixR4;
    }

    public void setProduct4MixR4(Integer product4MixR4) {
        this.product4MixR4 = product4MixR4;
    }

    public Integer getProduct4MixR5() {
        return product4MixR5;
    }

    public void setProduct4MixR5(Integer product4MixR5) {
        this.product4MixR5 = product4MixR5;
    }

    public Integer getProduct4MixP1() {
        return product4MixP1;
    }

    public void setProduct4MixP1(Integer product4MixP1) {
        this.product4MixP1 = product4MixP1;
    }

    public Integer getProduct4MixP2() {
        return product4MixP2;
    }

    public void setProduct4MixP2(Integer product4MixP2) {
        this.product4MixP2 = product4MixP2;
    }

    public Integer getProduct4MixP3() {
        return product4MixP3;
    }

    public void setProduct4MixP3(Integer product4MixP3) {
        this.product4MixP3 = product4MixP3;
    }

    public Integer getProduct4MixP4() {
        return product4MixP4;
    }

    public void setProduct4MixP4(Integer product4MixP4) {
        this.product4MixP4 = product4MixP4;
    }

    public Integer getProduct4MixP5() {
        return product4MixP5;
    }

    public void setProduct4MixP5(Integer product4MixP5) {
        this.product4MixP5 = product4MixP5;
    }

    public Integer getProduct5MixR1() {
        return product5MixR1;
    }

    public void setProduct5MixR1(Integer product5MixR1) {
        this.product5MixR1 = product5MixR1;
    }

    public Integer getProduct5MixR2() {
        return product5MixR2;
    }

    public void setProduct5MixR2(Integer product5MixR2) {
        this.product5MixR2 = product5MixR2;
    }

    public Integer getProduct5MixR3() {
        return product5MixR3;
    }

    public void setProduct5MixR3(Integer product5MixR3) {
        this.product5MixR3 = product5MixR3;
    }

    public Integer getProduct5MixR4() {
        return product5MixR4;
    }

    public void setProduct5MixR4(Integer product5MixR4) {
        this.product5MixR4 = product5MixR4;
    }

    public Integer getProduct5MixR5() {
        return product5MixR5;
    }

    public void setProduct5MixR5(Integer product5MixR5) {
        this.product5MixR5 = product5MixR5;
    }

    public Integer getProduct5MixP1() {
        return product5MixP1;
    }

    public void setProduct5MixP1(Integer product5MixP1) {
        this.product5MixP1 = product5MixP1;
    }

    public Integer getProduct5MixP2() {
        return product5MixP2;
    }

    public void setProduct5MixP2(Integer product5MixP2) {
        this.product5MixP2 = product5MixP2;
    }

    public Integer getProduct5MixP3() {
        return product5MixP3;
    }

    public void setProduct5MixP3(Integer product5MixP3) {
        this.product5MixP3 = product5MixP3;
    }

    public Integer getProduct5MixP4() {
        return product5MixP4;
    }

    public void setProduct5MixP4(Integer product5MixP4) {
        this.product5MixP4 = product5MixP4;
    }

    public Integer getProduct5MixP5() {
        return product5MixP5;
    }

    public void setProduct5MixP5(Integer product5MixP5) {
        this.product5MixP5 = product5MixP5;
    }
}
