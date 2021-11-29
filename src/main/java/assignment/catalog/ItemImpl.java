package assignment.catalog;

import java.math.BigDecimal;

public class ItemImpl {

    private String key;
    private String displayName;
    private BigDecimal salesPrice;
    private String itemClass;

    public void setKey(String key) {
        this.key = key;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public String getItemClass() {
        return itemClass;
    }
}
