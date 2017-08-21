package bobo.erp.controller;

import bobo.erp.domain.teach.SubUserInfo;
import bobo.erp.domain.teach.TeachClassInfo;
import bobo.erp.repository.teach.SubUserInfoRepository;
import bobo.erp.repository.teach.TeachClassInfoRepository;
import bobo.erp.service.running.GetTeachClassInfoService;
import bobo.erp.service.teach.CheckThisYearOrder;
import bobo.erp.service.teach.OrderMeeting;
import bobo.erp.service.teach.TeachClassInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    private GetTeachClassInfoService getTeachClassInfoService;

    @Autowired
    private CheckThisYearOrder checkThisYearOrder;

    @Autowired
    private OrderMeeting orderMeeting;

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
        return msg;
    }

    @PostMapping(value = "getTeachClassInfo/{nowUserName}")
    @ResponseBody
    public TeachClassInfo getTeachClassInfo(@PathVariable("nowUserName") String nowUserName){
        return getTeachClassInfoService.getTeachClassInfoByUsername(nowUserName);
    }

    @PostMapping(value = "checkThisYearOrder/{seriesId}/{time}")
    @ResponseBody
    public Integer checkThisYearOrder(@PathVariable("seriesId") Integer seriesId, @PathVariable("time") Integer time){
        return checkThisYearOrder.checkOrder(seriesId, time);
    }

    @PostMapping(value = "checkThisYearBid/{seriesId}/{time}")
    @ResponseBody
    public Integer checkThisYearBid(@PathVariable("seriesId") Integer seriesId, @PathVariable("time") Integer time){
        return checkThisYearOrder.checkBid(seriesId, time);
    }

    @PostMapping(value = "startOrderMeeting/{nowUserName}")
    @ResponseBody
    public TeachClassInfo startOrderMeeting(@PathVariable("nowUserName") String nowUserName){
        return orderMeeting.startOrderMeeting(nowUserName);
    }

    @PostMapping(value = "endOrderMeeting/{nowUserName}")
    @ResponseBody
    public TeachClassInfo endOrderMeeting(@PathVariable("nowUserName") String nowUserName){
        return orderMeeting.endOrderMeeting(nowUserName);
    }

}
