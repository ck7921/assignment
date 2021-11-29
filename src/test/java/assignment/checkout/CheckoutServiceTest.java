package assignment.checkout;

import assignment.basket.Basket;
import assignment.basket.BasketItem;
import assignment.basket.Discount;
import assignment.receipt.Receipt;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    @Test
    void simpleCheckoutTest() {
        CheckoutService service = new CheckoutService();

        LoyaltyCard loyaltyCard = createDummyLoyaltyCard();
        final Basket basket = createBasket();

        Receipt result = service.checkout(basket, loyaltyCard);

        assertNotNull(result);

        assertEquals("10,00 CHF", result.getGrandTotalDisplay(), "invalid total");
        assertEquals("5,00 CHF", result.getGrandTotalDiscount(), "invalid discount total");
        assertNotNull(result.getAdditionalText());
        assertEquals(1, result.getAdditionalText().size());
        assertTrue(result.getAdditionalText().get(0).contains("1 loyalty point"));
        assertNotNull(result.getLineItems());
        assertEquals(2, result.getLineItems().size());
        assertNotNull(result.getId());
        assertNotNull(result.getCreationTimestamp());
    }

    private LoyaltyCard createDummyLoyaltyCard() {
        return new LoyaltyCard(new Properties());
    }

    private Basket createBasket() {
        final List<BasketItem> items = new LinkedList<>();
        items.add(createBasketItem("Coffee small", "coffee", "2.50",
                null, "2.50", 1,null, null, 0));

        final Discount specialDiscount = createDiscount("special discount", "5.0");
        items.add(createBasketItem("Bacon roll Super Size", "food", "12.50",
                "5.0", "7.50", 1,null, Collections.singletonList(specialDiscount), 0));

        return new Basket() {
            @Override
            public String getCurrencySymbol() {
                return "CHF";
            }

            @Override
            public BigDecimal getTotal() {
                return new BigDecimal("10");
            }

            @Override
            public BigDecimal getTotalDiscount() {
                return new BigDecimal("5");
            }

            @Override
            public List<BasketItem> getItems() {
                return items;
            }
        };

    }

    private BasketItem createBasketItem(String name, String itemClass, String salesPrice,
                                        String discount, String total, int qty,
                                        List<BasketItem> subItems, List<Discount> discounts,
                                        int stampsUsed) {
        return new BasketItem() {
            @Override
            public String getDisplayName() {
                return name;
            }

            @Override
            public String getItemClass() {
                return itemClass;
            }

            @Override
            public BigDecimal getSalesPrice() {
                return new BigDecimal(salesPrice);
            }

            @Override
            public BigDecimal getDiscount() {
                return  discount!=null ? new BigDecimal(discount) : null;
            }

            @Override
            public BigDecimal getTotal() {
                return new BigDecimal(total);
            }

            @Override
            public int getQuantity() {
                return qty;
            }

            @Override
            public List<BasketItem> getSubItems() {
                return subItems!=null ? subItems : Collections.emptyList();
            }

            @Override
            public List<Discount> getDiscounts() {
                return discounts!=null ? discounts : Collections.emptyList();
            }

            @Override
            public int getStampsUsed() {
                return stampsUsed;
            }
        };
    }

    private Discount createDiscount(String name, String discount) {
        return new Discount() {
            @Override
            public String getDisplayName() {
                return name;
            }

            @Override
            public BigDecimal getDiscount() {
                return new BigDecimal(discount);
            }
        };
    }
}