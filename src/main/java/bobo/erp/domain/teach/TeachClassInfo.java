package bobo.erp.domain.teach;

import bobo.erp.domain.collator.Collator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by 59814 on 2017/7/23.
 */
@Entity
public class TeachClassInfo {

    public TeachClassInfo() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer teachClassId;

    private Integer userId;             //老师ID
    private String teachClassName;      //教学班名称
    private Integer teachClassVolume;   //子用户数量
    private Integer ruleId;             //调用的规则
    private Integer marketSeriesId;     //调用的市场



    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "teach_class_id")
    private List<SubUserInfo> subUserInfoList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "collator_id")
    private Collator collator;

    public Integer getTeachClassId() {
        return teachClassId;
    }

    public void setTeachClassId(Integer teachClassId) {
        this.teachClassId = teachClassId;
    }

    public String getTeachClassName() {
        return teachClassName;
    }

    public void setTeachClassName(String teachClassName) {
        this.teachClassName = teachClassName;
    }

    public Integer getTeachClassVolume() {
        return teachClassVolume;
    }

    public void setTeachClassVolume(Integer teachClassVolume) {
        this.teachClassVolume = teachClassVolume;
    }

    public List<SubUserInfo> getSubUserInfoList() {
        return subUserInfoList;
    }

    public void setSubUserInfoList(List<SubUserInfo> subUserInfoList) {
        this.subUserInfoList = subUserInfoList;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getMarketSeriesId() {
        return marketSeriesId;
    }

    public void setMarketSeriesId(Integer marketSeriesId) {
        this.marketSeriesId = marketSeriesId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Collator getCollator() {
        return collator;
    }

    public void setCollator(Collator collator) {
        this.collator = collator;
    }
}
