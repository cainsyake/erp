package bobo.erp.service.running;

import bobo.erp.domain.state.BaseState;
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
public class GetSubBaseStateService {
    private Logger logger = LoggerFactory.getLogger(GetSubBaseStateService.class);

    @Autowired
    private GetTeachClassInfoService getTeachClassInfoService;

    @Autowired
    private TeachClassInfoRepository teachClassInfoRepository;
    @Autowired
    private SubUserInfoRepository subUserInfoRepository;
    @Autowired
    private UserRepository userRepository;



    public BaseState getSubBaseState(String nowUserName){
        SubUserInfo subUserInfo = subUserInfoRepository.findBySubUserName(nowUserName);
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(nowUserName);
        if(subUserInfo.getRunningState() == null){
            return null;
        }
        return subUserInfo.getRunningState().getBaseState();
    }
}
