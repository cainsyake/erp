package bobo.erp.entity.market.data;

import bobo.erp.entity.market.MarketSeries;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bobo on 2017/10/12.
 */
@Entity
public class MarketData {
    public MarketData() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "market_data_id")
    private List<TimeData> timeDataList;

    @OneToOne(mappedBy = "marketData")
    private MarketSeries marketSeries;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TimeData> getTimeDataList() {
        return timeDataList;
    }

    public void setTimeDataList(List<TimeData> timeDataList) {
        this.timeDataList = timeDataList;
    }

    @JsonBackReference
    public MarketSeries getMarketSeries() {
        return marketSeries;
    }
    @JsonBackReference
    public void setMarketSeries(MarketSeries marketSeries) {
        this.marketSeries = marketSeries;
    }
}
