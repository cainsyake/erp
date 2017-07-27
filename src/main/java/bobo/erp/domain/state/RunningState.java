package bobo.erp.domain.state;

import bobo.erp.domain.teach.SubUserInfo;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/25.
 */
@Entity
public class RunningState {
    public RunningState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(mappedBy = "runningState")
    private SubUserInfo subUserInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "base_state_id")
    private BaseState baseState;    //基础状态

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "finance_state_id")
    private FinanceState financeState;  //财务状态

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_state_id")
    private StockState stockState;      //库存及采购状态

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "factory_state_id")
    private FactoryState factoryState;  //厂房状态

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dev_state_id")
    private DevState devState;  //研发状态

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "marketing_state_id")
    private MarketingState marketingState;  //营销状态

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SubUserInfo getSubUserInfo() {
        return subUserInfo;
    }

    public void setSubUserInfo(SubUserInfo subUserInfo) {
        this.subUserInfo = subUserInfo;
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

    public BaseState getBaseState() {
        return baseState;
    }

    public void setBaseState(BaseState baseState) {
        this.baseState = baseState;
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
