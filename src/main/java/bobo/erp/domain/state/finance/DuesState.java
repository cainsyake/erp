package bobo.erp.domain.state.finance;

import bobo.erp.domain.state.FinanceState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/25.
 */
@Entity
public class DuesState {
    public DuesState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;



    private Integer paymentPeriod;  //付款期
    private Integer amounts;        //金额

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

    public Integer getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(Integer paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public Integer getAmounts() {
        return amounts;
    }

    public void setAmounts(Integer amounts) {
        this.amounts = amounts;
    }
}
