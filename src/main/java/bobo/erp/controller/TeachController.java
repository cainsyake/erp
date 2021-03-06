package bobo.erp.controller;

import bobo.erp.entity.market.MarketOrder;
import bobo.erp.entity.teach.TeachClassInfo;
import bobo.erp.repository.teach.SubUserInfoRepository;
import bobo.erp.repository.teach.TeachClassInfoRepository;
import bobo.erp.service.running.GetTeachClassInfoService;
import bobo.erp.service.teach.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;

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
    private ReportGenerationService reportGenerationService;

    @Autowired
    private FileDownloadService fileDownloadService;

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

    @PostMapping(value = "changeTime/{nowUserName}")
    @ResponseBody
    public TeachClassInfo changeTime(@PathVariable("nowUserName") String nowUserName){
        return orderMeeting.changeTime(nowUserName);
    }

    @PostMapping(value = "nextArea/{nowUserName}/{id}")
    @ResponseBody
    public TeachClassInfo nextArea(@PathVariable("nowUserName") String nowUserName, @PathVariable("id") Integer id){
        return orderMeeting.nextArea(nowUserName, id);
    }

    @PostMapping(value = "nextProduct/{nowUserName}/{id}")
    @ResponseBody
    public TeachClassInfo nextProduct(@PathVariable("nowUserName") String nowUserName, @PathVariable("id") Integer id){
        return orderMeeting.nextProduct(nowUserName, id);
    }

    @PostMapping(value = "nextUser/{nowUserName}/{id}")
    @ResponseBody
    public TeachClassInfo nextUser(@PathVariable("nowUserName") String nowUserName, @PathVariable("id") Integer id){
        return orderMeeting.nextUser(nowUserName, id);
    }

    @PostMapping(value = "getOrderList/{nowUserName}/{area}/{product}")
    @ResponseBody
    public List<MarketOrder> getOrderList(@PathVariable("nowUserName") String nowUserName,
                                          @PathVariable("area") Integer area,
                                          @PathVariable("product") Integer product){
        return orderMeeting.getOrderList(nowUserName, area, product);
    }

    @PostMapping(value = "getOrder/{nowUserName}/{area}/{product}/{id}")
    @ResponseBody
    public TeachClassInfo getOrder(@PathVariable("nowUserName") String nowUserName,
                                      @PathVariable("area") Integer area,
                                      @PathVariable("product") Integer product,
                                      @PathVariable("id") Integer id){
        return orderMeeting.getOrder(nowUserName, area, product, id);
    }

    @PostMapping(value = "adReport/{name}")
    @ResponseBody
    public Integer adReport(@PathVariable("name") String name){
        return reportGenerationService.AdReportGeneration(name);
    }

    @RequestMapping(value = "/reportDownload/{type}/{id}", method = RequestMethod.GET)
    public void reportDownload(HttpServletResponse res,@PathVariable("type") String type,@PathVariable("id") Integer id) {
        fileDownloadService.reportDownload(res, type, id);
    }

}
