package assignment.basket;

import java.math.BigDecimal;
import java.util.List;

class BasketImpl implements Basket {

    private String currencySymbol;
    private BigDecimal total;
    private BigDecimal totalDiscount;
    private List<BasketItem> items;

    @Override
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    @Override
    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    @Override
    public List<BasketItem> getItems() {
        return items;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setItems(List<BasketItem> items) {
        this.items = items;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }
}
