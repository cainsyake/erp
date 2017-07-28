package bobo.erp.properties.state;

import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.dev.MarketDevState;
import bobo.erp.domain.state.dev.ProductDevState;
import bobo.erp.domain.state.dev.QualificationDevState;
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
@ConfigurationProperties(prefix = "devState")
public class DevStateProperties {
    public DevStateProperties() {
    }

    private RunningState runningState;
    private List<MarketDevState> marketDevStateList;
    private List<ProductDevState> productDevStateList;
    private List<QualificationDevState> qualificationDevStateList;

    public RunningState getRunningState() {
        return runningState;
    }

    public void setRunningState(RunningState runningState) {
        this.runningState = runningState;
    }

    public List<MarketDevState> getMarketDevStateList() {
        return marketDevStateList;
    }

    public void setMarketDevStateList(List<MarketDevState> marketDevStateList) {
        this.marketDevStateList = marketDevStateList;
    }

    public List<ProductDevState> getProductDevStateList() {
        return productDevStateList;
    }

    public void setProductDevStateList(List<ProductDevState> productDevStateList) {
        this.productDevStateList = productDevStateList;
    }

    public List<QualificationDevState> getQualificationDevStateList() {
        return qualificationDevStateList;
    }

    public void setQualificationDevStateList(List<QualificationDevState> qualificationDevStateList) {
        this.qualificationDevStateList = qualificationDevStateList;
    }
}
