package bobo.erp.domain.state.finance;

import bobo.erp.domain.state.FinanceState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/25.
 */
@Entity
public class DebtState {
    public DebtState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer debtType;           //负债类型  1-短期 2-长期
    private Integer repaymentPeriod;    //还款期
    private Integer amounts;            //金额

    @ManyToOne
    private FinanceState financeState;

    @JsonBackReference
    public FinanceState getFinanceState() {
        return financeState;
    }

    @JsonBackReference
    public void setFinanceState(FinanceState financeState) {
        this.financeState = financeState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
}
