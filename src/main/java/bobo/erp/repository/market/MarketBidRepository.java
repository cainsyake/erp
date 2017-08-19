package bobo.erp.repository.market;

import bobo.erp.domain.market.MarketBid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 59814 on 2017/7/23.
 */
public interface MarketBidRepository extends JpaRepository<MarketBid, Integer> {
    public List<MarketBid> findByMarketSeriesId(Integer seriesId);

    public List<MarketBid> findByMarketSeriesIdAndBidYear(Integer seriesId, Integer bidYear);

    public List<MarketBid> findByMarketSeriesIdAndBidYearAndBidArea(Integer seriesId, Integer bidYear, Integer bidArea);

    public List<MarketBid> findByMarketSeriesIdAndBidYearAndBidAreaAndBidProduct(Integer seriesId, Integer bidYear, Integer bidArea, Integer bidProduct);
}
