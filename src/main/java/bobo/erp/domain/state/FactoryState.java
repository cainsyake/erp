package bobo.erp.domain.state;

import bobo.erp.domain.state.factory.LineState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

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
    private Integer owningState;        //拥有状态 0为租用 大于0为已购买
    private Integer content;            //内含生产线数量
    private Integer finalPaymentTime;   //最后付租/购买时间
    private Integer value;              //厂房价值

//    @OneToOne(mappedBy = "factoryState")
    @ManyToOne
    private RunningState runningState;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "factory_state_id")
    private List<LineState> lineStateList;

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

    @JsonBackReference
    public RunningState getRunningState() {
        return runningState;
    }

    @JsonBackReference
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
