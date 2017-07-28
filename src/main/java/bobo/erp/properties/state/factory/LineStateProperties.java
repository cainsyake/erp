package bobo.erp.properties.state.factory;

import bobo.erp.domain.state.FactoryState;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.persistence.ManyToOne;

/**
 * Created by 59814 on 2017/7/28.
 */
@Component
@ConfigurationProperties(prefix = "lineState")
public class LineStateProperties {
    public LineStateProperties() {
    }

    private Integer type;           //生产线类型
    private Integer value;          //生产线价值
    private Integer owningState;    //拥有状态
    private Integer produceState;   //生产状态
    private Integer productType;    //生产产品类型
    private FactoryState factoryState;

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

    public FactoryState getFactoryState() {
        return factoryState;
    }

    public void setFactoryState(FactoryState factoryState) {
        this.factoryState = factoryState;
    }
}
