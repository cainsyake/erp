package bobo.erp.controller;

import bobo.erp.entity.common.packing.runPack.RunningStatePacking;
import bobo.erp.entity.rule.Rule;
import bobo.erp.entity.state.FactoryState;
import bobo.erp.entity.state.RunningState;
import bobo.erp.entity.state.factory.LineState;
import bobo.erp.entity.state.finance.DebtState;
import bobo.erp.entity.state.marketing.AdvertisingState;
import bobo.erp.entity.teach.FileInfo;
import bobo.erp.entity.teach.SubUserInfo;
import bobo.erp.entity.teach.TeachClassInfo;
import bobo.erp.service.running.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by 59814 on 2017/7/28.
 */
@Controller
public class RunningController {
    @Autowired
    private GetSubRunningStateService getSubRunningStateService;
    @Autowired
    private StartRunningService startRunningService;
    @Autowired
    private GetTeachClassRuleService getTeachClassRuleService;
    @Autowired
    private RunningOperate runningOperate;
    @Autowired
    private GetTeachClassInfoService getTeachClassInfoService;

    @PostMapping(value = "getSubRunningState/{nowUserName}")
    @ResponseBody
    public RunningState getSubRunningState(@PathVariable("nowUserName") String nowUserName){
        return getSubRunningStateService.getSubRunningState(nowUserName);
    }

    @PostMapping(value = "subStartRunning/{nowUserName}")
    @ResponseBody
    public RunningState startRunning(@PathVariable("nowUserName") String nowUserName){
        return startRunningService.startRunning(nowUserName);
    }

    @PostMapping(value = "getTeachClassRule/{nowUserName}")
    @ResponseBody
    public Rule getTeachClassRule(@PathVariable("nowUserName") String nowUserName){
        return getTeachClassRuleService.getTeachClassRule(nowUserName);
    }

    @PostMapping(value = "operateAdvertising/{nowUserName}")
    @ResponseBody
    public RunningState operateAdvertising(@PathVariable("nowUserName") String nowUserName, AdvertisingState advertisingState){
        return runningOperate.advertising(nowUserName, advertisingState);
    }

    @PostMapping(value = "operateGetTeachClassInfo/{nowUserName}")
    @ResponseBody
    public TeachClassInfo operateGetTeachClassInfo(@PathVariable("nowUserName") String nowUserName){
        return getTeachClassInfoService.getTeachClassInfoByUsername(nowUserName);
    }

    @PostMapping(value = "operateApplyDebt/{nowUserName}")  //@PostMapping注解是一个组合注解，该注解将Http Post映射到下面的处理方法上
    @ResponseBody   //@ResponseBody注解将Controller的方法返回的对象通过适当的HttpMessageConverter转换为指定格式后(此为JSON)，写入到Response对象的body数据区
    public RunningState operateApplyDebt(@PathVariable("nowUserName") String nowUserName, DebtState debtState){     //从请求中获取参数nowUserName及debtState
        return runningOperate.applyDebt(nowUserName, debtState);    //调用applyDebt方法并返回runningState
    }

    @PostMapping(value = "operateStartYear/{nowUserName}")
    @ResponseBody
    public RunningState operateStartYear(@PathVariable("nowUserName") String nowUserName){
        return runningOperate.startYear(nowUserName);
    }

    @PostMapping(value = "operateStartQuarter/{nowUserName}")
    @ResponseBody
    public RunningState operateStartQuarter(@PathVariable("nowUserName") String nowUserName){
        return runningOperate.startQuarter(nowUserName);
    }

    @PostMapping(value = "operateUpdatePurchase/{nowUserName}")
    @ResponseBody
    public RunningState operateUpdatePurchase(@PathVariable("nowUserName") String nowUserName){
        return runningOperate.updatePurchase(nowUserName);
    }

