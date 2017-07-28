package bobo.erp.domain.state;

import bobo.erp.domain.state.stock.MaterialState;
import bobo.erp.domain.state.stock.ProductState;
import bobo.erp.domain.state.stock.PurchaseState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by 59814 on 2017/7/26.
 */
@Entity
public class StockState {
    public StockState() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(mappedBy = "stockState")
    private RunningState runningState;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_state_id")
    private List<MaterialState> materialStateList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_state_id")
    private List<ProductState> productStates;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_state_id")
    private List<PurchaseState> purchaseStates;

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
