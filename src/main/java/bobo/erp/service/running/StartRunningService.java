package bobo.erp.service.running;

import bobo.erp.domain.state.BaseState;
import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.finance.FinancialStatement;
import bobo.erp.domain.teach.SubUserInfo;
import bobo.erp.repository.UserRepository;
import bobo.erp.repository.teach.SubUserInfoRepository;
import bobo.erp.repository.teach.TeachClassInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 59814 on 2017/7/29.
 */
@Service
public class StartRunningService {
    private Logger logger = LoggerFactory.getLogger(StartRunningService.class);

    @Autowired
    private GetTeachClassInfoService getTeachClassInfoService;

    @Autowired
    private TeachClassInfoRepository teachClassInfoRepository;
    @Autowired
    private SubUserInfoRepository subUserInfoRepository;
    @Autowired
    private UserRepository userRepository;

    public RunningState startRunning(String username){
        //利用Hibernate查询出来的对象是持久态,直接set修改属性即可自动保存更改至数据库
        logger.info("用户：{} 开始运营，设置stata=1", username);
        SubUserInfo subUserInfo = subUserInfoRepository.findBySubUserName(username);
        subUserInfo.getRunningState().getBaseState().setState(1);   //设置状态码为1
        subUserInfo.getRunningState().getBaseState().getOperateState().setAd(0);    //设置广告未投放
        FinancialStatement financialStatement = new FinancialStatement();   //开始运营时新建一个财务报表
        financialStatement.setYear(1);
        subUserInfo.getRunningState().getFinanceState().getFinancialStatementList().add(financialStatement);
        //此处不设置Report、OrderMeeting、BigMeeting、LongLoan的状态码，待具体业务执行后才设置，状态码为null
        return subUserInfoRepository.save(subUserInfo).getRunningState();
    }
}
