package bobo.erp.domain.state;

import bobo.erp.domain.state.factory.LineState;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/26.
 */
@Entity
public class FactoryState {
    public FactoryState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer type;               //厂房类型
    private Integer owningState;        //拥有状态
    private Integer content;            //内含生产线数量
    private Integer finalPaymentTime;   //最后付租/购买时间
    private Integer value;              //厂房价值

    @OneToOne(mappedBy = "factoryState")
    private RunningState runningState;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "line_state_id")
    private LineState lineState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public LineState getLineState() {
        return lineState;
    }

    public void setLineState(LineState lineState) {
        this.lineState = lineState;
    }
}
