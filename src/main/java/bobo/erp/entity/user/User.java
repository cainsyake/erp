package bobo.erp.entity.user;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by 59814 on 2017/7/18.
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    /**userLevel分为三个等级，1为平台管理员、2为教学班老师、3为教学班学生*/

    private Integer userLevel;
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;

    private String userOperator;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserOperator() {
        return userOperator;
    }

    public void setUserOperator(String userOperator) {
        this.userOperator = userOperator;
    }
}
