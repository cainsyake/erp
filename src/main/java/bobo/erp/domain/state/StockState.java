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
    private List<ProductState> productStateList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_state_id")
    private List<PurchaseState> purchaseStateList;

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

    public List<ProductState> getProductStateList() {
        return productStateList;
    }

    public void setProductStateList(List<ProductState> productStateList) {
        this.productStateList = productStateList;
    }

    public List<PurchaseState> getPurchaseStateList() {
        return purchaseStateList;
    }

    public void setPurchaseStateList(List<PurchaseState> purchaseStateList) {
        this.purchaseStateList = purchaseStateList;
    }
}
