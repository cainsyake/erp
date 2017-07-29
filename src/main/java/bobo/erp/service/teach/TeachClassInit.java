package bobo.erp.service.teach;

import bobo.erp.controller.MarketController;
import bobo.erp.domain.User;
import bobo.erp.domain.rule.Rule;
import bobo.erp.domain.state.*;
import bobo.erp.domain.state.base.OperateState;
import bobo.erp.domain.state.dev.MarketDevState;
import bobo.erp.domain.state.dev.ProductDevState;
import bobo.erp.domain.state.dev.QualificationDevState;
import bobo.erp.domain.state.factory.LineState;
import bobo.erp.domain.state.finance.DebtState;
import bobo.erp.domain.state.finance.DuesState;
import bobo.erp.domain.state.finance.FinancialStatement;
import bobo.erp.domain.state.finance.ReceivableState;
import bobo.erp.domain.state.marketing.AdvertisingState;
import bobo.erp.domain.state.marketing.OrderState;
import bobo.erp.domain.state.stock.MaterialState;
import bobo.erp.domain.state.stock.ProductState;
import bobo.erp.domain.state.stock.PurchaseState;
import bobo.erp.domain.teach.SubUserInfo;
import bobo.erp.domain.teach.TeachClassInfo;
import bobo.erp.repository.UserRepository;
import bobo.erp.repository.rule.RuleRepository;
import bobo.erp.repository.teach.SubUserInfoRepository;
import bobo.erp.repository.teach.TeachClassInfoRepository;
import bobo.erp.service.GetUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 59814 on 2017/7/24.
 */
@Service
public class TeachClassInit {

    private Logger logger = LoggerFactory.getLogger(TeachClassInit.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeachClassInfoRepository teachClassInfoRepository;

    @Autowired
    private SubUserInfoRepository   subUserInfoRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private GetUserInfo getUserInfo;

    @Transactional
    public String teachClassInit(TeachClassInfo teachClassInfo, String nowUserName, String initPassword){
        List<TeachClassInfo> checkList = teachClassInfoRepository.findByUserId(getUserInfo.getUserInfoSecure(nowUserName).get(0).getId());
        if( checkList.isEmpty() ){
            logger.info("初始化教学班 :{}", nowUserName);
            List<SubUserInfo> subUserInfoList = new ArrayList<SubUserInfo>();
            Rule rule = ruleRepository.findOne(teachClassInfo.getRuleId());

            for(int i =0 ; i < teachClassInfo.getTeachClassVolume(); i++ ){
                User user = new User(); //循环设置子用户信息并存入数据库
                user.setUsername(nowUserName+(1+i));    //子用户登录账号为教师账号+序号
                user.setUserOperator(nowUserName);
                user.setPassword(initPassword);
                user.setUserLevel(3);
                User tempUser = userRepository.save(user);

                SubUserInfo subUserInfo = new SubUserInfo();    //设置SubUserInfo
                subUserInfo.setSubUserName(nowUserName+(1+i));
                subUserInfo.setUserId(tempUser.getId()); //绑定子用户的账号id

                //开始执行State初始化链
                RunningState runningState = new RunningState();
                BaseState baseState = new BaseState();
                baseState.setState(0);
                baseState.setTimeYear(1);
                baseState.setTimeQuarter(0);
                List<OperateState> operateStateList = new ArrayList<OperateState>();
                baseState.setOperateStateList(operateStateList);
                runningState.setBaseState(baseState);   //存入BaseState

                FinanceState financeState = new FinanceState();
                financeState.setCashAmount(rule.getRuleParam().getParamInitialCash());
                List<DebtState>  debtStateList = new ArrayList<DebtState>();
                List<DuesState>  duesStateList = new ArrayList<DuesState>();
                List<ReceivableState>  receivableStateList = new ArrayList<ReceivableState>();
                List<FinancialStatement>  financialStatementList = new ArrayList<FinancialStatement>();
                financeState.setDebtStateList(debtStateList);
                financeState.setDuesStateList(duesStateList);
                financeState.setReceivableStateList(receivableStateList);
                financeState.setFinancialStatementList(financialStatementList);
                runningState.setFinanceState(financeState); //存入FinanceState

                StockState stockState = new StockState();
                List<MaterialState> materialStateList = new ArrayList<MaterialState>();
                List<ProductState> productStateList = new ArrayList<ProductState>();
                List<PurchaseState> purchaseStateList = new ArrayList<PurchaseState>();
                stockState.setMaterialStateList(materialStateList);
                stockState.setProductStates(productStateList);
                stockState.setPurchaseStates(purchaseStateList);
                runningState.setStockState(stockState); //存入StockState

                //由于一个RunningState中可能含有多个FactoryState,所以仅存入该类型List,三级实体由新增厂房时存入
                List<FactoryState> factoryStateList = new ArrayList<FactoryState>();
                runningState.setFactoryStateList(factoryStateList); //存入FactoryState

                DevState devState = new DevState();
                List<QualificationDevState> qualificationDevStateList = new ArrayList<QualificationDevState>();
                List<ProductDevState> productDevStateList = new ArrayList<ProductDevState>();
                List<MarketDevState> marketDevStateList = new ArrayList<MarketDevState>();
                devState.setQualificationDevStateList(qualificationDevStateList);
                devState.setProductDevStateList(productDevStateList);
                devState.setMarketDevStateList(marketDevStateList);
                runningState.setDevState(devState); //存入DevState

                MarketingState marketingState = new MarketingState();
                List<AdvertisingState> advertisingStateList = new ArrayList<AdvertisingState>();
                List<OrderState> orderStateList = new ArrayList<OrderState>();
                marketingState.setAdvertisingStateList(advertisingStateList);
                marketingState.setOrderStateList(orderStateList);
                runningState.setMarketingState(marketingState); //存入MarketingState

                subUserInfo.setRunningState(runningState);  //存入RunningState,结束初始化链
                subUserInfoList.add(subUserInfoRepository.save(subUserInfo));
            }

            User userT = getUserInfo.getUserInfoSecure(nowUserName).get(0);
            Integer teacherId = userT.getId();
            teachClassInfo.setUserId(teacherId);    //绑定教学班的老师id
            teachClassInfo.setSubUserInfoList(subUserInfoList);
            teachClassInfoRepository.save(teachClassInfo);
            return "成功初始化";
        }else {
            logger.info("教学班已初始化，还原后再初始化 :{}", nowUserName);
//            return teachClassReturnOriginal(nowUserName);
            return "请还原后再进行初始化操作";
        }


    }

    @Transactional
    public String teachClassReturnOriginal(String  nowUserName){
        logger.info("执行教学班还原操作，操作者：{}", nowUserName);
        User userT = getUserInfo.getUserInfoSecure(nowUserName).get(0);
        Integer teacherId = userT.getId();
        TeachClassInfo teachClassInfo = teachClassInfoRepository.findByUserId(teacherId).get(0);
        teachClassInfoRepository.delete(teachClassInfo.getTeachClassId());  //删除teachClassInfo
        List<SubUserInfo> subUserInfoList = teachClassInfo.getSubUserInfoList();
        for(SubUserInfo subUserInfo : subUserInfoList){
            Integer userId = subUserInfo.getUserId();
            userRepository.delete(userId);
        }
        return "成功还原 教学班至初始状态";
    }
}
