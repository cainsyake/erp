package bobo.erp.entity.rule;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bobo on 2017/11/19.
 */
@Entity
public class RuleProductBom {
    public RuleProductBom() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer type;   //产品类型

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bom_id")
    private List<Integer> materialBomList;    //原料组成，List中依次放入原料1、原料2、...、原料n的消耗量

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bom_id")
    private List<Integer> productBomList;    //半成品组成，，List中依次放入产品1、产品2、...、产品n的消耗量

    @ManyToOne
    private Rule rule;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Integer> getMaterialBomList() {
        return materialBomList;
    }

    public void setMaterialBomList(List<Integer> materialBomList) {
        this.materialBomList = materialBomList;
    }

    public List<Integer> getProductBomList() {
        return productBomList;
    }

    public void setProductBomList(List<Integer> productBomList) {
        this.productBomList = productBomList;
    }

    @JsonBackReference
    public Rule getRule() {
        return rule;
    }

    @JsonBackReference
    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
