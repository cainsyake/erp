package bobo.erp.properties.state.finance;

import bobo.erp.domain.state.FinanceState;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 59814 on 2017/7/25.
 */
@Component
@ConfigurationProperties(prefix = "duesState")
public class DuesStateProperties {
    public DuesStateProperties() {
    }

    private Integer paymentPeriod;  //付款期
    private Integer amounts;        //金额
    private FinanceState financeState;

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

    public FinanceState getFinanceState() {
        return financeState;
    }

    public void setFinanceState(FinanceState financeState) {
        this.financeState = financeState;
    }
}

