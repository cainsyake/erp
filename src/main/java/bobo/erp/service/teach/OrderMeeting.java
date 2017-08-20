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
                    if (!productCollatorList.isEmpty()){
                        List<Integer> orderList = new ArrayList<Integer>();
                        for (int k = 0; k < productOrderList.size(); k++){
                            orderList.add(productOrderList.get(k).getMarketOrderId());  //添加订单ID至排序器订单List
                        }

                        List<SortResult> sortResultList = sort(advertisingStateHashMap, i + 1, j + 1, rule.getRuleParam().getParamAdvertisingMinFee());

                    }
                }
            }
            areaCollatorList.add(areaCollator);
        }
        collator.setAreaCollatorList(areaCollatorList);

        return teachClassInfo;
    }

    public List<SortResult> sort(HashMap<String, AdvertisingState> advertisingStateHashMap, Integer area, Integer product, Integer minAd){
        List<SortResult> sortResultList = new ArrayList<SortResult>();
        for (int i = 0; i < advertisingStateHashMap.size(); i++){
            int tempMax = 0;
            int round = 0;
            String maxName = "";
            Iterator iterator = advertisingStateHashMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry)iterator.next();
                String username = (String)entry.getKey();
                AdvertisingState advertisingState = (AdvertisingState)entry.getValue();

                if (area == 1 && product == 1){
                    int adValue = advertisingState.getAd1();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 1 && product == 2){
                    int adValue = advertisingState.getAd2();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 1 && product == 3){
                    int adValue = advertisingState.getAd3();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 1 && product == 4){
                    int adValue = advertisingState.getAd4();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 1 && product == 5){
                    int adValue = advertisingState.getAd5();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                //Area2
                if (area == 2 && product == 1){
                    int adValue = advertisingState.getAd6();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 2 && product == 2){
                    int adValue = advertisingState.getAd7();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 2 && product == 3){
                    int adValue = advertisingState.getAd8();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 2 && product == 4){
                    int adValue = advertisingState.getAd9();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 2 && product == 5){
                    int adValue = advertisingState.getAd10();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                //Area3
                if (area == 3 && product == 1){
                    int adValue = advertisingState.getAd11();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 3 && product == 2){
                    int adValue = advertisingState.getAd12();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 3 && product == 3){
                    int adValue = advertisingState.getAd13();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 3 && product == 4){
                    int adValue = advertisingState.getAd14();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 3 && product == 5){
                    int adValue = advertisingState.getAd15();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                //Area4
                if (area == 4 && product == 1){
                    int adValue = advertisingState.getAd16();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 4 && product == 2){
                    int adValue = advertisingState.getAd17();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 4 && product == 3){
                    int adValue = advertisingState.getAd18();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 4 && product == 4){
                    int adValue = advertisingState.getAd19();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 4 && product == 5){
                    int adValue = advertisingState.getAd20();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                //Area5
                if (area == 5 && product == 1){
                    int adValue = advertisingState.getAd21();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 5 && product == 2){
                    int adValue = advertisingState.getAd22();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 5 && product == 3){
                    int adValue = advertisingState.getAd23();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 5 && product == 4){
                    int adValue = advertisingState.getAd24();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }
                if (area == 5 && product == 5){
                    int adValue = advertisingState.getAd25();
                    if (adValue >= minAd){
                        if (adValue >= tempMax){
                            maxName = username;
                            round = (adValue - minAd) / (2 * minAd) + 1;
                            advertisingStateHashMap.remove(username);
                        }
                    }
                }

            }

            if (maxName != ""){
                SortResult sortResult = new SortResult();
                sortResult.setRank(sortResultList.size() + 1);
                sortResult.setRound(round);
                sortResult.setUsername(maxName);
                sortResultList.add(sortResult);
            }
        }

        return sortResultList;
    }

}
