package bobo.erp.domain.teach;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/23.
 */
@Entity
public class SubUserInfo {
    public SubUserInfo() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer subUserId;

    private Integer userId;             //学生账号ID
    private String subUserName;

    @ManyToOne
    private TeachClassInfo teachClassInfo;

    public Integer getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(Integer subUserId) {
        this.subUserId = subUserId;
    }

    public String getSubUserName() {
        return subUserName;
    }

    public void setSubUserName(String subUserName) {
        this.subUserName = subUserName;
    }

    public TeachClassInfo getTeachClassInfo() {
        return teachClassInfo;
    }

    public void setTeachClassInfo(TeachClassInfo teachClassInfo) {
        this.teachClassInfo = teachClassInfo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
