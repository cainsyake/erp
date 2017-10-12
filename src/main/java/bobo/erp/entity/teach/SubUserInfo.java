package bobo.erp.entity.teach;

import bobo.erp.entity.state.RunningState;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/23.
 */
@Entity
public class SubUserInfo {
    public SubUserInfo() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer subUserId;

    private Integer userId;             //学生账号ID
    private String subUserName;

    @ManyToOne
    private TeachClassInfo teachClassInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "running_state_id")
    private RunningState runningState;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "running_state_backup_y_id")
    private RunningState runningStateStartYear;     //年初备份

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "running_state_backup_q_id")
    private RunningState runningStateStartQuarter;  //季初备份

    public Integer getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(Integer subUserId) {
        this.subUserId = subUserId;
    }

    public String getSubUserName() {
        return subUserName;
    }

    public void setSubUserName(String subUserName) {
        this.subUserName = subUserName;
    }

    public TeachClassInfo getTeachClassInfo() {
        return teachClassInfo;
    }

    public void setTeachClassInfo(TeachClassInfo teachClassInfo) {
        this.teachClassInfo = teachClassInfo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public RunningState getRunningState() {
        return runningState;
    }

    public void setRunningState(RunningState runningState) {
        this.runningState = runningState;
    }

    public RunningState getRunningStateStartYear() {
        return runningStateStartYear;
    }

    public void setRunningStateStartYear(RunningState runningStateStartYear) {
        this.runningStateStartYear = runningStateStartYear;
    }

    public RunningState getRunningStateStartQuarter() {
        return runningStateStartQuarter;
    }

    public void setRunningStateStartQuarter(RunningState runningStateStartQuarter) {
        this.runningStateStartQuarter = runningStateStartQuarter;
    }
}
