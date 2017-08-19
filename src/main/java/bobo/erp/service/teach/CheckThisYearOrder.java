package bobo.erp.service.teach;

import bobo.erp.domain.market.MarketBid;
import bobo.erp.domain.market.MarketOrder;
import bobo.erp.repository.market.MarketBidRepository;
import bobo.erp.repository.market.MarketOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 59814 on 2017/8/19.
 */
@Service
public class CheckThisYearOrder {
    @Autowired
    private MarketOrderRepository marketOrderRepository;
    @Autowired
    private MarketBidRepository marketBidRepository;

    public Integer checkOrder(Integer seriesId, Integer time){
        List<MarketOrder> marketOrderList = marketOrderRepository.findByMarketSeriesIdAndOrderYear(seriesId, time);
        if (marketOrderList.isEmpty()){
            return 0;
        }else {
            return 1;
        }
    }

    public Integer checkBid(Integer seriesId, Integer time){
        List<MarketBid> marketBidList = marketBidRepository.findByMarketSeriesIdAndBidYear(seriesId, time);
        if (marketBidList.isEmpty()){
            return 0;
        }else {
            return 1;
        }
    }

}
