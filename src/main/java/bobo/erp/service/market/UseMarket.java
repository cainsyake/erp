package bobo.erp.service.market;

import bobo.erp.entity.market.MarketBid;
import bobo.erp.entity.market.MarketOrder;
import bobo.erp.entity.market.MarketSeries;
import bobo.erp.repository.market.MarketBidRepository;
import bobo.erp.repository.market.MarketOrderRepository;
import bobo.erp.repository.market.MarketSeriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 59814 on 2017/7/23.
 */
@Service
public class UseMarket {
    private Logger logger = LoggerFactory.getLogger(UseMarket.class);

    @Autowired
    private MarketSeriesRepository marketSeriesRepository;
    @Autowired
    private MarketOrderRepository marketOrderRepository;
    @Autowired
    private MarketBidRepository marketBidRepository;

    public List<MarketSeries> findMarketSeriesAll(){
        return marketSeriesRepository.findAll();
    }

    public List<MarketOrder> findOrderBySeriesId(Integer seriesId){
        logger.info("输出SeriesID:{}市场订单", seriesId);
        return marketOrderRepository.findByMarketSeriesId(seriesId);
    }

    public List<MarketBid> findBidBySeriesId(Integer seriesId){
        logger.info("输出SeriesID:{}市场竞单", seriesId);
        return marketBidRepository.findByMarketSeriesId(seriesId);
    }
}
