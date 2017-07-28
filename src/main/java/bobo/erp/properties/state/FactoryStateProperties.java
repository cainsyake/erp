package bobo.erp.properties.state;

import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.factory.LineState;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * Created by 59814 on 2017/7/28.
 */
@Component
@ConfigurationProperties(prefix = "factoryState")
public class FactoryStateProperties {
    public FactoryStateProperties() {
    }
    private Integer type;               //厂房类型
    private Integer owningState;        //拥有状态
    private Integer content;            //内含生产线数量
    private Integer finalPaymentTime;   //最后付租/购买时间
    private Integer value;              //厂房价值
    private RunningState runningState;
    private List<LineState> lineStateList;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOwningState() {
        return owningState;
    }

    public void setOwningState(Integer owningState) {
        this.owningState = owningState;
    }

    public Integer getContent() {
        return content;
    }

    public void setContent(Integer content) {
        this.content = content;
    }

    public Integer getFinalPaymentTime() {
        return finalPaymentTime;
    }

    public void setFinalPaymentTime(Integer finalPaymentTime) {
        this.finalPaymentTime = finalPaymentTime;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public RunningState getRunningState() {
        return runningState;
    }

    public void setRunningState(RunningState runningState) {
        this.runningState = runningState;
    }

    public List<LineState> getLineStateList() {
        return lineStateList;
    }

    public void setLineStateList(List<LineState> lineStateList) {
        this.lineStateList = lineStateList;
    }
}
