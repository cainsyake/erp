package bobo.erp.controller;

import bobo.erp.domain.User;
import bobo.erp.repository.UserRepository;
import bobo.erp.service.GetUserInfo;
import bobo.erp.service.PlatformUserInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Scanner;

/**
 * Created by 59814 on 2017/7/18.
 */
@Controller
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private PlatformUserInit platformUserInit;

    @GetMapping(value = "platformUserInit/1005")   //URL中的'1005'仅作随机校验，可更换成任意字符串
    @ResponseBody
    public String platformUserInit(){
        return platformUserInit.platformUserInit();
    }

    @PostMapping(value = "userAdd")
    public String  userAdd(@Valid User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            logger.error("添加的用户格式不正确");
            return null;
        }else{
//            System.out.println("接收测试 " + user.getUsername() + user.getPassword());
            logger.info("添加用户,用户名：{} 等级：{} 操作者：{}", user.getUsername(), user.getUserLevel(), user.getUserOperator());
            user.setPassword(user.getPassword());
            user.setUserLevel(user.getUserLevel());
            user.setUserOperator(user.getUserOperator());
            user.setUsername(user.getUsername());
            userRepository.save(user);
            String msg = "等级：" + user.getUserLevel() + "的新用户 " + user.getUsername() + " 注册成功，请登录。";
            model.addAttribute("loginmessage",msg);
            return "login";
        }
    }

    @GetMapping(value = "userFindByLevel/{userLevel}")
    @ResponseBody
    public List<User> userFindByLevel(@PathVariable("userLevel") Integer userLevel){
        List<User> list = userRepository.findByUserLevel(userLevel);
        if(list.isEmpty()){
            return null;
        }else {
            return list;
        }
    }

    @PostMapping(value = "userChangePassword")
    @ResponseBody
    public String userChangePassword(@Valid User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            logger.error("提交的信息有误");
            return "failure";
        }else{
            logger.info("修改密码,用户名：{} 新密码：{} 操作者：{}", user.getUsername(), user.getPassword(), user.getUserOperator());
            user.setId(user.getId());
            user.setPassword(user.getPassword());
            user.setUserLevel(user.getUserLevel());
            String test = user.getUserOperator();
            if(user.getUserLevel() == 3){
//                User tempUser = userRepository.findOne(user.getId());
                user.setUserOperator(userRepository.findOne(user.getId()).getUserOperator());
            }
            user.setUsername(user.getUsername());
            userRepository.save(user);
            return "success";
        }
    }

    @DeleteMapping(value = "userDelete/{id}/{operator}")
    @ResponseBody
    public String userDelete(@PathVariable("id") Integer id,
                             @PathVariable("operator") String operator){
        logger.info("删除ID：{}的用户，操作者：{}", id, operator);
        userRepository.delete(id);
        return "success";
    }



    @GetMapping(value = "getUserInfo")
    @ResponseBody
    public List<User> getUserInfo(HttpSession session){
        String nowUserName = (String) session.getAttribute("nowUserName");

        List<User> list = getUserInfo.getUserInfoSecure(nowUserName);
        return list;
    }


    @GetMapping(value = "getUserInfoById/{id}")
    @ResponseBody
    public List<User> getUserInfo(@PathVariable("id") Integer id){
        List<User> list = getUserInfo.getUserInfoSecure(id);
        return list;
    }

    @GetMapping(value = "getUserInfoByUsername/{username}")
    @ResponseBody
    public List<User> getUserInfo(@PathVariable("username") String username){
        List<User> list = getUserInfo.getUserInfoSecure(username);
        return list;
    }


}
