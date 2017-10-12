package bobo.erp.entity.rule;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/20.
 */
@Entity
public class RuleMaterial {
    public RuleMaterial() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ruleMaterialId;

    @OneToOne(mappedBy = "ruleMaterial")
    private Rule rule;

    private String material1Name;   //原料名
    private Integer material1Price;  //原料价格
    private Integer material1Time;  //采购提前期

    private String material2Name;
    private Integer material2Price;
    private Integer material2Time;

    private String material3Name;
    private Integer material3Price;
    private Integer material3Time;

    private String material4Name;
    private Integer material4Price;
    private Integer material4Time;

    private String material5Name;
    private Integer material5Price;
    private Integer material5Time;

    public Integer getRuleMaterialId() {
        return ruleMaterialId;
    }

    public void setRuleMaterialId(Integer ruleMaterialId) {
        this.ruleMaterialId = ruleMaterialId;
    }

    @JsonBackReference
    public Rule getRule() {
        return rule;
    }

    @JsonBackReference
    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getMaterial1Name() {
        return material1Name;
    }

    public void setMaterial1Name(String material1Name) {
        this.material1Name = material1Name;
    }

    public Integer getMaterial1Price() {
        return material1Price;
    }

    public void setMaterial1Price(Integer material1Price) {
        this.material1Price = material1Price;
    }

    public Integer getMaterial1Time() {
        return material1Time;
    }

    public void setMaterial1Time(Integer material1Time) {
        this.material1Time = material1Time;
    }

    public String getMaterial2Name() {
        return material2Name;
    }

    public void setMaterial2Name(String material2Name) {
        this.material2Name = material2Name;
    }

    public Integer getMaterial2Price() {
        return material2Price;
    }

    public void setMaterial2Price(Integer material2Price) {
        this.material2Price = material2Price;
    }

    public Integer getMaterial2Time() {
        return material2Time;
    }

    public void setMaterial2Time(Integer material2Time) {
        this.material2Time = material2Time;
    }

    public String getMaterial3Name() {
        return material3Name;
    }

    public void setMaterial3Name(String material3Name) {
        this.material3Name = material3Name;
    }

    public Integer getMaterial3Price() {
        return material3Price;
    }

    public void setMaterial3Price(Integer material3Price) {
        this.material3Price = material3Price;
    }

    public Integer getMaterial3Time() {
        return material3Time;
    }

    public void setMaterial3Time(Integer material3Time) {
        this.material3Time = material3Time;
    }

    public String getMaterial4Name() {
        return material4Name;
    }

    public void setMaterial4Name(String material4Name) {
        this.material4Name = material4Name;
    }

    public Integer getMaterial4Price() {
        return material4Price;
    }

    public void setMaterial4Price(Integer material4Price) {
        this.material4Price = material4Price;
    }

    public Integer getMaterial4Time() {
        return material4Time;
    }

    public void setMaterial4Time(Integer material4Time) {
        this.material4Time = material4Time;
    }

    public String getMaterial5Name() {
        return material5Name;
    }

    public void setMaterial5Name(String material5Name) {
        this.material5Name = material5Name;
    }

    public Integer getMaterial5Price() {
        return material5Price;
    }

    public void setMaterial5Price(Integer material5Price) {
        this.material5Price = material5Price;
    }

    public Integer getMaterial5Time() {
        return material5Time;
    }

    public void setMaterial5Time(Integer material5Time) {
        this.material5Time = material5Time;
    }
}
