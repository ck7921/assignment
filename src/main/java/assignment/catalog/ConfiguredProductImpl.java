package assignment.catalog;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

class ConfiguredProductImpl implements ConfiguredProduct {

    private String key;
    private String displayName;
    private BigDecimal salesPrice;
    private int quantity;
    private String itemClass;
    private List<ConfiguredProduct> subProducts = new LinkedList<>();

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public String getItemClass() {
        return itemClass;
    }

    @Override
    public List<ConfiguredProduct> getSubProducts() {
        return subProducts;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }
}
