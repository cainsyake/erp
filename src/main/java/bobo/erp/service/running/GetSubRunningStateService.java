package bobo.erp.service.running;

import bobo.erp.domain.state.BaseState;
import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.teach.SubUserInfo;
import bobo.erp.domain.teach.TeachClassInfo;
import bobo.erp.repository.UserRepository;
import bobo.erp.repository.teach.SubUserInfoRepository;
import bobo.erp.repository.teach.TeachClassInfoRepository;
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
}
