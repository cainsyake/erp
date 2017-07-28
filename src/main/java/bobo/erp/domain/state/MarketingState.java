package bobo.erp.domain.state;

import bobo.erp.domain.state.marketing.AdvertisingState;
import bobo.erp.domain.state.marketing.OrderState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by 59814 on 2017/7/27.
 */
@Entity
public class MarketingState {
    public MarketingState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(mappedBy = "marketingState")
    private RunningState runningState;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "marketing_state_id")
    private List<AdvertisingState> advertisingStateList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "marketing_state_id")
    private List<OrderState> orderStateList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonBackReference
    public RunningState getRunningState() {
        return runningState;
    }

    @JsonBackReference
    public void setRunningState(RunningState runningState) {
        this.runningState = runningState;
    }

    public List<AdvertisingState> getAdvertisingStateList() {
        return advertisingStateList;
    }

    public void setAdvertisingStateList(List<AdvertisingState> advertisingStateList) {
        this.advertisingStateList = advertisingStateList;
    }

    public List<OrderState> getOrderStateList() {
        return orderStateList;
    }

    public void setOrderStateList(List<OrderState> orderStateList) {
        this.orderStateList = orderStateList;
    }
}
