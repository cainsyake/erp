package bobo.erp.repository.teach;

import bobo.erp.domain.teach.SubUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 59814 on 2017/7/23.
 */
public interface SubUserInfoRepository extends JpaRepository<SubUserInfo, Integer> {
    public SubUserInfo findBySubUserName(String subUserName);
}
