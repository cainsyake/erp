package bobo.erp.service.running;

import bobo.erp.entity.teach.TeachClassInfo;
import bobo.erp.repository.UserRepository;
import bobo.erp.repository.teach.TeachClassInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 59814 on 2017/7/28.
 */
@Service
public class GetTeachClassInfoService {
    private Logger logger = LoggerFactory.getLogger(GetTeachClassInfoService.class);

    @Autowired
    private TeachClassInfoRepository teachClassInfoRepository;
    @Autowired
    private UserRepository userRepository;

    public TeachClassInfo getTeachClassInfoByUsername(String strIn){
//        logger.info("查询子用户：{}的教学班信息", strIn);
        String[] str_string = strIn.split("\\d");//  \d 为正则表达式表示[0-9]数字
        String  strOut = "";
        for (int index = 0; index < str_string.length; index++)
        {
            strOut += str_string[index];
        }
        Integer userId = userRepository.findByUsername(strOut).get(0).getId();
        List<TeachClassInfo> teachClassInfoList = teachClassInfoRepository.findByUserId(userId);
        if (teachClassInfoList.isEmpty()){
            return null;
        }else {
            return teachClassInfoList.get(0);
        }
    }
}
