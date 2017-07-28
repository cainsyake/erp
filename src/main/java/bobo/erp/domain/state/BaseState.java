package bobo.erp.domain.state;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/27.
 */
@Entity
public class BaseState {
    public BaseState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer state;          //运营状态
    private Integer timeYear;       //运营年份
    private Integer timeQuarter;    //运营季度

    @OneToOne(mappedBy = "baseState")
    private RunningState runningState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getTimeYear() {
        return timeYear;
    }

    public void setTimeYear(Integer timeYear) {
        this.timeYear = timeYear;
    }

    public Integer getTimeQuarter() {
        return timeQuarter;
    }

    public void setTimeQuarter(Integer timeQuarter) {
        this.timeQuarter = timeQuarter;
    }

    @JsonBackReference
    public RunningState getRunningState() {
        return runningState;
    }

    @JsonBackReference
    public void setRunningState(RunningState runningState) {
        this.runningState = runningState;
    }
}
