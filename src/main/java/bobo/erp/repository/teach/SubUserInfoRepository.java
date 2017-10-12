package bobo.erp.repository.teach;

import bobo.erp.entity.teach.SubUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 59814 on 2017/7/23.
 */
public interface SubUserInfoRepository extends JpaRepository<SubUserInfo, Integer> {
    public SubUserInfo findBySubUserName(String subUserName);
}
