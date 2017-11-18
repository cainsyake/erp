package bobo.erp.service.teach;

import bobo.erp.entity.rule.Rule;
import bobo.erp.entity.rule.RuleMarket;
import bobo.erp.entity.rule.RuleProduct;
import bobo.erp.entity.state.marketing.AdvertisingState;
import bobo.erp.entity.teach.TeachClassInfo;
import bobo.erp.service.running.GetTeachClassInfoService;
import bobo.erp.service.running.GetTeachClassRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobo on 2017/11/18.
 */
@Service
public class ReportGenerationService {
    private Logger logger = LoggerFactory.getLogger(ReportGenerationService.class);

    @Autowired
    private GetTeachClassInfoService getTeachClassInfoService;

    @Autowired
    private GetTeachClassRuleService getTeachClassRuleService;

    @Autowired
    private ExcelExportService excelExportService;

    /**
     *
     * @param name 教学班账号
     * @return
     */
    public Integer AdReportGeneration(String name){
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(name);
        Integer time = teachClassInfo.getTime();
        Rule rule = getTeachClassRuleService.getTeachClassRule(name);
        RuleProduct ruleProduct = rule.getRuleProduct();
        RuleMarket ruleMarket = rule.getRuleMarket();
        List<String> productNameList = new ArrayList<String>();
        List<String> areaNameList = new ArrayList<String>();
        String title = name + " 第 " + time + " 年 AD";

        //获取产品名List
        if (ruleProduct.getProduct1Name() != ""){
            productNameList.add(ruleProduct.getProduct1Name());
        }
        if (ruleProduct.getProduct2Name() != ""){
            productNameList.add(ruleProduct.getProduct2Name());
        }
        if (ruleProduct.getProduct3Name() != ""){
            productNameList.add(ruleProduct.getProduct3Name());
        }
        if (ruleProduct.getProduct4Name() != ""){
            productNameList.add(ruleProduct.getProduct4Name());
        }
        if (ruleProduct.getProduct5Name() != ""){
            productNameList.add(ruleProduct.getProduct5Name());
        }
        //获取区域名List
        if (ruleMarket.getMarket1Name() != ""){
            areaNameList.add(ruleMarket.getMarket1Name());
        }
        if (ruleMarket.getMarket2Name() != ""){
            areaNameList.add(ruleMarket.getMarket2Name());
        }
        if (ruleMarket.getMarket3Name() != ""){
            areaNameList.add(ruleMarket.getMarket3Name());
        }
        if (ruleMarket.getMarket4Name() != ""){
            areaNameList.add(ruleMarket.getMarket4Name());
        }
        if (ruleMarket.getMarket5Name() != ""){
            areaNameList.add(ruleMarket.getMarket5Name());
        }

        //遍历子用户的广告
        List<AdvertisingState> advertisingStateList = new ArrayList<AdvertisingState>();
        for (int j = 0; j < teachClassInfo.getTeachClassVolume(); j++){
            List<AdvertisingState> subAdvertisingStateList = teachClassInfo.getSubUserInfoList().get(j).getRunningState().getMarketingState().getAdvertisingStateList();
            for (AdvertisingState advertisingState : subAdvertisingStateList){
                if (time == advertisingState.getYear()){
                    advertisingStateList.add(advertisingState); //把子用户的当年广告装入advertisingStateList
                }
            }
        }

        return excelExportService.exportAdReport(advertisingStateList, productNameList, areaNameList, title);
    }
}
