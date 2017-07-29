package bobo.erp.domain.state.base;

import bobo.erp.domain.state.BaseState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/29.
 */
@Entity
public class OperateState {
    public OperateState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //以下部分0表示未进行 1表示已进行
    private Integer report;    //财务报表
    private Integer ad;    //投放广告
    private Integer orderMeeting;   //订货会
    private Integer bidMeeting; //竞单会
    private Integer shortLoan;  //申请短贷
    private Integer updatePurchase; //更新原料订单
    private Integer addPurchase;    //下原料订单
    private Integer buildLine;  //在建生产线
    private Integer continueChange; //继续转产
    private Integer saleLine;   //出售生产线
    private Integer beginProduction;    //开始生产
    private Integer updateReceivable;   //应收款更新
    private Integer productDev; //产品研发
    private Integer qualificationDev;   //资质认证研发
    private Integer marketDev;  //市场研发

    @OneToOne(mappedBy = "operateState")
    private BaseState baseState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReport() {
        return report;
    }

    public void setReport(Integer report) {
        this.report = report;
    }

    public Integer getAd() {
        return ad;
    }

    public void setAd(Integer ad) {
        this.ad = ad;
    }

    public Integer getOrderMeeting() {
        return orderMeeting;
    }

    public void setOrderMeeting(Integer orderMeeting) {
        this.orderMeeting = orderMeeting;
    }

    public Integer getBidMeeting() {
        return bidMeeting;
    }

    public void setBidMeeting(Integer bidMeeting) {
        this.bidMeeting = bidMeeting;
    }

    public Integer getShortLoan() {
        return shortLoan;
    }

    public void setShortLoan(Integer shortLoan) {
        this.shortLoan = shortLoan;
    }

    public Integer getUpdatePurchase() {
        return updatePurchase;
    }

    public void setUpdatePurchase(Integer updatePurchase) {
        this.updatePurchase = updatePurchase;
    }

    public Integer getAddPurchase() {
        return addPurchase;
    }

    public void setAddPurchase(Integer addPurchase) {
        this.addPurchase = addPurchase;
    }

    public Integer getBuildLine() {
        return buildLine;
    }

    public void setBuildLine(Integer buildLine) {
        this.buildLine = buildLine;
    }

    public Integer getContinueChange() {
        return continueChange;
    }

    public void setContinueChange(Integer continueChange) {
        this.continueChange = continueChange;
    }

    public Integer getSaleLine() {
        return saleLine;
    }

    public void setSaleLine(Integer saleLine) {
        this.saleLine = saleLine;
    }

    public Integer getBeginProduction() {
        return beginProduction;
    }

    public void setBeginProduction(Integer beginProduction) {
        this.beginProduction = beginProduction;
    }

    public Integer getUpdateReceivable() {
        return updateReceivable;
    }

    public void setUpdateReceivable(Integer updateReceivable) {
        this.updateReceivable = updateReceivable;
    }

    public Integer getProductDev() {
        return productDev;
    }

    public void setProductDev(Integer productDev) {
        this.productDev = productDev;
    }

    public Integer getQualificationDev() {
        return qualificationDev;
    }

    public void setQualificationDev(Integer qualificationDev) {
        this.qualificationDev = qualificationDev;
    }

    public Integer getMarketDev() {
        return marketDev;
    }

    public void setMarketDev(Integer marketDev) {
        this.marketDev = marketDev;
    }

    @JsonBackReference
    public BaseState getBaseState() {
        return baseState;
    }

    @JsonBackReference
    public void setBaseState(BaseState baseState) {
        this.baseState = baseState;
    }
}
