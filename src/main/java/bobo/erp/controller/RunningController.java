package bobo.erp.controller;

import bobo.erp.domain.rule.Rule;
import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.marketing.AdvertisingState;
import bobo.erp.service.running.GetSubRunningStateService;
import bobo.erp.service.running.GetTeachClassRuleService;
import bobo.erp.service.running.RunningOperate;
import bobo.erp.service.running.StartRunningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        return runningOperate.advertising(advertisingState, nowUserName);
    }
}
