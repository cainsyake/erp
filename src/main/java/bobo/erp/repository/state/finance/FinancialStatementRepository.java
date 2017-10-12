package bobo.erp.repository.state.finance;

import bobo.erp.entity.state.finance.FinancialStatement;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 59814 on 2017/7/25.
 */
public interface FinancialStatementRepository extends JpaRepository<FinancialStatement, Integer> {
}
