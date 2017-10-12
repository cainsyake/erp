package bobo.erp.repository;

import bobo.erp.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 59814 on 2017/7/18.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    //通过用户名和密码组合查找
    public List<User> findByUsernameAndPassword(String username, String password);

    //通过用户等级查询
    public List<User> findByUserLevel(Integer userLevel);

    //通过用户名查询
    public List<User> findByUsername(String username);

}
