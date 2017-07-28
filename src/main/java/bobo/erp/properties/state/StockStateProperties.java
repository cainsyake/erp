package bobo.erp.properties.state;

import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.stock.MaterialState;
import bobo.erp.domain.state.stock.ProductState;
import bobo.erp.domain.state.stock.PurchaseState;
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
@ConfigurationProperties(prefix = "stockState")
public class StockStateProperties {
    public StockStateProperties() {
    }

    private RunningState runningState;
    private List<MaterialState> materialStateList;
    private List<ProductState> productStates;
    private List<PurchaseState> purchaseStates;

    public RunningState getRunningState() {
        return runningState;
    }

    public void setRunningState(RunningState runningState) {
        this.runningState = runningState;
    }

    public List<MaterialState> getMaterialStateList() {
        return materialStateList;
    }

    public void setMaterialStateList(List<MaterialState> materialStateList) {
        this.materialStateList = materialStateList;
    }

    public List<ProductState> getProductStates() {
        return productStates;
    }

    public void setProductStates(List<ProductState> productStates) {
        this.productStates = productStates;
    }

    public List<PurchaseState> getPurchaseStates() {
        return purchaseStates;
    }

    public void setPurchaseStates(List<PurchaseState> purchaseStates) {
        this.purchaseStates = purchaseStates;
    }
}
