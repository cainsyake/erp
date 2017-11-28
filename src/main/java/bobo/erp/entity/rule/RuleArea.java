package bobo.erp.entity.rule;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/20.
 */
@Entity
public class RuleArea {
    public RuleArea() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Rule rule;

    private Integer type; //类型ID

    private String name;         //市场名
    private Integer unitInvest;   //单位开发投资
    private Integer devTime;     //开发周期
    private Integer score;       //潜力分数

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

    public Integer getUnitInvest() {
        return unitInvest;
    }

    public void setUnitInvest(Integer unitInvest) {
        this.unitInvest = unitInvest;
    }

    public Integer getDevTime() {
        return devTime;
    }

    public void setDevTime(Integer devTime) {
        this.devTime = devTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
