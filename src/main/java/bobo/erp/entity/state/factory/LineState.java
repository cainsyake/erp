package bobo.erp.entity.state.factory;

import bobo.erp.entity.state.FactoryState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/26.
 */
@Entity
public class LineState {
    public LineState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer type;           //生产线类型
    private Integer value;          //生产线价值
    private Integer owningState;    //拥有状态
    private Integer produceState;   //生产状态
    private Integer productType;    //生产产品类型

    @ManyToOne
    private FactoryState factoryState;

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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getOwningState() {
        return owningState;
    }

    public void setOwningState(Integer owningState) {
        this.owningState = owningState;
    }

    public Integer getProduceState() {
        return produceState;
    }

    public void setProduceState(Integer produceState) {
        this.produceState = produceState;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    @JsonBackReference
    public FactoryState getFactoryState() {
        return factoryState;
    }

    @JsonBackReference
    public void setFactoryState(FactoryState factoryState) {
        this.factoryState = factoryState;
    }
}
