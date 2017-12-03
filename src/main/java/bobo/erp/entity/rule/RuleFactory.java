package bobo.erp.entity.rule;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/20.
 */
@Entity
public class RuleFactory {
    public RuleFactory() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Rule rule;

    private Integer type; //类型ID

    private String name;    //厂房名
    private Integer buyPrice;   //购买价格
    private Integer rentPrice;  //租赁费用
    private Integer salePrice;  //出售价格
    private Integer volume;     //生产线容量
    private Integer quaitityLimit;      //此类厂房购买数量限制
    private Integer score;      //潜力发展系数

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

    public Integer getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Integer buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Integer getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Integer rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getQuaitityLimit() {
        return quaitityLimit;
    }

    public void setQuaitityLimit(Integer quaitityLimit) {
        this.quaitityLimit = quaitityLimit;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
