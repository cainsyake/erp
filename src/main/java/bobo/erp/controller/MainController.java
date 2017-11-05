package bobo.erp.controller;

import bobo.erp.entity.user.User;
import bobo.erp.service.user.LoginCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by 59814 on 2017/7/18.
 */
@Controller
public class MainController {
    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private LoginCheckService loginCheckService;

    @GetMapping(value = "/")
    public String fromIndex(){
        return "login";
    }

    @PostMapping(value = "/usercheck")
    public String userCheck(@Valid User user, BindingResult bindingResult, Model model, HttpSession session){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            logger.info("表单格式错误");
            return null;
        }else{
            return loginCheckService.loginCheck(user, model, session);
        }
    }


}
