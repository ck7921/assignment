package assignment.basket;

import assignment.catalog.ConfiguredProduct;
import assignment.checkout.LoyaltyCard;
import assignment.utils.calculations.BigDecimalHelper;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;

/**
 * The purpose of the price engine is to determine the sales prices and discounts
 * of each basket item. The implementation here is of course very simple.
 * The engine can be extended with sophisticated price and discount rules
 * without requiring code changes anywhere else.
 */
public class SimplePriceEngine {

    private final Predicate<ConfiguredProduct> beverageFilter = (item) -> "coffee".equals(item.getItemClass())
            || "soft_drink".equals(item.getItemClass());

    private final Predicate<ConfiguredProduct> snackFilter = (item) -> "food".equals(item.getItemClass());

    public List<BasketItem> calculatePrices(final List<ConfiguredProduct> configuredProducts,
                                            final LoyaltyCard loyaltyCard) {

        // every 5th beverage is free (=coffee or soft_drink)
        int freeBeverageCounter = loyaltyCard!=null ? (loyaltyCard.getStampCounter("coffee")
                + loyaltyCard.getStampCounter("soft_drink"))/4 : 0;

        // for every beverage snack combination, there is a free extra
        final int countBeverage = configuredProducts.stream()
                .filter(beverageFilter)
                .mapToInt(ConfiguredProduct::getQuantity)
                .sum();
        final int snackCounter =  configuredProducts.stream()
                .filter(snackFilter)
                .mapToInt(ConfiguredProduct::getQuantity)
                .sum();
        long freeExtras = Math.min(countBeverage, snackCounter);

        final List<BasketItem> result = new LinkedList<>();

        for (final ConfiguredProduct configuredProduct : configuredProducts) {
            int count = configuredProduct.getQuantity();
            while (count-->0) {
                final BasketItemImpl basketItem = createBasketItem(configuredProduct);
                basketItem.setSalesPrice(basketItem.getSalesPrice());

                if(freeBeverageCounter>0 && beverageFilter.test(configuredProduct)) {
                    addDiscount(basketItem,"5Stamps Collect Discount", configuredProduct.getSalesPrice());
                    freeBeverageCounter--;
                    basketItem.setStampsUsed(4);
                }

                if(freeExtras>0) {
                    final boolean hasFreeExtraCandidate = basketItem.getSubItems()!=null
                            && basketItem.getSubItems().size()>0;
                    if(hasFreeExtraCandidate) {
                        if(basketItem.getDiscounts()==null || basketItem.getDiscounts().size()<1) {
                            // non free drink -> add discount for an extra
                            final BasketItem cheapestExtra = cheapestExtra(basketItem);
                            final String discountName = cheapestExtra.getDisplayName() != null ?
                                    "Free: " + cheapestExtra.getDisplayName() : "Free Extra";
                            addDiscount(basketItem, discountName, cheapestExtra.getSalesPrice());
                            --freeExtras;
                        }
                    }
                }

                basketItem.setDiscount(calculateTotalDiscount(basketItem));
                basketItem.setTotal(calculateTotalItemPrice(basketItem).subtract(basketItem.getDiscount()));

                result.add(basketItem);
            }
        }

        return result;
    }

    private BasketItemImpl createBasketItem(final ConfiguredProduct configuredProduct) {
        final BasketItemImpl item = new BasketItemImpl();
        item.setDisplayName(configuredProduct.getDisplayName());
        item.setItemClass(configuredProduct.getItemClass());
        item.setSalesPrice(configuredProduct.getSalesPrice());
        item.setQuantity(1);
        item.setTotal(configuredProduct.getSalesPrice());
        if(configuredProduct.getSubProducts()!=null && configuredProduct.getSubProducts().size()>0) {
            for (final ConfiguredProduct subProduct : configuredProduct.getSubProducts()) {
                item.getSubItems().add(createBasketItem(subProduct));
            }
        }
        return item;
    }

    private void addDiscount(final BasketItemImpl basketItem,
                             final String displayName,
                             final BigDecimal discountAmount) {
        final DiscountImpl discount = new DiscountImpl();
        discount.setDisplayName(displayName);
        discount.setDiscount(discountAmount);
        basketItem.getDiscounts().add(discount);
    }

    private BigDecimal calculateTotalDiscount(final BasketItem basketItem) {
        BigDecimal totalDiscount = basketItem.getDiscounts()
                .stream()
                .map(Discount::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if(basketItem.getSubItems()!=null && basketItem.getSubItems().size()>0) {
            totalDiscount = totalDiscount.add(basketItem.getSubItems().stream()
                    .map(this::calculateTotalDiscount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }
        return totalDiscount;
    }

    private BigDecimal calculateTotalItemPrice(final BasketItem basketItem) {
        BigDecimal price = basketItem.getSalesPrice().multiply(new BigDecimal(basketItem.getQuantity()));

        price = price.add(basketItem.getSubItems().stream()
                .map(item -> item.getSalesPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return price;
    }

    private BasketItem cheapestExtra(final BasketItem item) {
        if(item.getSubItems()==null || item.getSubItems().size()<1) {
            return null;
        }
        BigDecimal minPrice = item.getSubItems().get(0).getSalesPrice();
        BasketItem cheapestItem = item.getSubItems().get(0);
        for (int i = 1; i < item.getSubItems().size(); i++) {
            final BasketItem subItem =  item.getSubItems().get(i);
            if(BigDecimalHelper.lt(subItem.getSalesPrice(), minPrice)) {
                minPrice = subItem.getSalesPrice();
                cheapestItem = subItem;
            }
        }
        return cheapestItem;
    }
}
