package bobo.erp.entity.collator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/8/11.
 */
@Entity
public class SortResult {
    public SortResult() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer rank;       //排位
    private Integer round;      //可选次数
    private String username;    //用户名

    @ManyToOne
    private ProductCollator productCollator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonBackReference
    public ProductCollator getProductCollator() {
        return productCollator;
    }

    @JsonBackReference
    public void setProductCollator(ProductCollator productCollator) {
        this.productCollator = productCollator;
    }
}
