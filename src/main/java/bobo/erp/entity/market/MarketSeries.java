package bobo.erp.entity.market;

import bobo.erp.entity.market.data.MarketData;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

/**
 * Created by 59814 on 2017/7/23.
 */
@Entity
public class MarketSeries {
    public MarketSeries() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer marketSeriesId;

    private String marketSeriesName;    //市场系列名称
    private Integer marketSeriesUseCount;   //市场系列调用次数
    private Date marketSeriesAlterTime;     //市场系列更改时间
    private String marketSeriesUploader;    //市场系列上传者
    private Integer timeQuantity;   //年份数量
    private Integer areaQuantity;   //区域数量
    private Integer productQuantity;    //产品数量

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "market_data_id")
    private MarketData marketData;  //市场分析数据

    public Integer getMarketSeriesId() {
        return marketSeriesId;
    }

    public void setMarketSeriesId(Integer marketSeriesId) {
        this.marketSeriesId = marketSeriesId;
    }

    public String getMarketSeriesName() {
        return marketSeriesName;
    }

    public void setMarketSeriesName(String marketSeriesName) {
        this.marketSeriesName = marketSeriesName;
    }

    public Integer getMarketSeriesUseCount() {
        return marketSeriesUseCount;
    }

    public void setMarketSeriesUseCount(Integer marketSeriesUseCount) {
        this.marketSeriesUseCount = marketSeriesUseCount;
    }

    public Date getMarketSeriesAlterTime() {
        return marketSeriesAlterTime;
    }

    public void setMarketSeriesAlterTime(Date marketSeriesAlterTime) {
        this.marketSeriesAlterTime = marketSeriesAlterTime;
    }

    public String getMarketSeriesUploader() {
        return marketSeriesUploader;
    }

    public void setMarketSeriesUploader(String marketSeriesUploader) {
        this.marketSeriesUploader = marketSeriesUploader;
    }

    public MarketData getMarketData() {
        return marketData;
    }

    public void setMarketData(MarketData marketData) {
        this.marketData = marketData;
    }

    public Integer getTimeQuantity() {
        return timeQuantity;
    }

    public void setTimeQuantity(Integer timeQuantity) {
        this.timeQuantity = timeQuantity;
    }

    public Integer getAreaQuantity() {
        return areaQuantity;
    }

    public void setAreaQuantity(Integer areaQuantity) {
        this.areaQuantity = areaQuantity;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }
}
