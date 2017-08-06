package bobo.erp.controller;

import bobo.erp.domain.rule.Rule;
import bobo.erp.domain.state.FactoryState;
import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.factory.LineState;
import bobo.erp.domain.state.finance.DebtState;
import bobo.erp.domain.state.marketing.AdvertisingState;
import bobo.erp.domain.teach.TeachClassInfo;
import bobo.erp.repository.rule.RuleRepository;
import bobo.erp.service.running.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private RuleRepository ruleRepository;


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

    @PostMapping(value = "operateApplyDebt/{nowUserName}")
    @ResponseBody
    public RunningState operateApplyDebt(@PathVariable("nowUserName") String nowUserName, DebtState debtState){
        return runningOperate.applyDebt(nowUserName, debtState);
    }

    @PostMapping(value = "operateStartYear/{nowUserName}")
    @ResponseBody
    public RunningState operateStartYear(@PathVariable("nowUserName") String nowUserName){
        return runningOperate.startYear(nowUserName);
    }

    @PostMapping(value = "operateStartQuarter/{nowUserName}")
    @ResponseBody
    public RunningState operateStartQuarter(@PathVariable("nowUserName") String nowUserName){
        return runningOperate.starQuarter(nowUserName);
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

}
