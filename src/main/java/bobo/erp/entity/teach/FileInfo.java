package bobo.erp.entity.teach;

import javax.persistence.*;

/**
 * Created by bobo on 2017/11/18.
 */
@Entity
public class FileInfo {
    public FileInfo() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String type;    //类型
    private String name;    //文件名
    int state;  //状态 0:不可下载 1:可下载 2:已过期

    @ManyToOne
    private TeachClassInfo teachClassInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public TeachClassInfo getTeachClassInfo() {
        return teachClassInfo;
    }

    public void setTeachClassInfo(TeachClassInfo teachClassInfo) {
        this.teachClassInfo = teachClassInfo;
    }
}
