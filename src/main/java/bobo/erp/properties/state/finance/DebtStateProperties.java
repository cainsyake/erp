package bobo.erp.properties.state.finance;

import bobo.erp.domain.state.FinanceState;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 59814 on 2017/7/25.
 */
@Component
@ConfigurationProperties(prefix = "debtState")
public class DebtStateProperties {
    public DebtStateProperties() {
    }

    private Integer debtType;           //负债类型
    private Integer repaymentPeriod;    //还款期
    private Integer amounts;            //金额
    private FinanceState financeState;

    public Integer getDebtType() {
        return debtType;
    }

    public void setDebtType(Integer debtType) {
        this.debtType = debtType;
    }

    public Integer getRepaymentPeriod() {
        return repaymentPeriod;
    }

    public void setRepaymentPeriod(Integer repaymentPeriod) {
        this.repaymentPeriod = repaymentPeriod;
    }

    public Integer getAmounts() {
        return amounts;
    }

    public void setAmounts(Integer amounts) {
        this.amounts = amounts;
    }

    public FinanceState getFinanceState() {
        return financeState;
    }

    public void setFinanceState(FinanceState financeState) {
        this.financeState = financeState;
    }
}
