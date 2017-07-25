package bobo.erp.domain.state.finance;

import bobo.erp.domain.state.FinanceState;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/25.
 */
@Entity
public class ReceivableState {
    public ReceivableState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer accountPeriod;  //应收账期
    private Integer amounts;        //金额

    @ManyToOne
    private FinanceState financeState;

    public FinanceState getFinanceState() {
        return financeState;
    }

    public void setFinanceState(FinanceState financeState) {
        this.financeState = financeState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(Integer accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    public Integer getAmounts() {
        return amounts;
    }

    public void setAmounts(Integer amounts) {
        this.amounts = amounts;
    }
}
