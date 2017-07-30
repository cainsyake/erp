package bobo.erp.service.running;

import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.marketing.AdvertisingState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 59814 on 2017/7/30.
 */
@Service
public class RunningOperate {
    private Logger logger = LoggerFactory.getLogger(RunningOperate.class);

    @Autowired
    private GetSubRunningStateService getSubRunningStateService;

    public RunningState advertising(AdvertisingState advertisingState, String username){
        RunningState runningState = getSubRunningStateService.getSubRunningState(username);
        advertisingState.setAd26(advertisingState.getAd1()+advertisingState.getAd6()+advertisingState.getAd11()+advertisingState.getAd16()+advertisingState.getAd21());
        advertisingState.setAd27(advertisingState.getAd2()+advertisingState.getAd7()+advertisingState.getAd12()+advertisingState.getAd17()+advertisingState.getAd22());
        advertisingState.setAd28(advertisingState.getAd3()+advertisingState.getAd8()+advertisingState.getAd13()+advertisingState.getAd18()+advertisingState.getAd23());
        advertisingState.setAd29(advertisingState.getAd4()+advertisingState.getAd9()+advertisingState.getAd14()+advertisingState.getAd19()+advertisingState.getAd24());
        advertisingState.setAd30(advertisingState.getAd5()+advertisingState.getAd10()+advertisingState.getAd15()+advertisingState.getAd20()+advertisingState.getAd25());
        advertisingState.setAd0(advertisingState.getAd26()+advertisingState.getAd27()+advertisingState.getAd28()+advertisingState.getAd29()+advertisingState.getAd30());

        Integer balance = runningState.getFinanceState().getCashAmount() - advertisingState.getAd0();
        if (balance < 0){
            runningState.getBaseState().setMsg("现金不足");
            logger.info("现金不足警告");
        }else {
            runningState.getFinanceState().setCashAmount(balance);
            advertisingState.setYear(runningState.getBaseState().getTimeYear());
            runningState.getMarketingState().getAdvertisingStateList().add(advertisingState);
            logger.info("最新余额：{}", runningState.getFinanceState().getCashAmount());
        }
        logger.info("测试广告总额：{}", advertisingState.getAd0());
        return runningState;
    }
}
