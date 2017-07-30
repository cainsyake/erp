package bobo.erp.service.rule;

import bobo.erp.domain.rule.Rule;
import bobo.erp.repository.rule.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * Created by 59814 on 2017/7/22.
 */
@Service
public class UseRule {
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

    public List<Rule> ruleFindAll(){
        logger.info("输出全部规则");
        return ruleRepository.findAll();
    }

    public Rule ruleFindOne(Integer ruleId){
        logger.info("输出ID：{} 规则", ruleId);
        return ruleRepository.findOne(ruleId);
    }

    @Transactional
    public String ruleDeleteById(Integer ruleId, String operator){
        logger.info("删除ID：{} 规则，操作者：{}", ruleId, operator);
        ruleRepository.delete(ruleId);
//        ruleFactoryRepository.delete(ruleId);
//        ruleIsoRepository.delete(ruleId);
//        ruleLineRepository.delete(ruleId);
//        ruleMarketRepository.delete(ruleId);
//        ruleMaterialRepository.delete(ruleId);
//        ruleProductMixRepository.delete(ruleId);
//        ruleProductRepository.delete(ruleId);
//        ruleParamRepository.delete(ruleId);
        return "success";
    }
}
