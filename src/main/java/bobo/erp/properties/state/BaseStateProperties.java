package bobo.erp.properties.state;

import bobo.erp.domain.state.RunningState;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.persistence.OneToOne;

/**
 * Created by 59814 on 2017/7/28.
 */
@Component
@ConfigurationProperties(prefix = "baseState")
public class BaseStateProperties {
    public BaseStateProperties() {
    }

    private Integer state;          //运营状态
    private Integer timeYear;       //运营年份
    private Integer timeQuarter;    //运营季度
    private RunningState runningState;

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

    public RunningState getRunningState() {
        return runningState;
    }

    public void setRunningState(RunningState runningState) {
        this.runningState = runningState;
    }
}