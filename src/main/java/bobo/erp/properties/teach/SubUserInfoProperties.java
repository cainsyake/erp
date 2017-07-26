package bobo.erp.properties.teach;

import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.teach.TeachClassInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by 59814 on 2017/7/23.
 */
@Component
@ConfigurationProperties(prefix = "subUserInfo")
public class SubUserInfoProperties {
    public SubUserInfoProperties() {
    }

    private Integer userId;
    private String subUserName;
    private TeachClassInfo teachClassInfo;
    private RunningState runningState;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public RunningState getRunningState() {
        return runningState;
    }

    public void setRunningState(RunningState runningState) {
        this.runningState = runningState;
    }
}
