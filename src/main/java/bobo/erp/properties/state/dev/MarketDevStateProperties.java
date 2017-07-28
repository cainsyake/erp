package bobo.erp.properties.state.dev;

import bobo.erp.domain.state.DevState;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.persistence.ManyToOne;

/**
 * Created by 59814 on 2017/7/28.
 */
@Component
@ConfigurationProperties(prefix = "marketDevState")
public class MarketDevStateProperties {
    public MarketDevStateProperties() {
    }

    private Integer type;   //市场类型
    private Integer state;  //研发状态
    private DevState devState;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public DevState getDevState() {
        return devState;
    }

    public void setDevState(DevState devState) {
        this.devState = devState;
    }
}
