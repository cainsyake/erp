package bobo.erp.entity.collator;

import bobo.erp.entity.teach.TeachClassInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by 59814 on 2017/8/11.
 */
@Entity
public class Collator {
    public Collator() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer areaQuantity;           //区域数量

    private Integer sameTimeOpenQuantity;   //区域同时开放数量

    private Integer productNum;             //产品数量

    @ElementCollection
    @CollectionTable(name="open_area_list")
    @Column(name="area_id")
    private List<Integer> openAreaList;     //当前开放区ID List

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "collator_id")
    private List<AreaCollator> areaCollatorList;    //区域排序器 List

    @OneToOne(mappedBy = "collator")
    private TeachClassInfo teachClassInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAreaQuantity() {
        return areaQuantity;
    }

    public void setAreaQuantity(Integer areaQuantity) {
        this.areaQuantity = areaQuantity;
    }

    public Integer getSameTimeOpenQuantity() {
        return sameTimeOpenQuantity;
    }

    public void setSameTimeOpenQuantity(Integer sameTimeOpenQuantity) {
        this.sameTimeOpenQuantity = sameTimeOpenQuantity;
    }

    public Integer getProductNum() {
        return productNum;
    }

    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    public List<Integer> getOpenAreaList() {
        return openAreaList;
    }

    public void setOpenAreaList(List<Integer> openAreaList) {
        this.openAreaList = openAreaList;
    }

    public List<AreaCollator> getAreaCollatorList() {
        return areaCollatorList;
    }

    public void setAreaCollatorList(List<AreaCollator> areaCollatorList) {
        this.areaCollatorList = areaCollatorList;
    }

    @JsonBackReference
    public TeachClassInfo getTeachClassInfo() {
        return teachClassInfo;
    }

    @JsonBackReference
    public void setTeachClassInfo(TeachClassInfo teachClassInfo) {
        this.teachClassInfo = teachClassInfo;
    }
}
