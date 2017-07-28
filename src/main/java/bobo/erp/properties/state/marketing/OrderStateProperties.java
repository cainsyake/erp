package bobo.erp.properties.state.marketing;

import bobo.erp.domain.state.MarketingState;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.persistence.ManyToOne;

/**
 * Created by 59814 on 2017/7/28.
 */
@Component
@ConfigurationProperties(prefix = "orderState")
public class OrderStateProperties {
    public OrderStateProperties() {
    }

    private Integer orderId;        //订单id
    private Integer year;           //年份
    private Integer area;           //区域
    private Integer totalPrice;     //总价
    private Integer typeId;         //产品类型id
    private String typeName;       //产品名
    private Integer quantity;       //产品数量
    private Integer unitPrice;      //单价
    private Integer deliveryTime;   //交货期
    private Integer accountPeriod;  //账期
    private Integer qualificate;    //资质认证要求
    private Integer finishTime;     //交单时间(季度)
    private Integer execution;      //完成状态
    private String owner;          //拥有者
    private MarketingState marketingState;

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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

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

    public MarketingState getMarketingState() {
        return marketingState;
    }

    public void setMarketingState(MarketingState marketingState) {
        this.marketingState = marketingState;
    }
}
