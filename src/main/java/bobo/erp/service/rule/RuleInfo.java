package bobo.erp.service.rule;

import bobo.erp.entity.rule.*;
import bobo.erp.repository.rule.RuleRepository;
import bobo.erp.service.running.GetTeachClassRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bobo on 2017/12/2.
 */
@Service
public class RuleInfo {
    private Logger logger = LoggerFactory.getLogger(AddRule.class);

    @Autowired
    private GetTeachClassRuleService getTeachClassRuleService;

    public RuleFactory getFactoryInfo(Integer type, String username){
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        List<RuleFactory> ruleFactoryList = rule.getRuleFactoryList();
        for (int i = 0; i < rule.getFactoryQuantity(); i++){
            if (ruleFactoryList.get(i).getType() == type){
                return ruleFactoryList.get(i);
            }
        }
        return null;    //异常情况
    }

    public RuleLine getLineInfo(Integer type, String username){
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        List<RuleLine> ruleLineList = rule.getRuleLineList();
        for (int i = 0; i < rule.getLineQuantity(); i++){
            if (ruleLineList.get(i).getType() == type){
                return ruleLineList.get(i);
            }
        }
        return null;    //异常情况
    }

    public RuleQualification getQualificationInfo(Integer type, String username){
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        List<RuleQualification> ruleQualificationList = rule.getRuleQualificationList();
        for (int i = 0; i < rule.getQualificationQuantity(); i++){
            if (ruleQualificationList.get(i).getType() == type){
                return ruleQualificationList.get(i);
            }
        }
        return null;    //异常情况
    }

    public RuleArea getAreaInfo(Integer type, String username){
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        List<RuleArea> ruleAreaList = rule.getRuleAreaList();
        for (int i = 0; i < rule.getAreaQuantity(); i++){
            if (ruleAreaList.get(i).getType() == type){
                return ruleAreaList.get(i);
            }
        }
        return null;    //异常情况
    }

    public RuleMaterial getMaterialInfo(Integer type, String username){
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        List<RuleMaterial> ruleMaterialList = rule.getRuleMaterialList();
        for (int i = 0; i < rule.getMaterialQuantity(); i++){
            if (ruleMaterialList.get(i).getType() == type){
                return ruleMaterialList.get(i);
            }
        }
        return null;    //异常情况
    }

    public RuleProduct getProductInfo(Integer type, String username){
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        List<RuleProduct> ruleProductList = rule.getRuleProductList();
        for (int i = 0; i < rule.getProductQuantity(); i++){
            if (ruleProductList.get(i).getType() == type){
                return ruleProductList.get(i);
            }
        }
        return null;    //异常情况
    }

    public RuleProductBom getProductBomInfo(Integer type, String username){
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        List<RuleProductBom> ruleProductBomList = rule.getRuleProductBomList();
        for (int i = 0; i < rule.getProductQuantity(); i++){
            if (ruleProductBomList.get(i).getType() == type){
                return ruleProductBomList.get(i);
            }
        }
        return null;    //异常情况
    }

    public RuleParam getParamInfo(String username){
        return getTeachClassRuleService.getTeachClassRule(username).getRuleParam();
    }






}
