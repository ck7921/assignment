package assignment.basket;

import java.math.BigDecimal;

public class DiscountImpl implements Discount {

    private String displayName;
    private BigDecimal discount;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
