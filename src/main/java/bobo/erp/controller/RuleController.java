package bobo.erp.controller;

import bobo.erp.entity.common.UniformResult;
import bobo.erp.entity.common.packing.RulePacking;
import bobo.erp.entity.rule.*;
import bobo.erp.service.rule.AddRule;
import bobo.erp.service.rule.UseRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by 59814 on 2017/7/21.
 */
@Controller
public class RuleController {
    private Logger logger = LoggerFactory.getLogger(RuleController.class);

    @Autowired
    private AddRule addRule;

    @Autowired
    private UseRule useRule;

    @PostMapping(value = "addRule")
    @ResponseBody
    public UniformResult addRule(@RequestBody RulePacking rulePacking){
        System.out.print("成功接受请求");
        Rule rule = rulePacking.getRule();
        String nowUserName = rulePacking.getUsername();
        return addRule.addRuleParam(rule, nowUserName);
    }

//    @PostMapping(value = "addRule")
//    @ResponseBody
//    public UniformResult addRule(@RequestBody Rule rule){
//        String nowUserName = "admin";
//        return addRule.addRuleParam(rule, nowUserName);
//    }

//    @PostMapping(value = "addRule/{username}")
//    @ResponseBody
//    public UniformResult addRule(@PathVariable("username") String username, Rule rule){
//        System.out.println(username);
//        System.out.println(rule.getFactoryQuantity());
//        return addRule.addRuleParam(rule, username);
//    }

    @PostMapping(value = "addRuleFactory")
    @ResponseBody
    public UniformResult addRuleFactory(@RequestParam(value = "ruleFactoryArray") RuleFactory[] ruleFactories,
                                        @RequestParam(value = "ruleId") Integer ruleId,
                                        @RequestParam(value = "nowUserName") String nowUserName){
        return addRule.addRuleFactory(ruleFactories, ruleId, nowUserName);
    }

    @PostMapping(value = "addRuleQualification")
    @ResponseBody
    public UniformResult addRuleQualification(@RequestParam(value = "ruleQualificationArray") RuleQualification[] ruleQualifications,
                                                  @RequestParam(value = "ruleId") Integer ruleId,
                                                  @RequestParam(value = "nowUserName") String nowUserName){
        return addRule.addRuleQualification(ruleQualifications, ruleId, nowUserName);
    }

    @PostMapping(value = "addRuleLine")
    @ResponseBody
    public UniformResult addRuleLine(@RequestParam(value = "ruleLineArray") RuleLine[] ruleLines,
                                              @RequestParam(value = "ruleId") Integer ruleId,
                                              @RequestParam(value = "nowUserName") String nowUserName){
        return addRule.addRuleLine(ruleLines, ruleId, nowUserName);
    }

    @PostMapping(value = "addRuleArea")
    @ResponseBody
    public UniformResult addRuleArea(@RequestParam(value = "ruleAreaArray") RuleArea[] ruleAreas,
                                     @RequestParam(value = "ruleId") Integer ruleId,
                                     @RequestParam(value = "nowUserName") String nowUserName){
        return addRule.addRuleArea(ruleAreas, ruleId, nowUserName);
    }

    @PostMapping(value = "addRuleMaterial")
    @ResponseBody
    public UniformResult addRuleMaterial(@RequestParam(value = "ruleMaterialArray") RuleMaterial[] ruleMaterials,
                                     @RequestParam(value = "ruleId") Integer ruleId,
                                     @RequestParam(value = "nowUserName") String nowUserName){
        return addRule.addRuleMaterial(ruleMaterials, ruleId, nowUserName);
    }

    @PostMapping(value = "addRuleProduct")
    @ResponseBody
    public UniformResult addRuleProduct(@RequestParam(value = "ruleProductArray") RuleProduct[] ruleProducts,
                                         @RequestParam(value = "ruleId") Integer ruleId,
                                         @RequestParam(value = "nowUserName") String nowUserName){
        return addRule.addRuleProduct(ruleProducts, ruleId, nowUserName);
    }

    @PostMapping(value = "addRuleProductBom")
    @ResponseBody
    public UniformResult addRuleProductBom(@RequestParam(value = "ruleProductBomArray") RuleProductBom[] ruleProductBoms,
                                        @RequestParam(value = "ruleId") Integer ruleId,
                                        @RequestParam(value = "nowUserName") String nowUserName){
        return addRule.addRuleProductBom(ruleProductBoms, ruleId, nowUserName);
    }

    @GetMapping(value = "ruleFindAll")
    @ResponseBody
    public List<Rule> ruleFindAll(){
        List<Rule> list = useRule.ruleFindAll();
        System.out.println(list.get(0).getRuleAlterTime());
        return useRule.ruleFindAll();
    }

    @GetMapping(value = "ruleFindById/{ruleId}")
    @ResponseBody
    public Rule ruleFindById(@PathVariable("ruleId") Integer ruleId){
        return useRule.ruleFindOne(ruleId);
    }

    @DeleteMapping(value = "ruleDeleteById/{ruleId}")
    @ResponseBody
    public String ruleDeleteById(@PathVariable("ruleId") Integer ruleId, HttpSession session){
        String operator = (String) session.getAttribute("nowUserName");
        return useRule.ruleDeleteById(ruleId, operator);
    }

}
