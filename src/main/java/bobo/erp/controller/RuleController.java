package bobo.erp.controller;

import bobo.erp.entity.rule.*;
import bobo.erp.repository.rule.RuleRepository;
import bobo.erp.service.rule.AddRule;
import bobo.erp.service.rule.UseRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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

    @PostMapping(value = "cloneRule")
    @ResponseBody
    public Rule cloneRule(Rule rule){
        return addRule.cloneRule(rule);
    }

    @PostMapping(value = "addRuleFactory")
    @ResponseBody
    public RuleFactory addRuleFactory(@Valid RuleFactory ruleFactory, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return null;
        }else{
            return addRule.addRuleFactory(ruleFactory);
        }
    }

    @PostMapping(value = "addRuleIso")
    @ResponseBody
    public RuleIso addRuleIso(@Valid RuleIso ruleIso, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return null;
        }else{
            return addRule.addRuleIso(ruleIso);
        }
    }

    @PostMapping(value = "addRuleLine")
    @ResponseBody
    public RuleLine addRuleLine(@Valid RuleLine ruleLine, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return null;
        }else{
            return addRule.addRuleLine(ruleLine);
        }
    }

    @PostMapping(value = "addRuleMarket")
    @ResponseBody
    public RuleMarket addRuleMarket(@Valid RuleMarket ruleMarket, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return null;
        }else{
            return addRule.addRuleMarket(ruleMarket);
        }
    }

    @PostMapping(value = "addRuleMaterial")
    @ResponseBody
    public RuleMaterial addRuleMaterial(@Valid RuleMaterial ruleMaterial, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return null;
        }else{
            return addRule.addRuleMaterial(ruleMaterial);
        }
    }


    @PostMapping(value = "addRuleProductMix")
    @ResponseBody
    public RuleProductMix addRuleProductMix(@Valid RuleProductMix ruleProductMix, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return null;
        }else{
            return addRule.addRuleProductMix(ruleProductMix);
        }
    }

    @PostMapping(value = "addRuleProduct")
    @ResponseBody
    public RuleProduct addRuleProduct(@Valid RuleProduct ruleProduct, @Valid Rule rule, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return null;
        }else{
            return addRule.addRuleProduct(ruleProduct);
        }
    }

    @PostMapping(value = "addRuleParam")
    @ResponseBody
    public Rule addRuleParam(@Valid RuleParam ruleParam, @Valid Rule rule, BindingResult bindingResult, HttpSession session){
        if (bindingResult.hasErrors()){
            return null;
        }else{
            String nowUserName = (String) session.getAttribute("nowUserName");
            return addRule.addRuleParam(ruleParam, rule, nowUserName);
        }
    }

    @GetMapping(value = "ruleFindAll")
    @ResponseBody
    public List<Rule> ruleFindAll(){
        List<Rule> list = useRule.ruleFindAll();
        System.out.println(list.get(0).getRuleAlterTime());
        return useRule.ruleFindAll();
    }

    @Autowired
    private RuleRepository ruleRepository;

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
