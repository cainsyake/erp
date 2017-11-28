package bobo.erp.service.rule;

import bobo.erp.entity.common.UniformResult;
import bobo.erp.entity.rule.*;
import bobo.erp.repository.rule.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 59814 on 2017/7/20.
 */
@Service
public class AddRule {
    private Logger logger = LoggerFactory.getLogger(AddRule.class);

    @Autowired
    private RuleRepository ruleRepository;
    @Autowired
    private RuleFactoryRepository ruleFactoryRepository;
    @Autowired
    private RuleIsoRepository ruleIsoRepository;
    @Autowired
    private RuleLineRepository ruleLineRepository;
    @Autowired
    private RuleMarketRepository ruleMarketRepository;
    @Autowired
    private RuleMaterialRepository ruleMaterialRepository;
    @Autowired
    private RuleParamRepository ruleParamRepository;
    @Autowired
    private RuleProductMixRepository ruleProductMixRepository;
    @Autowired
    private RuleProductRepository ruleProductRepository;

    @Transactional
    public UniformResult addRuleParam(RuleParam ruleParam, String operator){
        Rule rule = new Rule();
        Date ruleAlterTime = new Date();
        rule.setRuleAlterTime(ruleAlterTime);
        rule.setRuleUploader(operator);
        rule.setRuleUserCount(0);
        rule.setRuleParam(ruleParam);
        Rule saveResult = ruleRepository.save(rule);
        Integer id = saveResult.getId();

        UniformResult uniformResult = new UniformResult();
        uniformResult.setState("00");
        uniformResult.setMsg("初始化规则成功");
        uniformResult.setUser(operator);
        uniformResult.setTarget("AddRuleService");

        return uniformResult;
    }

    @Transactional
    public UniformResult addRuleFactory(RuleFactory[] ruleFactories, Integer id, String operator){
        List<RuleFactory> ruleFactoryList = new ArrayList<RuleFactory>();
        for(int i = 0; i < ruleFactories.length; i++){
            ruleFactoryList.add(ruleFactories[i]);  //将数组重组为List
        }

        Rule rule = ruleRepository.findOne(id);
        rule.setRuleFactoryList(ruleFactoryList);
        UniformResult uniformResult = new UniformResult();
        uniformResult.setState("00");
        uniformResult.setMsg("添加厂房部分规则成功");
        uniformResult.setUser(operator);
        uniformResult.setTarget("AddRuleService");
        return uniformResult;
    }

    public RuleQualification addRuleIso(RuleQualification ruleQualification){
        return ruleIsoRepository.save(ruleQualification);
    }

    public RuleLine addRuleLine(RuleLine ruleLine){
        return ruleLineRepository.save(ruleLine);
    }

    public RuleArea addRuleMarket(RuleArea ruleArea){
        return ruleMarketRepository.save(ruleArea);
    }

    public RuleMaterial addRuleMaterial(RuleMaterial ruleMaterial){
        logger.info("接收测试-》原料1名：{}",ruleMaterial.getMaterial1Name());
        return ruleMaterialRepository.save(ruleMaterial);
    }



    public RuleProduct addRuleProduct(RuleProduct ruleProduct){
        return ruleProductRepository.save(ruleProduct);
    }



    @Transactional
    public Rule cloneRule(Rule ruleRemote){
        logger.info("Clone Rule Test");
        Date date = new Date();
        ruleRemote.setRuleAlterTime(date);
        ruleRemote.setRuleId(0);
        ruleRepository.save(ruleRemote);
        return ruleRemote;
    }


}
