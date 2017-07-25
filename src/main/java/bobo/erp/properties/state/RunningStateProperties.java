package bobo.erp.properties.state;

import bobo.erp.domain.state.FinanceState;
import bobo.erp.domain.teach.SubUserInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by 59814 on 2017/7/25.
 */
@Component
@ConfigurationProperties(prefix = "runningState")
public class RunningStateProperties {
    public RunningStateProperties() {
    }

    private FinanceState financeState;
    private SubUserInfo subUserInfo;

    public FinanceState getFinanceState() {
        return financeState;
    }

    public void setFinanceState(FinanceState financeState) {
        this.financeState = financeState;
    }

    public SubUserInfo getSubUserInfo() {
        return subUserInfo;
    }

    public void setSubUserInfo(SubUserInfo subUserInfo) {
        this.subUserInfo = subUserInfo;
    }
}
