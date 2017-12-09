package bobo.erp.service.running;

import bobo.erp.entity.common.packing.runPack.RunningStatePacking;
import bobo.erp.entity.state.RunningState;
import bobo.erp.entity.teach.SubUserInfo;
import bobo.erp.entity.teach.TeachClassInfo;
import bobo.erp.repository.teach.SubUserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 59814 on 2017/7/28.
 */
@Service
public class GetSubRunningStateService {
    private Logger logger = LoggerFactory.getLogger(GetSubRunningStateService.class);

    @Autowired
    private SubUserInfoRepository subUserInfoRepository;
    @Autowired
    private GetTeachClassInfoService getTeachClassInfoService;

    public RunningState getSubRunningState(String username){
        SubUserInfo subUserInfo = subUserInfoRepository.findBySubUserName(username);
//        logger.info("获取用户：{}的RunningState", username);
        return subUserInfo.getRunningState();
    }

    public SubUserInfo getSubUserInfo(String username){
        return subUserInfoRepository.findBySubUserName(username);
    }

    public RunningStatePacking getRunningStatePacking(String username){
        RunningStatePacking runningStatePacking = new RunningStatePacking();
        runningStatePacking.runningState = subUserInfoRepository.findBySubUserName(username).getRunningState();
        List<Integer> meetingStateList = new ArrayList<Integer>();
        TeachClassInfo teachClassInfo = getTeachClassInfoService.getTeachClassInfoByUsername(username);
        if (teachClassInfo.getOrderMeetingState() == null){
            meetingStateList.add(0);
        } else {
            meetingStateList.add(teachClassInfo.getOrderMeetingState());
        }
        if (teachClassInfo.getBidMeetingState() == null){
            meetingStateList.add(0);
        } else {
            meetingStateList.add(teachClassInfo.getBidMeetingState());
        }
        runningStatePacking.meetingStateList = meetingStateList;
        return runningStatePacking;
    }
}