    @PostMapping(value = "operateAddPurchase/{nowUserName}")
    @ResponseBody
    public RunningState operateAddPurchase(@PathVariable("nowUserName") String nowUserName,
                                           @RequestParam(value = "array[]") String[] arrays){
        int arrayLength = arrays.length;
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
            list.add(Integer.parseInt(arrays[i]));
        }
        return runningOperate.addPurchase(nowUserName, list);
    }

    @PostMapping(value = "operateNewFactory/{nowUserName}")
    @ResponseBody
    public RunningState operateNewFactory(@PathVariable("nowUserName") String nowUserName, FactoryState factoryState){
        return runningOperate.newFactory(nowUserName, factoryState);
    }

    @PostMapping(value = "operateNewLine/{nowUserName}/{forFactory}")
    @ResponseBody
    public RunningState operateNewLine(@PathVariable("nowUserName") String nowUserName,
                                       @PathVariable("forFactory") Integer factoryId,
                                       LineState lineState){
        return runningOperate.newLine(nowUserName, factoryId, lineState);
    }

    @PostMapping(value = "operateBuildLine/{nowUserName}")
    @ResponseBody
    public RunningState operateBuildLine(@PathVariable("nowUserName") String nowUserName,
                                           @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.buildLine(nowUserName, arrays);
    }

    @PostMapping(value = "operateChangeLine/{nowUserName}/{changeType}")
    @ResponseBody
    public RunningState operateChangeLine(@PathVariable("nowUserName") String nowUserName,
                                         @PathVariable("changeType") Integer changeType,
                                         @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.changeLine(nowUserName, changeType, arrays);
    }

    @PostMapping(value = "operateContinueChange/{nowUserName}")
    @ResponseBody
    public RunningState operateContinueChange(@PathVariable("nowUserName") String nowUserName,
                                         @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.continueChange(nowUserName, arrays);
    }

    @PostMapping(value = "operateSaleLine/{nowUserName}")
    @ResponseBody
    public RunningState operateSaleLine(@PathVariable("nowUserName") String nowUserName,
                                              @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.saleLine(nowUserName, arrays);
    }

    @PostMapping(value = "operateBeginProduction/{nowUserName}")
    @ResponseBody
    public RunningState operateBeginProduction(@PathVariable("nowUserName") String nowUserName,
                                        @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.beginProduction(nowUserName, arrays);
    }

    @PostMapping(value = "operateUpdateReceivable/{nowUserName}")
    @ResponseBody
    public RunningState operateUpdateReceivable(@PathVariable("nowUserName") String nowUserName){
        return runningOperate.updateReceivable(nowUserName);
    }

    @PostMapping(value = "operateDelivery/{nowUserName}")
    @ResponseBody
    public RunningState operateDelivery(@PathVariable("nowUserName") String nowUserName,
                                               @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.delivery(nowUserName, arrays);
    }

    @PostMapping(value = "operateProductDev/{nowUserName}")
    @ResponseBody
    public RunningState operateProductDev(@PathVariable("nowUserName") String nowUserName,
                                        @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.productDev(nowUserName, arrays);
    }

    @PostMapping(value = "operateSaleFactory/{nowUserName}")
    @ResponseBody
    public RunningState operateSaleFactory(@PathVariable("nowUserName") String nowUserName,
                                          @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.saleFactory(nowUserName, arrays);
    }

    @PostMapping(value = "operateExitRent/{nowUserName}")
    @ResponseBody
    public RunningState operateExitRent(@PathVariable("nowUserName") String nowUserName,
                                           @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.exitRent(nowUserName, arrays);
    }

    @PostMapping(value = "operateRentToBuy/{nowUserName}")
    @ResponseBody
    public RunningState operateRentToBuy(@PathVariable("nowUserName") String nowUserName,
                                        @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.rentToBuy(nowUserName, arrays);
    }

    @PostMapping(value = "operateEndQuarter/{nowUserName}")
    @ResponseBody
    public RunningState operateEndQuarter(@PathVariable("nowUserName") String nowUserName){
        return runningOperate.endQuarter(nowUserName);
    }

    @PostMapping(value = "operateMarketDev/{nowUserName}")
    @ResponseBody
    public RunningState operateMarketDev(@PathVariable("nowUserName") String nowUserName,
                                          @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.marketDev(nowUserName, arrays);
    }

    @PostMapping(value = "operateQualificationDev/{nowUserName}")
    @ResponseBody
    public RunningState operateQualificationDev(@PathVariable("nowUserName") String nowUserName,
                                         @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.qualificationDev(nowUserName, arrays);
    }

    @PostMapping(value = "operateEndYear/{nowUserName}")
    @ResponseBody
    public RunningState operateEndYear(@PathVariable("nowUserName") String nowUserName){
        return runningOperate.endYear(nowUserName);
    }

    @PostMapping(value = "operateDiscount/{nowUserName}")
    @ResponseBody
    public RunningState operateDiscount(@PathVariable("nowUserName") String nowUserName,
                                        @RequestParam(value = "array[]") String[] arrays){
        return runningOperate.discount(nowUserName, arrays);
    }

    @PostMapping(value = "operateEmergencyPurchase/{nowUserName}")
    @ResponseBody
    public RunningState operateEmergencyPurchase(@PathVariable("nowUserName") String nowUserName,
                                                 @RequestParam(value = "array1[]") String[] arrays1,
                                                 @RequestParam(value = "array2[]") String[] arrays2){
        return runningOperate.emergencyPurchase(nowUserName, arrays1, arrays2);
    }

    @PostMapping(value = "operateReport/{nowUserName}/{result}")
    @ResponseBody
    public RunningState operateReport(@PathVariable("nowUserName") String nowUserName,
                                      @PathVariable("result") Integer result){
        System.out.println("测试 接收到报表核验结果");
        return runningOperate.report(nowUserName, result);
    }

    @PostMapping(value = "getServiceTime")
    @ResponseBody
    public Date getServiceTime(){
        Date date = new Date();
        return date;
    }

    @PostMapping(value = "getSubUserInfo/{nowUserName}")
    @ResponseBody
    public SubUserInfo getSubUserInfo(@PathVariable("nowUserName") String nowUserName){
        return getSubRunningStateService.getSubUserInfo(nowUserName);
    }

    @PostMapping(value = "getFileInfo/{nowUserName}")
    @ResponseBody
    public List<FileInfo> getFileInfo(@PathVariable("nowUserName") String nowUserName){
        return getTeachClassInfoService.getTeachClassInfoByUsername(nowUserName).getFileInfoList();
    }

    @PostMapping(value = "runningStatePacking")
    @ResponseBody
    public RunningStatePacking getRunningStatePacking(@RequestParam(value = "username") String username){
        return getSubRunningStateService.getRunningStatePacking(username);
    }

}
