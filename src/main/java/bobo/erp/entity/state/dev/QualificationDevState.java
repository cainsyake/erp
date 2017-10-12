package bobo.erp.entity.state.dev;

import bobo.erp.entity.state.DevState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/27.
 */
@Entity
public class QualificationDevState {
    public QualificationDevState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer type;   //资质认证类型
    private Integer state;  //研发状态

    @ManyToOne
    private DevState devState;

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @JsonBackReference
    public DevState getDevState() {
        return devState;
    }

    @JsonBackReference
    public void setDevState(DevState devState) {
        this.devState = devState;
    }
}
