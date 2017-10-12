package bobo.erp.model.running;

import bobo.erp.entity.rule.Rule;
import bobo.erp.entity.teach.TeachClassInfo;
import bobo.erp.repository.rule.RuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 59814 on 2017/7/30.
 */
@Service
public class GetTeachClassRuleService {
    private Logger logger = LoggerFactory.getLogger(GetTeachClassRuleService.class);

    @Autowired
    private GetTeachClassInfoService getTeachClassInfoService;

    @Autowired
    private RuleRepository ruleRepository;

    @Transactional
    public Rule getTeachClassRule(String username){
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(username);
        Rule rule = ruleRepository.findOne(teachClassInfo.getRuleId());
//        logger.info("查询子用户：{} 教学班：{}的规则", username, teachClassInfo.getTeachClassName());
        return rule;
    }
}
