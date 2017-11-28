package bobo.erp.entity.rule;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/20.
 */
@Entity
public class RuleProduct {
    public RuleProduct() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Rule rule;

    private Integer type; //类型ID

    private String name;        //产品名
    private Integer procCost;   //加工费
    private Integer devInvest;  //单位周期开发费用
    private Integer devTime;    //开发周期
    private Integer finalCost;  //直接成本，含加工费及原料成本
    private Integer score;      //潜力分数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonBackReference
    public Rule getRule() {
        return rule;
    }

    @JsonBackReference
    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProcCost() {
        return procCost;
    }

    public void setProcCost(Integer procCost) {
        this.procCost = procCost;
    }

    public Integer getDevInvest() {
        return devInvest;
    }

    public void setDevInvest(Integer devInvest) {
        this.devInvest = devInvest;
    }

    public Integer getDevTime() {
        return devTime;
    }

    public void setDevTime(Integer devTime) {
        this.devTime = devTime;
    }

    public Integer getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(Integer finalCost) {
        this.finalCost = finalCost;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
