package bobo.erp.service.teach;

import bobo.erp.entity.rule.Rule;
import bobo.erp.entity.rule.RuleArea;
import bobo.erp.entity.rule.RuleProduct;
import bobo.erp.entity.state.marketing.AdvertisingState;
import bobo.erp.entity.teach.FileInfo;
import bobo.erp.entity.teach.TeachClassInfo;
import bobo.erp.service.running.GetTeachClassInfoService;
import bobo.erp.service.running.GetTeachClassRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Integer AdReportGeneration(String name){
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(name);
        Integer time = teachClassInfo.getTime();
        Rule rule = getTeachClassRuleService.getTeachClassRule(name);
        RuleProduct ruleProduct = rule.getRuleProduct();
        RuleArea ruleArea = rule.getRuleMarket();
        List<String> productNameList = new ArrayList<String>();
        List<String> areaNameList = new ArrayList<String>();
        String title = "AD" + time + name;
        String fileName = title + ".xlsx";
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(fileName);
        fileInfo.setState(1);
        fileInfo.setType("ad");
        teachClassInfo.getFileInfoList().add(fileInfo);

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
        if (ruleArea.getMarket1Name() != ""){
            areaNameList.add(ruleArea.getMarket1Name());
        }
        if (ruleArea.getMarket2Name() != ""){
            areaNameList.add(ruleArea.getMarket2Name());
        }
        if (ruleArea.getMarket3Name() != ""){
            areaNameList.add(ruleArea.getMarket3Name());
        }
        if (ruleArea.getMarket4Name() != ""){
            areaNameList.add(ruleArea.getMarket4Name());
        }
        if (ruleArea.getMarket5Name() != ""){
            areaNameList.add(ruleArea.getMarket5Name());
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
