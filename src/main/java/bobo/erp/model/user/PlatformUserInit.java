package bobo.erp.model.user;

import bobo.erp.entity.user.User;
import bobo.erp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bobo on 2017/10/5.
 */
@Service
public class PlatformUserInit {
    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(PlatformUserInit.class);
    public String platformUserInit(){
        if (userRepository.findAll().size() == 0){
            User user = new User();
            user.setPassword("admin");
            user.setUserLevel(1);
            user.setUserOperator("root");
            user.setUsername("admin");
            userRepository.save(user);
            return "平台初始化完成 - -><br>管理员账号：admin <br>密码：admin <br>请重新登录。";
        }else {
            return "平台已初始化 <- - ERROR<br>请重新登录。<br>如有疑问请查看安装说明或联系软件提供方。";
        }

    }
}
