package bobo.erp.entity.market;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by 59814 on 2017/7/23.
 */
@Entity
public class MarketBid {
    public MarketBid() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer marketBidId;

    private Integer marketSeriesId;     //市场系列id

    private Integer bidYear;        //年份
    private Integer bidArea;        //区域
    private Integer bidProduct;     //产品Id
    private Integer bidQuantity;    //数量
    private Integer bidQualificate; //竞单资质要求

    public Integer getMarketBidId() {
        return marketBidId;
    }

    public void setMarketBidId(Integer marketBidId) {
        this.marketBidId = marketBidId;
    }

    public Integer getMarketSeriesId() {
        return marketSeriesId;
    }

    public void setMarketSeriesId(Integer marketSeriesId) {
        this.marketSeriesId = marketSeriesId;
    }

    public Integer getBidYear() {
        return bidYear;
    }

    public void setBidYear(Integer bidYear) {
        this.bidYear = bidYear;
    }

    public Integer getBidArea() {
        return bidArea;
    }

    public void setBidArea(Integer bidArea) {
        this.bidArea = bidArea;
    }

    public Integer getBidProduct() {
        return bidProduct;
    }

    public void setBidProduct(Integer bidProduct) {
        this.bidProduct = bidProduct;
    }

    public Integer getBidQuantity() {
        return bidQuantity;
    }

    public void setBidQuantity(Integer bidQuantity) {
        this.bidQuantity = bidQuantity;
    }

    public Integer getBidQualificate() {
        return bidQualificate;
    }

    public void setBidQualificate(Integer bidQualificate) {
        this.bidQualificate = bidQualificate;
    }
}
