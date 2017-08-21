package bobo.erp.service.teach;

import bobo.erp.domain.collator.AreaCollator;
import bobo.erp.domain.collator.Collator;
import bobo.erp.domain.collator.ProductCollator;
import bobo.erp.domain.collator.SortResult;
import bobo.erp.domain.market.MarketOrder;
import bobo.erp.domain.rule.Rule;
import bobo.erp.domain.state.marketing.AdvertisingState;
import bobo.erp.domain.state.marketing.OrderState;
import bobo.erp.domain.teach.SubUserInfo;
import bobo.erp.domain.teach.TeachClassInfo;
import bobo.erp.repository.market.MarketOrderRepository;
import bobo.erp.service.running.GetTeachClassInfoService;
import bobo.erp.service.running.GetTeachClassRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by 59814 on 2017/8/20.
 */
@Service
public class OrderMeeting {
    @Autowired
    private GetTeachClassInfoService getTeachClassInfoService;

    @Autowired
    private GetTeachClassRuleService getTeachClassRuleService;

    @Autowired
    private MarketOrderRepository marketOrderRepository;

    @Transactional
    public TeachClassInfo startOrderMeeting(String username){
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);

        List<SubUserInfo> subUserInfoList = teachClassInfo.getSubUserInfoList();
        HashMap<String, AdvertisingState> advertisingStateHashMap = new HashMap<String, AdvertisingState>();
        for (SubUserInfo subUserInfo : subUserInfoList){
            if (subUserInfo.getRunningState().getBaseState().getState() != -2){
                List<AdvertisingState> advertisingStateList = subUserInfo.getRunningState().getMarketingState().getAdvertisingStateList();
                for (AdvertisingState advertisingState : advertisingStateList){
                    if (advertisingState.getYear() == teachClassInfo.getTime()){
                        if (advertisingState.getAd0() > 0){
                            advertisingStateHashMap.put(subUserInfo.getSubUserName(), advertisingState);    //添加用户名和广告状态至Map中
                        }
                    }
                }
            }
        }

        Collator collator = new Collator();
        Integer areaQuantity = 0;
        Integer productNum = 0;
        if(teachClassInfo.getTime() - rule.getRuleMarket().getMarket1DevTime() > 0){
            areaQuantity++;
        }
        if(teachClassInfo.getTime() - rule.getRuleMarket().getMarket2DevTime() > 0){
            areaQuantity++;
        }
        if(teachClassInfo.getTime() - rule.getRuleMarket().getMarket3DevTime() > 0){
            areaQuantity++;
        }
        if(teachClassInfo.getTime() - rule.getRuleMarket().getMarket4DevTime() > 0){
            areaQuantity++;
        }
        if(teachClassInfo.getTime() - rule.getRuleMarket().getMarket5DevTime() > 0){
            areaQuantity++;
        }
        if (rule.getRuleProduct().getProduct1Name() != "" || rule.getRuleProduct().getProduct1Name() != null){
            productNum++;
        }
        if (rule.getRuleProduct().getProduct1Name() != "" || rule.getRuleProduct().getProduct2Name() != null){
            productNum++;
        }
        if (rule.getRuleProduct().getProduct1Name() != "" || rule.getRuleProduct().getProduct3Name() != null){
            productNum++;
        }
        if (rule.getRuleProduct().getProduct1Name() != "" || rule.getRuleProduct().getProduct4Name() != null){
            productNum++;
        }
        if (rule.getRuleProduct().getProduct1Name() != "" || rule.getRuleProduct().getProduct5Name() != null){
            productNum++;
        }

        collator.setAreaQuantity(areaQuantity);     //设置区域数量
        collator.setSameTimeOpenQuantity(rule.getRuleParam().getParamMarketSametimeOpenNum());      //设置同开数量

