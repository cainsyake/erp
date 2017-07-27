package bobo.erp.domain.state.marketing;

import bobo.erp.domain.state.MarketingState;
import bobo.erp.domain.state.dev.MarketDevState;

import javax.persistence.*;

/**
 * Created by 59814 on 2017/7/27.
 */
@Entity
public class AdvertisingState {
    public AdvertisingState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer year;       //年份
    private Integer bidCost;    //标书费

    /**
     * ad0为总广告费(含标书费)
     * ad1-ad5为‘市场1’的‘产品1-5’的广告费
     * ad6-ad25以此类推
     * ad26-ad30为‘市场1-5’的自广告合计
     */
    private Integer ad0;
    private Integer ad1;
    private Integer ad2;
    private Integer ad3;
    private Integer ad4;
    private Integer ad5;
    private Integer ad6;
    private Integer ad7;
    private Integer ad8;
    private Integer ad9;
    private Integer ad10;
    private Integer ad11;
    private Integer ad12;
    private Integer ad13;
    private Integer ad14;
    private Integer ad15;
    private Integer ad16;
    private Integer ad17;
    private Integer ad18;
    private Integer ad19;
    private Integer ad20;
    private Integer ad21;
    private Integer ad22;
    private Integer ad23;
    private Integer ad24;
    private Integer ad25;
    private Integer ad26;
    private Integer ad27;
    private Integer ad28;
    private Integer ad29;
    private Integer ad30;

    @ManyToOne
    private MarketingState marketingState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getBidCost() {
        return bidCost;
    }

    public void setBidCost(Integer bidCost) {
        this.bidCost = bidCost;
    }

    public Integer getAd0() {
        return ad0;
    }

    public void setAd0(Integer ad0) {
        this.ad0 = ad0;
    }

    public Integer getAd1() {
        return ad1;
    }

    public void setAd1(Integer ad1) {
        this.ad1 = ad1;
    }

    public Integer getAd2() {
        return ad2;
    }

    public void setAd2(Integer ad2) {
        this.ad2 = ad2;
    }

    public Integer getAd3() {
        return ad3;
    }

    public void setAd3(Integer ad3) {
        this.ad3 = ad3;
    }

    public Integer getAd4() {
        return ad4;
    }

    public void setAd4(Integer ad4) {
        this.ad4 = ad4;
    }

    public Integer getAd5() {
        return ad5;
    }

    public void setAd5(Integer ad5) {
        this.ad5 = ad5;
    }

    public Integer getAd6() {
        return ad6;
    }

    public void setAd6(Integer ad6) {
        this.ad6 = ad6;
    }

    public Integer getAd7() {
        return ad7;
    }

    public void setAd7(Integer ad7) {
        this.ad7 = ad7;
    }

    public Integer getAd8() {
        return ad8;
    }

    public void setAd8(Integer ad8) {
        this.ad8 = ad8;
    }

    public Integer getAd9() {
        return ad9;
    }

    public void setAd9(Integer ad9) {
        this.ad9 = ad9;
    }

    public Integer getAd10() {
        return ad10;
    }

    public void setAd10(Integer ad10) {
        this.ad10 = ad10;
    }

    public Integer getAd11() {
        return ad11;
    }

    public void setAd11(Integer ad11) {
        this.ad11 = ad11;
    }

    public Integer getAd12() {
        return ad12;
    }

    public void setAd12(Integer ad12) {
        this.ad12 = ad12;
    }

    public Integer getAd13() {
        return ad13;
    }

    public void setAd13(Integer ad13) {
        this.ad13 = ad13;
    }

    public Integer getAd14() {
        return ad14;
    }

    public void setAd14(Integer ad14) {
        this.ad14 = ad14;
    }

    public Integer getAd15() {
        return ad15;
    }

    public void setAd15(Integer ad15) {
        this.ad15 = ad15;
    }

    public Integer getAd16() {
        return ad16;
    }

    public void setAd16(Integer ad16) {
        this.ad16 = ad16;
    }

    public Integer getAd17() {
        return ad17;
    }

    public void setAd17(Integer ad17) {
        this.ad17 = ad17;
    }

    public Integer getAd18() {
        return ad18;
    }

    public void setAd18(Integer ad18) {
        this.ad18 = ad18;
    }

    public Integer getAd19() {
        return ad19;
    }

    public void setAd19(Integer ad19) {
        this.ad19 = ad19;
    }

    public Integer getAd20() {
        return ad20;
    }

    public void setAd20(Integer ad20) {
        this.ad20 = ad20;
    }

    public Integer getAd21() {
        return ad21;
    }

    public void setAd21(Integer ad21) {
        this.ad21 = ad21;
    }

    public Integer getAd22() {
        return ad22;
    }

    public void setAd22(Integer ad22) {
        this.ad22 = ad22;
    }

    public Integer getAd23() {
        return ad23;
    }

    public void setAd23(Integer ad23) {
        this.ad23 = ad23;
    }

    public Integer getAd24() {
        return ad24;
    }

    public void setAd24(Integer ad24) {
        this.ad24 = ad24;
    }

    public Integer getAd25() {
        return ad25;
    }

    public void setAd25(Integer ad25) {
        this.ad25 = ad25;
    }

    public Integer getAd26() {
        return ad26;
    }

    public void setAd26(Integer ad26) {
        this.ad26 = ad26;
    }

    public Integer getAd27() {
        return ad27;
    }

    public void setAd27(Integer ad27) {
        this.ad27 = ad27;
    }

    public Integer getAd28() {
        return ad28;
    }

    public void setAd28(Integer ad28) {
        this.ad28 = ad28;
    }

    public Integer getAd29() {
        return ad29;
    }

    public void setAd29(Integer ad29) {
        this.ad29 = ad29;
    }

    public Integer getAd30() {
        return ad30;
    }

    public void setAd30(Integer ad30) {
        this.ad30 = ad30;
    }

    public MarketingState getMarketingState() {
        return marketingState;
    }

    public void setMarketingState(MarketingState marketingState) {
        this.marketingState = marketingState;
    }
}
