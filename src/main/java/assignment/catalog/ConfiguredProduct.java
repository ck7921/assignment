package assignment.catalog;

import java.math.BigDecimal;
import java.util.List;

public interface ConfiguredProduct {

    String getKey();

    String getDisplayName();

    BigDecimal getSalesPrice();

    int getQuantity();

    String getItemClass();

    List<ConfiguredProduct> getSubProducts();
}
