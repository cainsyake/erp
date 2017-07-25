package bobo.erp.properties.state;

import bobo.erp.domain.state.RunningState;
import bobo.erp.domain.state.finance.DebtState;
import bobo.erp.domain.state.finance.DuesState;
import bobo.erp.domain.state.finance.FinancialStatement;
import bobo.erp.domain.state.finance.ReceivableState;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 59814 on 2017/7/25.
 */
@Component
@ConfigurationProperties(prefix = "financeState")
public class FinanceStateProperties {
    public FinanceStateProperties() {
    }
    private Integer cashAmount;     //现金金额

    private RunningState runningState;

    private List<DebtState> debtStateList;

    private List<ReceivableState> receivableStateList;

    private List<DuesState> duesStateList;

    private List<FinancialStatement> financialStatementList;

    public Integer getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Integer cashAmount) {
        this.cashAmount = cashAmount;
    }

    public RunningState getRunningState() {
        return runningState;
    }

    public void setRunningState(RunningState runningState) {
        this.runningState = runningState;
    }

    public List<DebtState> getDebtStateList() {
        return debtStateList;
    }

    public void setDebtStateList(List<DebtState> debtStateList) {
        this.debtStateList = debtStateList;
    }

    public List<ReceivableState> getReceivableStateList() {
        return receivableStateList;
    }

    public void setReceivableStateList(List<ReceivableState> receivableStateList) {
        this.receivableStateList = receivableStateList;
    }

    public List<DuesState> getDuesStateList() {
        return duesStateList;
    }

    public void setDuesStateList(List<DuesState> duesStateList) {
        this.duesStateList = duesStateList;
    }

    public List<FinancialStatement> getFinancialStatementList() {
        return financialStatementList;
    }

    public void setFinancialStatementList(List<FinancialStatement> financialStatementList) {
        this.financialStatementList = financialStatementList;
    }
}
