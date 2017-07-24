package bobo.erp.controller;

import bobo.erp.domain.teach.SubUserInfo;
import bobo.erp.domain.teach.TeachClassInfo;
import bobo.erp.repository.teach.SubUserInfoRepository;
import bobo.erp.repository.teach.TeachClassInfoRepository;
import bobo.erp.service.teach.TeachClassInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 59814 on 2017/7/23.
 */
@Controller
public class TeachController {
    private Logger logger = LoggerFactory.getLogger(TeachController.class);

    @Autowired
    private TeachClassInit teachClassInit;

    @Autowired
    private TeachClassInfoRepository teachClassInfoRepository;

    @Autowired
    private SubUserInfoRepository subUserInfoRepository;

    @PostMapping(value = "teachClassInit")
    @ResponseBody
    public String teachClassInit(@RequestParam("initPassword") String initPassword,
                                 @RequestParam("nowUserName") String nowUserName,
                                 TeachClassInfo teachClassInfo){
//        String nowUserName = (String) session.getAttribute("nowUserName");
        logger.info("收到初始化请求，参数接收 PWD:{} UN:{}",initPassword, nowUserName);
        return teachClassInit.teachClassInit(teachClassInfo, nowUserName, initPassword);
    }

    @GetMapping(value = "teachClassReturnOriginal")
    @ResponseBody
    public String teachClassReturnOriginal(HttpSession session){
        String nowUserName = (String) session.getAttribute("nowUserName");
        String msg;
        try {
            msg = teachClassInit.teachClassReturnOriginal(nowUserName);
        }catch (Exception e){
            msg = "还原失败，请检查当前状态";
        }
//        return teachClassInit.teachClassReturnOriginal(nowUserName);
        return msg;
    }

    @GetMapping(value = "test")
    @ResponseBody
    public String test(){
        SubUserInfo subUserInfo = subUserInfoRepository.findOne(35);
        System.out.println(subUserInfo.getSubUserName());
        System.out.println(subUserInfo.getTeachClassInfo().getTeachClassId());

        return "TEST";
    }
}
