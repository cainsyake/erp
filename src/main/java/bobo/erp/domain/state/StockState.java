package bobo.erp.domain.state;

import bobo.erp.domain.state.stock.MaterialState;
import bobo.erp.domain.state.stock.ProductState;
import bobo.erp.domain.state.stock.PurchaseState;

import javax.persistence.*;

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
    @JoinColumn(name = "material_state_id")
    private MaterialState materialState;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_state_id")
    private ProductState productState;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_state_id")
    private PurchaseState purchaseState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RunningState getRunningState() {
        return runningState;
    }

    public void setRunningState(RunningState runningState) {
        this.runningState = runningState;
    }

    public MaterialState getMaterialState() {
        return materialState;
    }

    public void setMaterialState(MaterialState materialState) {
        this.materialState = materialState;
    }

    public ProductState getProductState() {
        return productState;
    }

    public void setProductState(ProductState productState) {
        this.productState = productState;
    }

    public PurchaseState getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(PurchaseState purchaseState) {
        this.purchaseState = purchaseState;
    }
}
