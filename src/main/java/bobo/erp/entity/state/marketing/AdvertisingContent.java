package bobo.erp.entity.state.marketing;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by bobo on 2017/12/9.
 */
@Entity
public class AdvertisingContent {
    public AdvertisingContent() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer areaType;       //区域类型ID
    private Integer productType;    //产品类型ID
    private Integer adValue;        //广告投放额

    @ManyToOne
    private AdvertisingState advertisingState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAreaType() {
        return areaType;
    }

    public void setAreaType(Integer areaType) {
        this.areaType = areaType;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Integer getAdValue() {
        return adValue;
    }

    public void setAdValue(Integer adValue) {
        this.adValue = adValue;
    }

    @JsonBackReference
    public AdvertisingState getAdvertisingState() {
        return advertisingState;
    }

    @JsonBackReference
    public void setAdvertisingState(AdvertisingState advertisingState) {
        this.advertisingState = advertisingState;
    }
}
