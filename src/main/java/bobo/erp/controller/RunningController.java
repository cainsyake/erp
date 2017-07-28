package bobo.erp.controller;

import bobo.erp.domain.state.BaseState;
import bobo.erp.domain.state.RunningState;
import bobo.erp.repository.state.FactoryStateRepository;
import bobo.erp.repository.state.RunningStateRepository;
import bobo.erp.repository.state.factory.LineStateRepository;
import bobo.erp.repository.teach.SubUserInfoRepository;
import bobo.erp.service.running.GetSubBaseStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 59814 on 2017/7/28.
 */
@Controller
public class RunningController {
    @Autowired
    private GetSubBaseStateService getSubBaseStateService;

    @GetMapping(value = "getSubBaseState/{nowUserName}")
    @ResponseBody
    public BaseState getSubBaseState(@PathVariable("nowUserName") String nowUserName){
        return getSubBaseStateService.getSubBaseState(nowUserName);
    }

}
