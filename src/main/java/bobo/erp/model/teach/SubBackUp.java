package bobo.erp.model.teach;

import bobo.erp.entity.state.RunningState;
import bobo.erp.entity.teach.SubUserInfo;
import bobo.erp.repository.teach.SubUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bobo on 2017/10/12.
 */
@Service
public class SubBackUp {
    @Autowired
    private SubUserInfoRepository subUserInfoRepository;

    @Transactional
    public boolean yearBackUp(String username, RunningState runningState){
        try{
            SubUserInfo subUserInfo = subUserInfoRepository.findBySubUserName(username);    //根据用户名提取subUserInfo
            subUserInfo.setRunningStateStartYear(runningState); //设置年初备份
            subUserInfo.setRunningStateStartQuarter(runningState);  //设置季初备份
            return true;   //备份成功返回 true
        }catch (Exception e){
            return false;   //备份失败则返回 false
        }
    }

    @Transactional
    public boolean quarterBackUp(String username, RunningState runningState){
        try {
            SubUserInfo subUserInfo = subUserInfoRepository.findBySubUserName(username);    //根据用户名提取subUserInfo
            subUserInfo.setRunningStateStartQuarter(runningState);  //设置季初备份
            return true;   //备份成功返回 true
        }catch (Exception e){
            return false;   //备份失败则返回 false
        }
    }

    @Transactional
    public RunningState rollback(String username, int type){
        try {
            SubUserInfo subUserInfo = subUserInfoRepository.findBySubUserName(username);    //根据用户名提取subUserInfo
            RunningState runningState = subUserInfo.getRunningState();
            if (type == 0){
                runningState = subUserInfo.getRunningStateStartYear();
            }else if (type == 1){
                runningState = subUserInfo.getRunningStateStartQuarter();
            }else {
                runningState.getBaseState().setMsg("Rollback ERROR");
            }
            return runningState;
        }catch (Exception e){
            return null;   //备份失败则返回 null
        }
    }
}