        List<AreaCollator> areaCollatorList = new ArrayList<AreaCollator>();
        for (int i = 0; i < areaQuantity; i++){
            AreaCollator areaCollator = new AreaCollator();
            areaCollator.setType(i + 1);    //设置区域类型
            areaCollator.setState(-1);  //设置区域开放状态为 未开放
            //当前开放产品ID尚未设置
            List<MarketOrder> areaOrderList = marketOrderRepository.findByMarketSeriesIdAndOrderYearAndOrderArea(teachClassInfo.getMarketSeriesId(), teachClassInfo.getTime(), (i + 1));
            if (!areaOrderList.isEmpty()){
                List<ProductCollator> productCollatorList = new ArrayList<ProductCollator>();
                for (int j = 0; j < productNum; j++){
                    List<MarketOrder> productOrderList = marketOrderRepository.findByMarketSeriesIdAndOrderYearAndOrderAreaAndOrderProduct(teachClassInfo.getMarketSeriesId(), teachClassInfo.getTime(), (i + 1), (j + 1));
                    ProductCollator productCollator = new ProductCollator();
                    productCollator.setType(j + 1);
                    productCollator.setState(-1);
                    //当前开放的排位尚未设置
                    if (!productOrderList.isEmpty()){
                        List<Integer> orderList = new ArrayList<Integer>();
                        //测试代码 开始
                        System.out.println("测试节点，输出区域：" + (i+1) + " 产品：" + (j+1) + " 的订单数量：" + productOrderList.size());
                        //测试代码 结束
                        for (int k = 0; k < productOrderList.size(); k++){
                            orderList.add(productOrderList.get(k).getMarketOrderId());  //添加订单ID至排序器订单List
                        }
                        System.out.println("测试节点，输出区域：" + (i+1) + " 产品：" + (j+1) + " 排序前的adMap长度" + advertisingStateHashMap.size());
                        List<SortResult> sortResultList = sort(advertisingStateHashMap, i + 1, j + 1, rule.getRuleParam().getParamAdvertisingMinFee());
                        //测试代码 开始
                        System.out.println("测试节点，输出区域：" + (i+1) + " 产品：" + (j+1) + " 排序后的adMap长度" + advertisingStateHashMap.size());
                        //测试代码 结束
                        productCollator.setOrderIdList(orderList);
                        productCollator.setSortResultList(sortResultList);
                    }else {
                        //目标区域-产品无订单的业务处理
                        List<Integer> orderList = new ArrayList<Integer>();
                        productCollator.setOrderIdList(orderList);
                        //无需设置排序结果List
                    }
                    productCollatorList.add(productCollator);
                }
                areaCollator.setProductCollatorList(productCollatorList);
            }else {
                //当目标区域无订单时，设置空的产品排序器
                List<ProductCollator> productCollatorList = new ArrayList<ProductCollator>();
                areaCollator.setProductCollatorList(productCollatorList);
            }
            areaCollatorList.add(areaCollator);
        }

        collator.setAreaCollatorList(areaCollatorList);
        teachClassInfo.setCollator(collator);
        teachClassInfo.setOrderMeetingState(1);
        //TODO 还需要添加初始开放区域和初始开放产品

        //测试代码 开始
        System.out.println("测试节点，排序器工作结束");
        //测试代码 结束

