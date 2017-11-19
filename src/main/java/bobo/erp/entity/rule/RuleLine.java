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

    private String lineName;           //生产线名
    private Integer lineUnitInvest;     //单位投资
    private Integer lineInstallTime;   //安装时间
    private Integer lineProduceTime;   //生产周期
    private Integer lineChangeInvest;   //转产费用，每周期
    private Integer lineChangeTime;    //转产周期
    private Integer lineUpkeep;         //维护费
    private Integer lineScrapValue;     //残值
    private Integer lineDepreciation;   //单位周期折旧
    private Integer lineDepreTime;     //折旧周期
    private Integer lineScore;         //潜力分数

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

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Integer getLineUnitInvest() {
        return lineUnitInvest;
    }

    public void setLineUnitInvest(Integer lineUnitInvest) {
        this.lineUnitInvest = lineUnitInvest;
    }

    public Integer getLineInstallTime() {
        return lineInstallTime;
    }

    public void setLineInstallTime(Integer lineInstallTime) {
        this.lineInstallTime = lineInstallTime;
    }

    public Integer getLineProduceTime() {
        return lineProduceTime;
    }

    public void setLineProduceTime(Integer lineProduceTime) {
        this.lineProduceTime = lineProduceTime;
    }

    public Integer getLineChangeInvest() {
        return lineChangeInvest;
    }

    public void setLineChangeInvest(Integer lineChangeInvest) {
        this.lineChangeInvest = lineChangeInvest;
    }

    public Integer getLineChangeTime() {
        return lineChangeTime;
    }

    public void setLineChangeTime(Integer lineChangeTime) {
        this.lineChangeTime = lineChangeTime;
    }

    public Integer getLineUpkeep() {
        return lineUpkeep;
    }

    public void setLineUpkeep(Integer lineUpkeep) {
        this.lineUpkeep = lineUpkeep;
    }

    public Integer getLineScrapValue() {
        return lineScrapValue;
    }

    public void setLineScrapValue(Integer lineScrapValue) {
        this.lineScrapValue = lineScrapValue;
    }

    public Integer getLineDepreciation() {
        return lineDepreciation;
    }

    public void setLineDepreciation(Integer lineDepreciation) {
        this.lineDepreciation = lineDepreciation;
    }

    public Integer getLineDepreTime() {
        return lineDepreTime;
    }

    public void setLineDepreTime(Integer lineDepreTime) {
        this.lineDepreTime = lineDepreTime;
    }

    public Integer getLineScore() {
        return lineScore;
    }

    public void setLineScore(Integer lineScore) {
        this.lineScore = lineScore;
    }
}
