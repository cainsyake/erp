package bobo.erp.service.teach;

import bobo.erp.domain.collator.AreaCollator;
import bobo.erp.domain.collator.Collator;
import bobo.erp.domain.collator.ProductCollator;
import bobo.erp.domain.collator.SortResult;
import bobo.erp.domain.market.MarketOrder;
import bobo.erp.domain.rule.Rule;
import bobo.erp.domain.state.RunningState;
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

        collator.setProductNum(productNum);     //设置产品数量
        collator.setAreaQuantity(areaQuantity);     //设置区域数量
        collator.setSameTimeOpenQuantity(rule.getRuleParam().getParamMarketSametimeOpenNum());      //设置同开数量

        List<Integer> openAreaList = new ArrayList<Integer>();
        for (int i = 0 ; i < rule.getRuleParam().getParamMarketSametimeOpenNum(); i++){
            openAreaList.add( i + 1);
        }
        collator.setOpenAreaList(openAreaList);     //设置初始开放区域List

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
                        for (int k = 0; k < productOrderList.size(); k++){
                            orderList.add(productOrderList.get(k).getMarketOrderId());  //添加订单ID至排序器订单List
                        }
                        List<SortResult> sortResultList = sort(advertisingStateHashMap, i + 1, j + 1, rule.getRuleParam().getParamAdvertisingMinFee());
                        if (!sortResultList.isEmpty()){
                            productCollator.setOpenUser(0);     //如果排序结果List非空，初始化产品排序器的用户排位为0
                        }
                        productCollator.setOrderIdList(orderList);
                        productCollator.setSortResultList(sortResultList);

                        Date time = new Date();
                        productCollator.setTime(time);
                    }else {
                        //目标区域-产品无订单的业务处理
                        List<Integer> orderList = new ArrayList<Integer>();
                        productCollator.setOrderIdList(orderList);
                        //无需设置排序结果List
                    }
                    productCollatorList.add(productCollator);
                }
                areaCollator.setProductCollatorList(productCollatorList);   //设置产品排序器List
                areaCollator.setOpenProduct(0);     //设置初始开放产品为0
            }else {
                //当目标区域无订单时，设置空的产品排序器
                List<ProductCollator> productCollatorList = new ArrayList<ProductCollator>();
                areaCollator.setProductCollatorList(productCollatorList);
            }
            areaCollatorList.add(areaCollator);
        }

        collator.setAreaCollatorList(areaCollatorList);
        teachClassInfo.setCollator(collator);
        teachClassInfo.setOrderMeetingState(1);     //设置选单会状态为 正在进行中
        //TODO 还需要添加初始开放区域和初始开放产品 非空的产品排序器还需要添加默认用户排位(0)

        //测试代码 开始
        System.out.println("测试节点，排序器工作结束");
        //测试代码 结束

        return teachClassInfo;
    }

    @Transactional
    public TeachClassInfo endOrderMeeting(String username){
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        teachClassInfo.setOrderMeetingState(2);
        return teachClassInfo;
    }


    public List<SortResult> sort(HashMap<String, AdvertisingState> advertisingStateHashMap, Integer area, Integer product, Integer minAd){
        List<SortResult> sortResultList = new ArrayList<SortResult>();
        HashMap<String, AdvertisingState> localMap = new HashMap<String, AdvertisingState>();
        localMap.putAll(advertisingStateHashMap);

        int size = localMap.size();
        for (int i = 0; i < size; i++){
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

    @Transactional
    public TeachClassInfo changeTime(String username){
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(username);
        //TODO 完成竞单模块后需要增加对竞单会状态的检验
        if (teachClassInfo.getOrderMeetingState() == 2){
            teachClassInfo.setTime(teachClassInfo.getTime() + 1);
            teachClassInfo.setOrderMeetingState(0);
            teachClassInfo.setBidMeetingState(0);
        }
        return teachClassInfo;
    }

    @Transactional
    public TeachClassInfo nextArea(String username, Integer id){
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        int maxOpen = 0;
        List<Integer> openAreaList = teachClassInfo.getCollator().getOpenAreaList();
        for (Integer areaId : openAreaList){
            if (areaId > maxOpen){
                maxOpen = areaId;
            }
        }
        AreaCollator areaCollatorClose = teachClassInfo.getCollator().getAreaCollatorList().get(id - 1);
        areaCollatorClose.setState(2);  //关闭该区域排序器
        Iterator<Integer> iterator = openAreaList.iterator();
        while (iterator.hasNext()){
            int tempId = iterator.next();
            if (tempId == id){
                iterator.remove();  //从开放区域List中删除此区域ID
            }
        }
        if (maxOpen < teachClassInfo.getCollator().getAreaQuantity()){
            openAreaList.add(maxOpen + 1);
            AreaCollator areaCollatorNew = teachClassInfo.getCollator().getAreaCollatorList().get(maxOpen);
            areaCollatorNew.setState(1);    //打开新的区域排序器
            areaCollatorNew.setOpenProduct(0);  //设置新的区域排序器的开放产品ID为0
        }
        return teachClassInfo;
    }

    @Transactional
    public TeachClassInfo nextProduct(String username, Integer id){
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        AreaCollator areaCollator = teachClassInfo.getCollator().getAreaCollatorList().get(id - 1);
        int next = areaCollator.getOpenProduct();
        if (next < teachClassInfo.getCollator().getProductNum()){
            if (next != 0){
                areaCollator.getProductCollatorList().get(next - 1).setState(2);    //关闭当前产品排序器
            }
            ProductCollator productCollator = areaCollator.getProductCollatorList().get(next);  //打开新的产品排序器
            productCollator.setState(1);
            productCollator.setOpenUser(0); //设置新的产品排序器的用户排位为0
            areaCollator.setOpenProduct(next + 1);  //设置该区域排序器的开放产品ID为新的产品
        }else {
            //当前产品是最后一个产品 执行切换区域操作
            areaCollator.getProductCollatorList().get(next - 1).setState(2);    //关闭当前产品排序器
            nextArea(username, id);
        }
        return teachClassInfo;
    }

    @Transactional
    public TeachClassInfo nextUser(String username, Integer id){
//        System.out.println("测试 接收到教学班：" + username + " 的切换用户请求");
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(username);
        Rule rule = getTeachClassRuleService.getTeachClassRule(username);
        AreaCollator areaCollator = teachClassInfo.getCollator().getAreaCollatorList().get(id - 1);
        ProductCollator productCollator = areaCollator.getProductCollatorList().get(areaCollator.getOpenProduct() - 1); //打开产品排序器
        List<SortResult> sortResultList = productCollator.getSortResultList();
        int check = 0;  //检查全体用户的round
        for (SortResult sortResult : sortResultList){
            if (sortResult.getRound() > 0){
                check++;
            }
        }
        if (check == 0){
            //全部用户的round均为0，调用切换产品方法
            return nextProduct(username, id);
        }
        productCollator = nextUserOperate(productCollator, rule);
        return teachClassInfo;
    }

    @Transactional
    public ProductCollator nextUserOperate(ProductCollator productCollator, Rule rule){
        int next = productCollator.getOpenUser();
        List<SortResult> sortResultList = productCollator.getSortResultList();
        int n = 0;
        for (SortResult sortResult : sortResultList){
            if (next == sortResultList.size()){
                productCollator.setOpenUser(0);
                return nextUserOperate(productCollator, rule);
            }
            if (n == next){
                if (sortResult.getRound() > 0){
                    productCollator.setOpenUser(next + 1);
                    sortResult.setRound(sortResult.getRound() - 1);
                    long currentTime = System.currentTimeMillis();
                    currentTime += 1000 * rule.getRuleParam().getParamSelectOrderTime();
                    Date time = new Date(currentTime);
                    productCollator.setTime(time);
                }else {
                    next++;
                }
            }
            n++;
        }
        return productCollator;
    }

    @Transactional
    public List<MarketOrder> getOrderList(String username, Integer area, Integer product){
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(username);
        return marketOrderRepository.findByMarketSeriesIdAndOrderYearAndOrderAreaAndOrderProduct(teachClassInfo.getMarketSeriesId(), teachClassInfo.getTime(), area, product);
    }

    @Transactional
    public TeachClassInfo getOrder(String username, Integer area, Integer product, Integer id){
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(username);
        RunningState runningState = new RunningState();
        List<SubUserInfo> subUserInfoList = teachClassInfo.getSubUserInfoList();
        for (SubUserInfo subUserInfo : subUserInfoList){
            System.out.println("测试，SUB名：" + subUserInfo.getSubUserName() + "  参数名：" + username);
            if(subUserInfo.getSubUserName().equals(username)){
                System.out.println("比对成功");
                runningState = subUserInfo.getRunningState();
            }
        }
        Collator collator = teachClassInfo.getCollator();
        List<AreaCollator> areaCollatorList = collator.getAreaCollatorList();
        ProductCollator productCollator =areaCollatorList.get(area - 1).getProductCollatorList().get(product - 1);
        List<Integer> orderIdList = productCollator.getOrderIdList();
        Iterator<Integer> iterator = orderIdList.iterator();
        while (iterator.hasNext()){
            Integer orderId = iterator.next();
            if (orderId.intValue() == id.intValue()){
                iterator.remove();      //从orderIdList中删除此订单ID
            }
        }
        MarketOrder marketOrder = marketOrderRepository.findOne(id);
        OrderState orderState = new OrderState();
        orderState.setOrderId(id);
        orderState.setYear(marketOrder.getOrderYear());
        orderState.setArea(marketOrder.getOrderArea());
        orderState.setTotalPrice(marketOrder.getOrderTotalPrice());
        orderState.setTypeId(marketOrder.getOrderProduct());
        orderState.setQuantity(marketOrder.getOrderQuantity());
        orderState.setDeliveryTime(marketOrder.getOrderDeliveryTime());
        orderState.setAccountPeriod(marketOrder.getOrderAccountPeriod());
        orderState.setQualificate(marketOrder.getOrderQualificate());
        orderState.setExecution(0); //设置订单状态为 未完成
        orderState.setOwner(username);
        runningState.getMarketingState().getOrderStateList().add(orderState);

        nextUser(username, area);
        return teachClassInfo;
    }





}
