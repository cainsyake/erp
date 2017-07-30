package bobo.erp.domain.state;

import bobo.erp.domain.state.base.OperateState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

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
    /**
     * state的值与对应的状态
     * -2	资不抵债
     * -1	现金不足
     * 0    未开始运营
     * 1    开始运营
     * 10   未开始当季
     * 11   季初运营
     * 12	季中运营
     * 13	季末运营
     */

    private Integer timeYear;       //运营年份
    private Integer timeQuarter;    //运营季度
    private String msg; //后台返回信息，提示

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "operat_state_id")
    private OperateState operateState;

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

    public OperateState getOperateState() {
        return operateState;
    }

    public void setOperateState(OperateState operateState) {
        this.operateState = operateState;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
