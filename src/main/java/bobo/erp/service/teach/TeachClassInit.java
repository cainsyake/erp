package bobo.erp.service.teach;

import bobo.erp.controller.MarketController;
import bobo.erp.domain.User;
import bobo.erp.domain.teach.SubUserInfo;
import bobo.erp.domain.teach.TeachClassInfo;
import bobo.erp.repository.UserRepository;
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
    private GetUserInfo getUserInfo;

    @Transactional
    public String teachClassInit(TeachClassInfo teachClassInfo, String nowUserName, String initPassword){
        List<TeachClassInfo> checkList = teachClassInfoRepository.findByUserId(getUserInfo.getUserInfoSecure(nowUserName).get(0).getId());
        if( checkList.isEmpty() ){
            logger.info("初始化教学班 :{}", nowUserName);
            List<SubUserInfo> subUserInfoList = new ArrayList<SubUserInfo>();

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
