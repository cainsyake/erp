package bobo.erp.domain.collator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by 59814 on 2017/8/11.
 */
@Entity
public class AreaCollator {
    public AreaCollator() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer type;           //区域类型

    private Integer state;          //开放状态  -1：未开放 0：正在开放 1：结束开放

    private Integer openProduct;    //当前开放产品id

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "area_collator_id")
    private List<ProductCollator> productCollatorList;  //产品排序器 List

    @ManyToOne
    private Collator collator;

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

    public Integer getOpenProduct() {
        return openProduct;
    }

    public void setOpenProduct(Integer openProduct) {
        this.openProduct = openProduct;
    }

    public List<ProductCollator> getProductCollatorList() {
        return productCollatorList;
    }

    public void setProductCollatorList(List<ProductCollator> productCollatorList) {
        this.productCollatorList = productCollatorList;
    }

    @JsonBackReference
    public Collator getCollator() {
        return collator;
    }

    @JsonBackReference
    public void setCollator(Collator collator) {
        this.collator = collator;
    }
}
