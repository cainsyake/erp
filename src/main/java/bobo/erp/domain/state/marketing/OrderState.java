package bobo.erp.domain.state.marketing;

import bobo.erp.domain.state.MarketingState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/27.
 */
@Entity
public class OrderState {
    public OrderState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer orderId;        //订单id
    private Integer year;           //年份
    private Integer area;           //区域
    private Integer totalPrice;     //总价
    private Integer typeId;         //产品类型id
    private Integer quantity;       //产品数量
//    private Integer unitPrice;      //单价
    private Integer deliveryTime;   //交货期
    private Integer accountPeriod;  //账期
    private Integer qualificate;    //资质认证要求 0-无要求 1-资质1 2-资质2 3-资质1和资质2
    private Integer finishTime;     //交单时间(季度)
    private Integer execution;      //完成状态 0-未完成 1-交单 2-违约
    private String owner;          //拥有者

    @ManyToOne
    private MarketingState marketingState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

//    public String getTypeName() {
//        return typeName;
//    }
//
//    public void setTypeName(String typeName) {
//        this.typeName = typeName;
//    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

//    public Integer getUnitPrice() {
//        return unitPrice;
//    }
//
//    public void setUnitPrice(Integer unitPrice) {
//        this.unitPrice = unitPrice;
//    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(Integer accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    public Integer getQualificate() {
        return qualificate;
    }

    public void setQualificate(Integer qualificate) {
        this.qualificate = qualificate;
    }

    public Integer getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getExecution() {
        return execution;
    }

    public void setExecution(Integer execution) {
        this.execution = execution;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @JsonBackReference
    public MarketingState getMarketingState() {
        return marketingState;
    }

    @JsonBackReference
    public void setMarketingState(MarketingState marketingState) {
        this.marketingState = marketingState;
    }
}
