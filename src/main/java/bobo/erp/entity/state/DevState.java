package bobo.erp.entity.state;

import bobo.erp.entity.state.dev.MarketDevState;
import bobo.erp.entity.state.dev.ProductDevState;
import bobo.erp.entity.state.dev.QualificationDevState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

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
    @JoinColumn(name = "dev_state_id")
    private List<MarketDevState> marketDevStateList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "dev_state_id")
    private List<ProductDevState> productDevStateList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "dev_state_id")
    private List<QualificationDevState> qualificationDevStateList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonBackReference
    public RunningState getRunningState() {
        return runningState;
    }

    @JsonBackReference
    public void setRunningState(RunningState runningState) {
        this.runningState = runningState;
    }

    public List<MarketDevState> getMarketDevStateList() {
        return marketDevStateList;
    }

    public void setMarketDevStateList(List<MarketDevState> marketDevStateList) {
        this.marketDevStateList = marketDevStateList;
    }

    public List<ProductDevState> getProductDevStateList() {
        return productDevStateList;
    }

    public void setProductDevStateList(List<ProductDevState> productDevStateList) {
        this.productDevStateList = productDevStateList;
    }

    public List<QualificationDevState> getQualificationDevStateList() {
        return qualificationDevStateList;
    }

    public void setQualificationDevStateList(List<QualificationDevState> qualificationDevStateList) {
        this.qualificationDevStateList = qualificationDevStateList;
    }
}
