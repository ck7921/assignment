package assignment.basket;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class BasketItemImpl implements BasketItem {

    private String displayName;
    private String itemClass;
    private BigDecimal salesPrice;
    private BigDecimal discount;
    private BigDecimal total;
    private int quantity;
    private List<BasketItem> subItems = new LinkedList<>();
    private List<Discount> discounts = new LinkedList<>();
    private int stampsUsed;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    @Override
    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    @Override
    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Override
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }


    @Override
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public List<BasketItem> getSubItems() {
        return subItems;
    }

    @Override
    public List<Discount> getDiscounts() {
        return discounts;
    }

    @Override
    public int getStampsUsed() {
        return stampsUsed;
    }

    public void setStampsUsed(int stampsUsed) {
        this.stampsUsed = stampsUsed;
    }
}
