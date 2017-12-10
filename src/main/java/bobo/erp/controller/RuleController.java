package bobo.erp.controller;

import bobo.erp.entity.common.UniformResult;
import bobo.erp.entity.common.packing.rulePack.*;
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
    public UniformResult addRuleFactory(@RequestBody RuleFactoryPacking ruleFactoryPacking){
        System.out.println("成功接受请求：厂房规则");
        RuleFactory[] ruleFactories = ruleFactoryPacking.getFactories();
        String ruleId = ruleFactoryPacking.getRuleId();
        String nowUserName = ruleFactoryPacking.getUsername();
        return addRule.addRuleFactory(ruleFactories, Integer.valueOf(ruleId), nowUserName);
    }

    @PostMapping(value = "addRuleLine")
    @ResponseBody
    public UniformResult addRuleLine(@RequestBody RuleLinePacking ruleLinePacking){
        System.out.println("成功接受请求：生产线规则");
        RuleLine[] ruleLines = ruleLinePacking.getLines();
        String ruleId = ruleLinePacking.getRuleId();
        String nowUserName = ruleLinePacking.getUsername();
        return addRule.addRuleLine(ruleLines, Integer.valueOf(ruleId), nowUserName);
    }

    @PostMapping(value = "addRuleQualification")
    @ResponseBody
    public UniformResult addRuleQualification(@RequestBody RuleQualificationPacking ruleQualificationPacking){
        return addRule.addRuleQualification(ruleQualificationPacking.getQualifications(), Integer.valueOf(ruleQualificationPacking.getRuleId()), ruleQualificationPacking.getUsername());
    }

    @PostMapping(value = "addRuleArea")
    @ResponseBody
    public UniformResult addRuleArea(@RequestBody RuleAreaPacking ruleAreaPacking){
        return addRule.addRuleArea(ruleAreaPacking.getAreas(), Integer.valueOf(ruleAreaPacking.getRuleId()), ruleAreaPacking.getUsername());
    }

    @PostMapping(value = "addRuleMaterial")
    @ResponseBody
    public UniformResult addRuleMaterial(@RequestBody RuleMaterialPacking ruleMaterialPacking){
        return addRule.addRuleMaterial(ruleMaterialPacking.getMaterials(), Integer.valueOf(ruleMaterialPacking.getRuleId()), ruleMaterialPacking.getUsername());
    }

    @PostMapping(value = "addRuleProduct")
    @ResponseBody
    public UniformResult addRuleProduct(@RequestBody RuleProductPacking ruleProductPacking){
        return addRule.addRuleProduct(ruleProductPacking.getProducts(), Integer.valueOf(ruleProductPacking.getRuleId()), ruleProductPacking.getUsername());
    }

    @PostMapping(value = "addRuleProductBom")
    @ResponseBody
    public UniformResult addRuleProductBom(@RequestBody RuleProductBomPacking ruleProductBomPacking){
        return addRule.addRuleProductBom(ruleProductBomPacking.getProductBoms(), Integer.valueOf(ruleProductBomPacking.getRuleId()), ruleProductBomPacking.getUsername());
    }

    @GetMapping(value = "ruleFindAll")
    @ResponseBody
    public List<Rule> ruleFindAll(){
        return useRule.findRuleNotDeleted();
//        List<Rule> list = useRule.ruleFindAll();
//        System.out.println(list.get(0).getRuleAlterTime());
//        return useRule.ruleFindAll();
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

    @PostMapping(value = "deleteRuleInSafety/{id}")
    @ResponseBody
    public UniformResult deleteRuleInSafety(@PathVariable("id") Integer id){
        //此API接口仍在开发中
        //TODO 增加操作者身份验证
        return useRule.ruleDeleteInSafety(id);
    }

}
