package bobo.erp.model.rule;

import bobo.erp.entity.rule.*;
import bobo.erp.repository.rule.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    public RuleFactory addRuleFactory(RuleFactory ruleFactory){
        return ruleFactoryRepository.save(ruleFactory);
    }

    public RuleIso addRuleIso(RuleIso ruleIso){
        return ruleIsoRepository.save(ruleIso);
    }

    public RuleLine addRuleLine(RuleLine ruleLine){
        return ruleLineRepository.save(ruleLine);
    }

    public RuleMarket addRuleMarket(RuleMarket ruleMarket){
        return ruleMarketRepository.save(ruleMarket);
    }

    public RuleMaterial addRuleMaterial(RuleMaterial ruleMaterial){
        logger.info("接收测试-》原料1名：{}",ruleMaterial.getMaterial1Name());
        return ruleMaterialRepository.save(ruleMaterial);
    }

    public RuleProductMix addRuleProductMix(RuleProductMix ruleProductMix){
        return ruleProductMixRepository.save(ruleProductMix);
    }

    public RuleProduct addRuleProduct(RuleProduct ruleProduct){
        return ruleProductRepository.save(ruleProduct);
    }

    public Rule addRuleParam(RuleParam ruleParam, Rule rule, String operator){
        ruleParamRepository.save(ruleParam);
        Integer seriesId = ruleParam.getRuleParamId();  //获取系列Id

        rule.setRuleFactory(ruleFactoryRepository.findOne(seriesId));
        rule.setRuleIso(ruleIsoRepository.findOne(seriesId));
        rule.setRuleLine(ruleLineRepository.findOne(seriesId));
        rule.setRuleMarket(ruleMarketRepository.findOne(seriesId));
        rule.setRuleMaterial(ruleMaterialRepository.findOne(seriesId));
        rule.setRuleParam(ruleParamRepository.findOne(seriesId));
        rule.setRuleProductMix(ruleProductMixRepository.findOne(seriesId));
        rule.setRuleProduct(ruleProductRepository.findOne(seriesId));

        Date ruleAlterTime = new Date();
        rule.setRuleAlterTime(ruleAlterTime);
        rule.setRuleUploader(operator);
        rule.setRuleUserCount(0);
        return ruleRepository.save(rule);
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
