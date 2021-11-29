package assignment.basket;

import java.math.BigDecimal;
import java.util.List;

public interface BasketItem {

    String getDisplayName();

    String getItemClass();

    BigDecimal getSalesPrice();

    BigDecimal getDiscount();

    BigDecimal getTotal();

    int getQuantity();

    List<BasketItem> getSubItems();

    List<Discount> getDiscounts();

    int getStampsUsed();
}
