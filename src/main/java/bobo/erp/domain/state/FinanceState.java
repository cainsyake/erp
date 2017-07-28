package bobo.erp.domain.state;

import bobo.erp.domain.state.finance.DebtState;
import bobo.erp.domain.state.finance.DuesState;
import bobo.erp.domain.state.finance.FinancialStatement;
import bobo.erp.domain.state.finance.ReceivableState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by 59814 on 2017/7/25.
 */
@Entity
public class FinanceState {
    public FinanceState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer cashAmount;     //现金金额

    @OneToOne(mappedBy = "financeState")
    private RunningState runningState;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "marketing_state_id")
    private List<DebtState> debtStateList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "marketing_state_id")
    private List<ReceivableState> receivableStateList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "marketing_state_id")
    private List<DuesState> duesStateList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "marketing_state_id")
    private List<FinancialStatement> financialStatementList;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Integer cashAmount) {
        this.cashAmount = cashAmount;
    }

    @JsonBackReference
    public RunningState getRunningState() {
        return runningState;
    }

    @JsonBackReference
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
