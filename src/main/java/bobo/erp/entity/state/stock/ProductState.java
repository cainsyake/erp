package bobo.erp.entity.state.stock;

import bobo.erp.entity.state.StockState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/26.
 */
@Entity
public class ProductState {
    public ProductState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer type;
    private Integer quantity;

    @ManyToOne
    private StockState stockState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonBackReference
    public StockState getStockState() {
        return stockState;
    }

    @JsonBackReference
    public void setStockState(StockState stockState) {
        this.stockState = stockState;
    }
}
