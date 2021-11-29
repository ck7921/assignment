package assignment.basket;

import assignment.catalog.ConfiguredProduct;
import assignment.checkout.LoyaltyCard;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class BasketServiceTest {

    /**
     * i dont have Mockito stubbing so SimplePriceEngine gets tested too
     */
    @Test
    void simpleBasketCreationTest() {

        BasketService basketService = new BasketService();
        basketService.setPriceEngine(new SimplePriceEngine()); // should be stubbed and separately tested

        LoyaltyCard loyaltyCard = createDummyLoyaltyCard();
        loyaltyCard.incrementStampCounter("coffee");
        loyaltyCard.incrementStampCounter("coffee");
        loyaltyCard.incrementStampCounter("coffee");
        loyaltyCard.incrementStampCounter("coffee");
        final List<ConfiguredProduct> products = createConfiguredProducts();

        Basket basket = basketService.createBasket(products, loyaltyCard);

        assertNotNull(basket);
        assertNotNull(basket.getItems());
        assertEquals(5, basket.getItems().size(), "invalid basket size");

        final long countOfFreeExtras = basket.getItems().stream()
                .flatMap(item -> item.getDiscounts().stream())
                .filter(discount -> discount.getDisplayName().contains("Free"))
                .count();
        assertEquals(2, countOfFreeExtras, "invalid count of free extras");

        final long countOfFreeBeverages = basket.getItems().stream()
                .flatMap(item -> item.getDiscounts().stream())
                .filter(discount -> discount.getDisplayName().contains("5Stamps"))
                .count();
        assertEquals(1, countOfFreeBeverages, "invalid count of free beverages");

        assertEquals(new BigDecimal("15.40"), basket.getTotal(), "invalid basket grand total");
        assertEquals(new BigDecimal("3.50"), basket.getTotalDiscount(), "invalid basket grand discount");
    }

    private LoyaltyCard createDummyLoyaltyCard() {
        return new LoyaltyCard(new Properties());
    }

    private List<ConfiguredProduct> createConfiguredProducts() {
        final List<ConfiguredProduct> subProducts = new LinkedList<>();
        subProducts.add(createConfiguredProduct("milk_extra", "Extra Milk", "0.50", 1, "milk", null));

        final List<ConfiguredProduct> products = new LinkedList<>();
        products.add(createConfiguredProduct("coffee_small", "Coffee Small", "2.50", 3, "coffee", subProducts));
        products.add(createConfiguredProduct("bacon_roll", "Bacon Roll", "4.95", 2, "food", null));
        return products;
    }

    private ConfiguredProduct createConfiguredProduct(String key, String name, String salesPrice,
                                                      int qty, String itemClass, List<ConfiguredProduct> subProducts) {
        return new ConfiguredProduct() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String getDisplayName() {
                return name;
            }

            @Override
            public BigDecimal getSalesPrice() {
                return new BigDecimal(salesPrice);
            }

            @Override
            public int getQuantity() {
                return qty;
            }

            @Override
            public String getItemClass() {
                return itemClass;
            }

            @Override
            public List<ConfiguredProduct> getSubProducts() {
                return subProducts!=null ? subProducts : Collections.emptyList();
            }
        };
    }
}