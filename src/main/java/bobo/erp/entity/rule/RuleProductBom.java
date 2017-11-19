package bobo.erp.entity.rule;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bobo on 2017/11/19.
 */
@Entity
public class RuleProductBom {
    public RuleProductBom() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer type;   //产品类型

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bom_id")
    private List<Integer> materialBomList;    //原料组成

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bom_id")
    private List<Integer> productBomList;    //半成品组成

    @ManyToOne
    private Rule rule;
}
