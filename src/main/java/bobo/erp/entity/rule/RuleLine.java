package bobo.erp.entity.rule;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/20.
 */
@Entity
public class RuleLine {
    public RuleLine() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Rule rule;

    private Integer type; //类型ID

    private String name;           //生产线名
    private Integer unitInvest;     //单位投资
    private Integer installTime;   //安装时间
    private Integer produceTime;   //生产周期
    private Integer changeInvest;   //转产费用，每周期
    private Integer changeTime;    //转产周期
    private Integer upkeep;         //维护费
    private Integer scrapValue;     //残值
    private Integer depreciation;   //单位周期折旧
    private Integer depreTime;     //折旧周期
    private Integer score;         //潜力分数

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

    public Integer getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Integer installTime) {
        this.installTime = installTime;
    }

    public Integer getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(Integer produceTime) {
        this.produceTime = produceTime;
    }

    public Integer getChangeInvest() {
        return changeInvest;
    }

    public void setChangeInvest(Integer changeInvest) {
        this.changeInvest = changeInvest;
    }

    public Integer getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Integer changeTime) {
        this.changeTime = changeTime;
    }

    public Integer getUpkeep() {
        return upkeep;
    }

    public void setUpkeep(Integer upkeep) {
        this.upkeep = upkeep;
    }

    public Integer getScrapValue() {
        return scrapValue;
    }

    public void setScrapValue(Integer scrapValue) {
        this.scrapValue = scrapValue;
    }

    public Integer getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(Integer depreciation) {
        this.depreciation = depreciation;
    }

    public Integer getDepreTime() {
        return depreTime;
    }

    public void setDepreTime(Integer depreTime) {
        this.depreTime = depreTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
