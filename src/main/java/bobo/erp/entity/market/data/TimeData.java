package bobo.erp.entity.market.data;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bobo on 2017/10/12.
 */
@Entity
public class TimeData {
    public TimeData() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "time_data_id")
    private List<ProductData> productDataList;

    @ManyToOne
    private MarketData marketData;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public List<ProductData> getProductDataList() {
        return productDataList;
    }

    public void setProductDataList(List<ProductData> productDataList) {
        this.productDataList = productDataList;
    }

    @JsonBackReference
    public MarketData getMarketData() {
        return marketData;
    }

    @JsonBackReference
    public void setMarketData(MarketData marketData) {
        this.marketData = marketData;
    }
}
