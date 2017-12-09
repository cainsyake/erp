package bobo.erp.entity.state.marketing;

import bobo.erp.entity.state.MarketingState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by 59814 on 2017/7/27.
 */
@Entity
public class AdvertisingState {
    public AdvertisingState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer year;       //年份
    private Integer bidCost;    //标书费
    private Integer totalAd;    //总广告费(不含标书费)


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "advertising_state_id")
    private List<AdvertisingContent> advertisingContentList;

    @ManyToOne
    private MarketingState marketingState;

    @JsonBackReference
    public MarketingState getMarketingState() {
        return marketingState;
    }

    @JsonBackReference
    public void setMarketingState(MarketingState marketingState) {
        this.marketingState = marketingState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getBidCost() {
        return bidCost;
    }

    public void setBidCost(Integer bidCost) {
        this.bidCost = bidCost;
    }

    public Integer getTotalAd() {
        return totalAd;
    }

    public void setTotalAd(Integer totalAd) {
        this.totalAd = totalAd;
    }

    public List<AdvertisingContent> getAdvertisingContentList() {
        return advertisingContentList;
    }

    public void setAdvertisingContentList(List<AdvertisingContent> advertisingContentList) {
        this.advertisingContentList = advertisingContentList;
    }
}
