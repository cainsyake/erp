package bobo.erp.service.user;

import bobo.erp.entity.user.User;
import bobo.erp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 59814 on 2017/7/19.
 */
@Service
public class GetUserInfo {
    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(GetUserInfo.class);

    //此方法屏蔽掉user对象中password
    public List<User> getUserInfoSecure(String nowUserName){
        List<User> list = userRepository.findByUsername(nowUserName);
        if(list.isEmpty()){
            logger.info("查找 {} 用户的信息 失败，原因：无此用户", nowUserName);
        }else {
            logger.info("查找 {} 用户的信息 成功。执行安全屏蔽操作。", nowUserName);
        }
        return secureMask(list);
    }
    public List<User> getUserInfoSecure(Integer id){
        try {
            User user = userRepository.findOne(id);
            logger.info("查找 ID:{} 用户的信息 成功。执行安全屏蔽操作。", id);
            List<User> list = new ArrayList<User>();
            list.add(user);
            return secureMask(list);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public List<User> secureMask(List<User> list){
        //只有交换空间才能解除user对象的游离态,如果直接setPassword的话会自动更新到数据库
        User user = list.get(0);
        User newUser = new User();
        newUser.setUserLevel(user.getUserLevel());
        newUser.setUserOperator(user.getUserOperator());
        newUser.setUsername(user.getUsername());
        newUser.setId(user.getId());
        ArrayList<User> newList = new ArrayList<User>();
        newList.add(newUser);
        return newList;
    }


    //此方法获取user对象中全部信息
    public List<User> getUserInfoAll(String nowUserName){

        List<User> list = userRepository.findByUsername(nowUserName);
        if(list.isEmpty()){
            logger.info("查找 {} 用户的信息 失败，原因：无此用户", nowUserName);
        }else {
            logger.info("查找 {} 用户的信息 成功。", nowUserName);
        }
        return list;
    }
    public List<User> getUserInfoAll(Integer id){
        try {
            User user = userRepository.findOne(id);
            logger.info("查找 ID:{} 用户的信息 成功。执行安全屏蔽操作。", id);
            List<User> list = new ArrayList<User>();
            list.add(user);
            return list;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
