package bobo.erp.entity.teach;

import bobo.erp.entity.collator.Collator;

import javax.persistence.*;
import java.util.List;

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
    private Integer time;               //运行时间
    private Integer orderMeetingState;  //选单会状态 0/1/2 -> 未开始/进行中/已结束
    private Integer bidMeetingState;    //竞单会状态 0/1/2 -> 未开始/进行中/已结束

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "teach_class_id")
    private List<SubUserInfo> subUserInfoList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "collator_id")
    private Collator collator;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "teach_class_id")
    private List<FileInfo> fileInfoList;

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

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getOrderMeetingState() {
        return orderMeetingState;
    }

    public void setOrderMeetingState(Integer orderMeetingState) {
        this.orderMeetingState = orderMeetingState;
    }

    public Integer getBidMeetingState() {
        return bidMeetingState;
    }

    public void setBidMeetingState(Integer bidMeetingState) {
        this.bidMeetingState = bidMeetingState;
    }

    public List<FileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(List<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }
}
