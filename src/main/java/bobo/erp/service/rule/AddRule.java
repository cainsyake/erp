package bobo.erp.service.rule;

import bobo.erp.entity.common.UniformResult;
import bobo.erp.entity.rule.*;
import bobo.erp.repository.common.UniformResultJpa;
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
    private UniformResultJpa uniformResultJpa;

    @Transactional
    public UniformResult addRuleParam(Rule rule, String operator){
//        Rule rule = new Rule();
        Date ruleAlterTime = new Date();
        rule.setRuleAlterTime(ruleAlterTime);
        rule.setRuleUploader(operator);
        rule.setRuleUserCount(0);
//        rule.setRuleParam(ruleParam);
        Rule saveResult = ruleRepository.save(rule);
        Integer id = saveResult.getId();

        UniformResult uniformResult = new UniformResult();
        uniformResult.setState("00");
        uniformResult.setMsg(String.valueOf(id));
        uniformResult.setUser(operator);
        uniformResult.setTarget("AddRuleService");

        return uniformResultJpa.save(uniformResult);
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
        uniformResult.setMsg("Add RuleFactory Success");
        uniformResult.setUser(operator);
        uniformResult.setTarget("AddRuleFactoryService");
        return uniformResultJpa.save(uniformResult);
    }

    @Transactional
    public UniformResult addRuleQualification(RuleQualification[] ruleQualifications, Integer id, String operator){
        List<RuleQualification> ruleQualificationList = new ArrayList<RuleQualification>();
        for(int i = 0; i < ruleQualifications.length; i++){
            ruleQualificationList.add(ruleQualifications[i]);  //将数组重组为List
        }
        Rule rule = ruleRepository.findOne(id);
        rule.setRuleQualificationList(ruleQualificationList);
        UniformResult uniformResult = new UniformResult();
        uniformResult.setState("00");
        uniformResult.setMsg("Add RuleQualification Success");
        uniformResult.setUser(operator);
        uniformResult.setTarget("AddRuleQualificationService");
        return uniformResultJpa.save(uniformResult);
    }

    @Transactional
    public UniformResult addRuleLine(RuleLine[] ruleLines, Integer id, String operator){
        List<RuleLine> ruleLineList = new ArrayList<RuleLine>();
        for(int i = 0; i < ruleLines.length; i++){
            ruleLineList.add(ruleLines[i]);  //将数组重组为List
        }
        Rule rule = ruleRepository.findOne(id);
        rule.setRuleLineList(ruleLineList);
        UniformResult uniformResult = new UniformResult();
        uniformResult.setState("00");
        uniformResult.setMsg("Add RuleLine Success");
        uniformResult.setUser(operator);
        uniformResult.setTarget("AddRuleLineService");
        return uniformResultJpa.save(uniformResult);
    }

    @Transactional
    public UniformResult addRuleArea(RuleArea[] ruleAreas, Integer id, String operator){
        List<RuleArea> ruleAreaList = new ArrayList<RuleArea>();
        for(int i = 0; i < ruleAreas.length; i++){
            ruleAreaList.add(ruleAreas[i]);  //将数组重组为List
        }
        Rule rule = ruleRepository.findOne(id);
        rule.setRuleAreaList(ruleAreaList);
        UniformResult uniformResult = new UniformResult();
        uniformResult.setState("00");
        uniformResult.setMsg("Add RuleArea Success");
        uniformResult.setUser(operator);
        uniformResult.setTarget("AddRuleAreaService");
        return uniformResultJpa.save(uniformResult);
    }

    @Transactional
    public UniformResult addRuleMaterial(RuleMaterial[] ruleMaterials, Integer id, String operator){
        List<RuleMaterial> ruleMaterialList = new ArrayList<RuleMaterial>();
        for(int i = 0; i < ruleMaterials.length; i++){
            ruleMaterialList.add(ruleMaterials[i]);  //将数组重组为List
        }
        Rule rule = ruleRepository.findOne(id);
        rule.setRuleMaterialList(ruleMaterialList);
        UniformResult uniformResult = new UniformResult();
        uniformResult.setState("00");
        uniformResult.setMsg("Add RuleMaterial Success");
        uniformResult.setUser(operator);
        uniformResult.setTarget("AddRuleMaterialService");
        return uniformResultJpa.save(uniformResult);
    }

    @Transactional
    public UniformResult addRuleProduct(RuleProduct[] ruleProducts, Integer id, String operator){
        List<RuleProduct> ruleProductList = new ArrayList<RuleProduct>();
        for(int i = 0; i < ruleProducts.length; i++){
            ruleProductList.add(ruleProducts[i]);  //将数组重组为List
        }
        Rule rule = ruleRepository.findOne(id);
        rule.setRuleProductList(ruleProductList);
        UniformResult uniformResult = new UniformResult();
        uniformResult.setState("00");
        uniformResult.setMsg("Add RuleProduct Success");
        uniformResult.setUser(operator);
        uniformResult.setTarget("AddRuleProductService");
        return uniformResultJpa.save(uniformResult);
    }

    @Transactional
    public UniformResult addRuleProductBom(RuleProductBom[] ruleProductBoms, Integer id, String operator){
        List<RuleProductBom> ruleProductBomList = new ArrayList<RuleProductBom>();
        for(int i = 0; i < ruleProductBoms.length; i++){
            ruleProductBomList.add(ruleProductBoms[i]);  //将数组重组为List
        }
        Rule rule = ruleRepository.findOne(id);
        rule.setRuleProductBomList(ruleProductBomList);
        UniformResult uniformResult = new UniformResult();
        uniformResult.setState("00");
        uniformResult.setMsg("Add RuleProductBom Success");
        uniformResult.setUser(operator);
        uniformResult.setTarget("AddRuleProductBomService");
        return uniformResultJpa.save(uniformResult);
    }


}
