package bobo.erp.domain.state;

import bobo.erp.domain.state.dev.MarketDevState;
import bobo.erp.domain.state.dev.ProductDevState;
import bobo.erp.domain.state.dev.QualificationDevState;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/27.
 */
@Entity
public class DevState {
    public DevState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(mappedBy = "devState")
    private RunningState runningState;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "market_dev_state_id")
    private MarketDevState marketDevState;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_dev_state_id")
    private ProductDevState productDevState;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "qualification_dev_state_id")
    private QualificationDevState qualificationDevState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RunningState getRunningState() {
        return runningState;
    }

    public void setRunningState(RunningState runningState) {
        this.runningState = runningState;
    }

    public MarketDevState getMarketDevState() {
        return marketDevState;
    }

    public void setMarketDevState(MarketDevState marketDevState) {
        this.marketDevState = marketDevState;
    }

    public ProductDevState getProductDevState() {
        return productDevState;
    }

    public void setProductDevState(ProductDevState productDevState) {
        this.productDevState = productDevState;
    }

    public QualificationDevState getQualificationDevState() {
        return qualificationDevState;
    }

    public void setQualificationDevState(QualificationDevState qualificationDevState) {
        this.qualificationDevState = qualificationDevState;
    }
}