        return teachClassInfo;
    }

    @Transactional
    public TeachClassInfo endOrderMeeting(String username){
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);

        //测试代码 开始
        System.out.println("测试节点，时间：" + teachClassInfo.getTime());
        System.out.println("测试节点，选单状态：" + teachClassInfo.getOrderMeetingState());
        //测试代码 结束

        teachClassInfo.setOrderMeetingState(2);

        //TODO 在选单会开发中，直接将时间变更放在选单会结束，等竞单模块开始开发后，需要新编写时间变换方法
        teachClassInfo.setTime(teachClassInfo.getTime() + 1);
        teachClassInfo.setOrderMeetingState(0); //变换年份后选单会状态变回 未开始

        return teachClassInfo;
    }


    public List<SortResult> sort(HashMap<String, AdvertisingState> advertisingStateHashMap, Integer area, Integer product, Integer minAd){
        List<SortResult> sortResultList = new ArrayList<SortResult>();
        HashMap<String, AdvertisingState> localMap = new HashMap<String, AdvertisingState>();
        localMap.putAll(advertisingStateHashMap);

        int size = localMap.size();
        for (int i = 0; i < size; i++){
            // TTTTTTTTTTTTTTTTT
//            System.out.println("测试输出外循环次数：" + (i + 1));
//            System.out.println("测试输出MAP长度：" + advertisingStateHashMap.size());
            //TTTTTTTTTTTTTTTTTT
            int tempMax = 0;
            int round = 0;
            String maxName = "";
            Iterator iterator = localMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry)iterator.next();
                String username = (String)entry.getKey();
                AdvertisingState advertisingState = (AdvertisingState)entry.getValue();
                int adValue = 0;
                if (area == 1 && product == 1){
                    adValue = advertisingState.getAd1();
                }
                if (area == 1 && product == 2){
                    adValue = advertisingState.getAd2();
                }
                if (area == 1 && product == 3){
                    adValue = advertisingState.getAd3();
                }
                if (area == 1 && product == 4){
                    adValue = advertisingState.getAd4();
                }
                if (area == 1 && product == 5){
                    adValue = advertisingState.getAd5();
                }
                if (area == 2 && product == 1){
                    adValue = advertisingState.getAd6();
                }
                if (area == 2 && product == 2){
                    adValue = advertisingState.getAd7();
                }
                if (area == 2 && product == 3){
                    adValue = advertisingState.getAd8();
                }
                if (area == 2 && product == 4){
                    adValue = advertisingState.getAd9();
                }
                if (area == 2 && product == 5){
                    adValue = advertisingState.getAd10();
                }
                if (area == 3 && product == 1){
                    adValue = advertisingState.getAd11();
                }
                if (area == 3 && product == 2){
                    adValue = advertisingState.getAd12();
                }
                if (area == 3 && product == 3){
                    adValue = advertisingState.getAd13();
                }
                if (area == 3 && product == 4){
                    adValue = advertisingState.getAd14();
                }
                if (area == 3 && product == 5){
                    adValue = advertisingState.getAd15();
                }
                if (area == 4 && product == 1){
                    adValue = advertisingState.getAd16();
                }
                if (area == 4 && product == 2){
                    adValue = advertisingState.getAd17();
                }
                if (area == 4 && product == 3){
                    adValue = advertisingState.getAd18();
                }
                if (area == 4 && product == 4){
                    adValue = advertisingState.getAd19();
                }
                if (area == 4 && product == 5){
                    adValue = advertisingState.getAd20();
                }
                if (area == 5 && product == 1){
                    adValue = advertisingState.getAd21();
                }
                if (area == 5 && product == 2){
                    adValue = advertisingState.getAd22();
                }
                if (area == 5 && product == 3){
                    adValue = advertisingState.getAd23();
                }
                if (area == 5 && product == 4){
                    adValue = advertisingState.getAd24();
                }
                if (area == 5 && product == 5){
                    adValue = advertisingState.getAd25();
                }

                if (adValue >= minAd){
                    // TTT
//                    System.out.println("测试 外层编号：" + i + " 子用户:" + username + " 广告额：" + adValue);
//                    System.out.println("测试 当前temp最大广告额：" + tempMax);
                    // TTT
                    if (adValue >= tempMax){
                        maxName = username;
                        tempMax = adValue;
                        round = (adValue - minAd) / (2 * minAd) + 1;
                    }
                }
            }

            if (maxName != ""){
                //TODO 暂未添加对广告额相同，对区域总额进行比较的代码
                SortResult sortResult = new SortResult();
                sortResult.setRank(sortResultList.size() + 1);
                sortResult.setRound(round);
                sortResult.setUsername(maxName);
                sortResultList.add(sortResult);

                localMap.remove(maxName);
            }
        }

        return sortResultList;
    }

}
