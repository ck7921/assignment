package assignment.catalog;

import java.math.BigDecimal;

public class ProductImpl {

    private String key;
    private String displayName;
    private BigDecimal salesPrice;
    private BigDecimal salesTaxRateStreet;
    private BigDecimal salesTaxRateIndoor;
    private String itemClass;

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public BigDecimal getSalesTaxRateStreet() {
        return salesTaxRateStreet;
    }

    public BigDecimal getSalesTaxRateIndoor() {
        return salesTaxRateIndoor;
    }

    public String getItemClass() {
        return itemClass;
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

    public void setSalesTaxRateStreet(BigDecimal salesTaxRateStreet) {
        this.salesTaxRateStreet = salesTaxRateStreet;
    }

    public void setSalesTaxRateIndoor(BigDecimal salesTaxRateIndoor) {
        this.salesTaxRateIndoor = salesTaxRateIndoor;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }
}
