package bobo.erp.domain.market;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    private String marketSeriesName;
    private Integer marketSeriesUseCount;
    private Date marketSeriesAlterTime;
    private String marketSeriesUploader;

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
}
