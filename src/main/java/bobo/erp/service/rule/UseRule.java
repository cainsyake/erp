package bobo.erp.service.rule;

import bobo.erp.entity.common.UniformResult;
import bobo.erp.entity.rule.Rule;
import bobo.erp.repository.common.UniformResultJpa;
import bobo.erp.repository.rule.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private UniformResultJpa uniformResultJpa;

    public List<Rule> ruleFindAll(){
        logger.info("输出全部规则");
        return ruleRepository.findAll();
    }

    public List<Rule> findRuleNotDeleted(){
        List<Rule> ruleList = ruleRepository.findAll();
        List<Rule> rules = new ArrayList<Rule>();
        for (int i = 0; i < ruleList.size(); i++){
            if (ruleList.get(i).getIsDeleted() == 0){
                rules.add(ruleList.get(i));
            }
        }
        return rules;
    }

    @Transactional
    public UniformResult ruleDeleteInSafety(Integer id){
        //软删除
        ruleRepository.findOne(id).setIsDeleted(1);
        UniformResult uniformResult = new UniformResult();
        uniformResult.setUser("SYSTEM");
        uniformResult.setTarget("DELETE RULE SAFETY");
        uniformResult.setMsg("DELETE SUCCESS");
        uniformResult.setState("00");
        return uniformResultJpa.save(uniformResult);
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
