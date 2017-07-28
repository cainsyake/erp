package bobo.erp.properties.state;

import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.marketing.AdvertisingState;
import bobo.erp.domain.state.marketing.OrderState;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by 59814 on 2017/7/28.
 */
@Component
@ConfigurationProperties(prefix = "marketingState")
public class MarketingStateProperties {
    public MarketingStateProperties() {
    }

    private RunningState runningState;
    private List<AdvertisingState> advertisingStateList;
    private List<OrderState> orderStateList;

    public RunningState getRunningState() {
        return runningState;
    }

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
