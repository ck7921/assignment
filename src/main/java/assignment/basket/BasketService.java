package assignment.basket;

import assignment.catalog.ConfiguredProduct;
import assignment.checkout.LoyaltyCard;

import java.math.BigDecimal;
import java.util.List;

/**
 * The basket service creates a shopping basket containing the products
 * the customer wants to purchase. It add prices and discounts
 * (by using a {@link SimplePriceEngine}) and returns a {@link Basket}
 * which can be rendered to the customer on a display in the shop
 * at the cash desk or e.g. on a web-page.
 * Normally the {@link SimplePriceEngine} would be an injected service,
 * for the purpose of this assignment just a simple implementation is used
 * and directly instantiated.
 */
public class BasketService {

    private SimplePriceEngine priceEngine;

    public void init() {
        this.priceEngine = new SimplePriceEngine();
    }

    public Basket createBasket(final List<ConfiguredProduct> products,
                               final LoyaltyCard loyaltyCard) {

        final List<BasketItem> basketItems = priceEngine.calculatePrices(products, loyaltyCard);

        final BasketImpl basket = new BasketImpl();
        basket.setCurrencySymbol("CHF"); // Currently hard coded! Not the way to do
        basket.setItems(basketItems);

        final BigDecimal totalValue = basketItems.stream()
                .map(BasketItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        basket.setTotal(totalValue);

        final BigDecimal totalDiscount = basketItems.stream()
                .map(BasketItem::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        basket.setTotalDiscount(totalDiscount);

        return basket;
    }

    public void setPriceEngine(final SimplePriceEngine priceEngine) {
        this.priceEngine = priceEngine;
    }
}
