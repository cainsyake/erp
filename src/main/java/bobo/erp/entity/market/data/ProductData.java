package bobo.erp.entity.market.data;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bobo on 2017/10/12.
 */
@Entity
public class ProductData {
    public ProductData() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_data_id")
    private List<AreaData> areaDataList;

    @ManyToOne
    private TimeData timeData;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<AreaData> getAreaDataList() {
        return areaDataList;
    }

    public void setAreaDataList(List<AreaData> areaDataList) {
        this.areaDataList = areaDataList;
    }

    @JsonBackReference
    public TimeData getTimeData() {
        return timeData;
    }

    @JsonBackReference
    public void setTimeData(TimeData timeData) {
        this.timeData = timeData;
    }
}
