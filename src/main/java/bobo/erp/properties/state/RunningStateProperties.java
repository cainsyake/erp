package bobo.erp.properties.state;

import bobo.erp.domain.state.*;
import bobo.erp.domain.teach.SubUserInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Created by 59814 on 2017/7/25.
 */
@Component
@ConfigurationProperties(prefix = "runningState")
public class RunningStateProperties {
    public RunningStateProperties() {
    }

    private SubUserInfo subUserInfo;
    private BaseState baseState;    //基础状态
    private FinanceState financeState;  //财务状态
    private StockState stockState;      //库存及采购状态
    private FactoryState factoryState;  //厂房状态
    private DevState devState;  //研发状态
    private MarketingState marketingState;  //营销状态

    public SubUserInfo getSubUserInfo() {
        return subUserInfo;
    }

    public void setSubUserInfo(SubUserInfo subUserInfo) {
        this.subUserInfo = subUserInfo;
    }

    public BaseState getBaseState() {
        return baseState;
    }

    public void setBaseState(BaseState baseState) {
        this.baseState = baseState;
    }

    public FinanceState getFinanceState() {
        return financeState;
    }

    public void setFinanceState(FinanceState financeState) {
        this.financeState = financeState;
    }

    public StockState getStockState() {
        return stockState;
    }

    public void setStockState(StockState stockState) {
        this.stockState = stockState;
    }

    public FactoryState getFactoryState() {
        return factoryState;
    }

    public void setFactoryState(FactoryState factoryState) {
        this.factoryState = factoryState;
    }

    public DevState getDevState() {
        return devState;
    }

    public void setDevState(DevState devState) {
        this.devState = devState;
    }

    public MarketingState getMarketingState() {
        return marketingState;
    }

    public void setMarketingState(MarketingState marketingState) {
        this.marketingState = marketingState;
    }
}
