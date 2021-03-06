package bobo.erp.entity.collator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by 59814 on 2017/8/11.
 */
@Entity
public class ProductCollator {
    public ProductCollator() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer type;           //产品类型

    private Integer state;          //开放状态  0：未开放 1：正在开放 2：结束开放

    private Integer openUser;       //当前开放的用户排位

    private Date time;  //到期时间

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_collator_id")
    private List<SortResult> sortResultList;    //排序结果 List

    @ElementCollection
    @CollectionTable(name="order_id_list")
    @Column(name="order_id")
    private List<Integer> orderIdList;

    @ManyToOne
    private AreaCollator areaCollator;

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

    public Integer getOpenUser() {
        return openUser;
    }

    public void setOpenUser(Integer openUser) {
        this.openUser = openUser;
    }

    public List<SortResult> getSortResultList() {
        return sortResultList;
    }

    public void setSortResultList(List<SortResult> sortResultList) {
        this.sortResultList = sortResultList;
    }

    public List<Integer> getOrderIdList() {
        return orderIdList;
    }

    public void setOrderIdList(List<Integer> orderIdList) {
        this.orderIdList = orderIdList;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @JsonBackReference
    public AreaCollator getAreaCollator() {
        return areaCollator;
    }

    @JsonBackReference
    public void setAreaCollator(AreaCollator areaCollator) {
        this.areaCollator = areaCollator;
    }
}
