package bobo.erp.repository.teach;

import bobo.erp.domain.teach.TeachClassInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 59814 on 2017/7/23.
 */
public interface TeachClassInfoRepository extends JpaRepository<TeachClassInfo, Integer> {
    public List<TeachClassInfo>  findByUserId(Integer userId);


    public  TeachClassInfo findByTeachClassName(String name);
}
