package assignment.basket;

import java.math.BigDecimal;
import java.util.List;

public interface Basket {

    String getCurrencySymbol();

    BigDecimal getTotal();

    BigDecimal getTotalDiscount();

    List<BasketItem> getItems();

}
