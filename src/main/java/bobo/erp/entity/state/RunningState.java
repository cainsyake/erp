package bobo.erp.entity.state;

import bobo.erp.entity.teach.SubUserInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "running_state_id")
    private List<FactoryState> factoryStateList;    //厂房状态
//    private FactoryState factoryState;

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

    @JsonBackReference
    public SubUserInfo getSubUserInfo() {
        return subUserInfo;
    }

    @JsonBackReference
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

    public List<FactoryState> getFactoryStateList() {
        return factoryStateList;
    }

    public void setFactoryStateList(List<FactoryState> factoryStateList) {
        this.factoryStateList = factoryStateList;
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
