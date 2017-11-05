package bobo.erp.service.user;

import bobo.erp.entity.user.User;
import bobo.erp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by 59814 on 2017/7/18.
 */
@Service
public class LoginCheckService {
    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(LoginCheckService.class);

    public String loginCheck(User userRequest, Model model, HttpSession session){
        if(userRequest.getUsername().equals("")||userRequest.getPassword().equals("")){
            model.addAttribute("loginmessage","用户名和密码不能为空");
            return "login";
        }
        try{
            List<User> userList;
            userList = userRepository.findByUsernameAndPassword(userRequest.getUsername(),userRequest.getPassword());
            if(userList.isEmpty()){
                model.addAttribute("loginmessage","密码错误，请重新输入");
                return "login";
            }else{
                User userResponse = userList.get(0);
                logger.info("等级：{} 用户：{} 已经登录", userResponse.getUserLevel(), userResponse.getUsername());
//                session.setAttribute("loginStatus", "true");
                model.addAttribute("message", userResponse.getUsername());
                session.setAttribute("nowUserLevel",userResponse.getUserLevel());
                session.setAttribute("nowUserName", userResponse.getUsername());
                switch (userResponse.getUserLevel()){
                    case 1:
                        session.setAttribute("loginStatus", "1");
                        return "platformManage";
                    case 2:
                        session.setAttribute("loginStatus", "2");
                        return "teachManage";
                    case 3:
                        session.setAttribute("loginStatus", "3");
                        return "subManage";
                    default:
                        return "login";
                }
            }
        }catch (Exception e){
            System.out.println(e);
            model.addAttribute("loginmessage","登录异常");
            return "login";
        }
    }
}
