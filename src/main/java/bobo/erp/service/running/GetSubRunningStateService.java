package bobo.erp.service.running;

import bobo.erp.entity.state.RunningState;
import bobo.erp.entity.teach.SubUserInfo;
import bobo.erp.repository.teach.SubUserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 59814 on 2017/7/28.
 */
@Service
public class GetSubRunningStateService {
    private Logger logger = LoggerFactory.getLogger(GetSubRunningStateService.class);

    @Autowired
    private SubUserInfoRepository subUserInfoRepository;

    public RunningState getSubRunningState(String username){
        SubUserInfo subUserInfo = subUserInfoRepository.findBySubUserName(username);
//        logger.info("获取用户：{}的RunningState", username);
        return subUserInfo.getRunningState();
    }

    public SubUserInfo getSubUserInfo(String username){
        return subUserInfoRepository.findBySubUserName(username);
    }
}
