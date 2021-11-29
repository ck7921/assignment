package assignment.checkout;

import assignment.basket.Basket;
import assignment.basket.BasketItem;
import assignment.basket.Discount;
import assignment.receipt.Receipt;
import assignment.receipt.ReceiptBuilder;
import assignment.utils.calculations.BigDecimalHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The Checkout Service would normally be the service to place an order.
 * Here it takes a basket and a loyalty card and creates a receipt
 * and also updates the loyalty card. It writes it back to disk
 * so it can be used in repeated application runs.
 * Metaphorical speaking:
 * The checkout service represent the cash register in the coffee shop
 * while the {@link assignment.receipt.ReceiptRenderer} acts as the printer.
 * Normally the checkout process is complex and also receipts are
 * printed by a terminal running its own software to additional
 * tax information etc. Her of course, just a simple implementation.
 */
public class CheckoutService {

    private Supplier<Instant> nowProvider = Instant::now;
    private Path cardFilePath = null;

    private final Predicate<BasketItem> beverageFilter = (item) -> "coffee".equals(item.getItemClass())
            || "soft_drink".equals(item.getItemClass());

    public Receipt checkout(final Basket basket, final LoyaltyCard loyaltyCard) {

        final ReceiptBuilder receiptBuilder = new ReceiptBuilder();
        final String displayGrandTotal = BigDecimalHelper.formatPrice(basket.getCurrencySymbol(), basket.getTotal());
        final String displayGrandTotalDiscount = BigDecimalHelper.gt(basket.getTotalDiscount(), BigDecimal.ZERO) ?
                BigDecimalHelper.formatPrice(basket.getCurrencySymbol(), basket.getTotalDiscount()) : "";
        receiptBuilder.withId(UUID.randomUUID().toString())
                .withCreationTimestamp(nowProvider.get())
                .withGrandTotalDisplay(displayGrandTotal)
                .withGrandTotalDiscountDisplay(displayGrandTotalDiscount);

        for (final BasketItem item : basket.getItems()) {

            final String displayPrice = BigDecimalHelper.formatPrice(basket.getCurrencySymbol(), item.getSalesPrice());
            final String displayDiscount = item.getDiscount() != null
                    && BigDecimalHelper.gt(item.getDiscount(), BigDecimal.ZERO) ?
                    BigDecimalHelper.formatPrice(basket.getCurrencySymbol(), item.getDiscount()) : "";
            final String displayTotal = item.getTotal() != null ?
                    BigDecimalHelper.formatPrice(basket.getCurrencySymbol(), item.getTotal()) :
                    BigDecimalHelper.formatPrice(basket.getCurrencySymbol(), BigDecimal.ZERO);

            ReceiptBuilder.LineItemBuilder<ReceiptBuilder> builder = receiptBuilder.buildLineItem()
                    .withDisplayCount(String.valueOf(item.getQuantity()))
                    .withDisplayName(item.getDisplayName())
                    .withDisplayPrice(displayPrice)
                    .withDisplayDiscount(displayDiscount)
                    .withDisplayTotal(displayTotal);

            // extras/sub-items
            if (item.getSubItems() != null && item.getSubItems().size() > 0) {
                for (final BasketItem subItem : item.getSubItems()) {
                    final String subItemDisplayPrice =
                            BigDecimalHelper.formatPrice(basket.getCurrencySymbol(), subItem.getSalesPrice());
                    builder = builder.addSubItem()
                            .withDisplayName(subItem.getDisplayName())
                            .withDisplayPrice(subItemDisplayPrice)
                            .add();
                }
            }

            // discounts
            if (item.getDiscounts() != null && item.getDiscounts().size() > 0) {
                for (final Discount discount : item.getDiscounts()) {
                    final String subItemDisplayDiscount =
                            BigDecimalHelper.formatPrice(basket.getCurrencySymbol(), discount.getDiscount().negate());
                    builder = builder.addSubItem()
                            .withDisplayName(discount.getDisplayName())
                            .withDisplayPrice(subItemDisplayDiscount)
                            .add();
                }
            }

            builder.add();
        }

        // update loyalty card
        if (loyaltyCard != null) {
            final LoyaltyCard updatedCard = new LoyaltyCard(loyaltyCard);
            for (final BasketItem item : basket.getItems()) {
                if (item.getStampsUsed() > 0) {
                    updatedCard.decrementStampCounter(item.getItemClass(), item.getStampsUsed());
                } else if ((item.getDiscounts() == null || item.getDiscounts().size() < 1)
                        && beverageFilter.test(item)) {
                    updatedCard.incrementStampCounter(item.getItemClass());
                }
            }
            // write card to disk
            if (cardFilePath != null) {
                writeLoyaltyCard(cardFilePath, updatedCard);
            }

            // currently, only for coffee
            int coffeePoints = updatedCard.getStampCounter("coffee");
            if (coffeePoints > 0) {
                receiptBuilder.addAdditionalText("You have " + coffeePoints + " loyalty point(s)!");
            }

        }


        return receiptBuilder.build();
    }

    private void writeLoyaltyCard(final Path cardFilePath, final LoyaltyCard card) {
        final StringBuilder buffer = new StringBuilder();
        card.getStamps().forEach((key, value) -> buffer.append(key.toString()).append("=").append(value.toString()));
        try {
            Files.writeString(cardFilePath, buffer.toString());
        } catch (final IOException e) {
            System.err.println("Error writing loyalty card to disk.");
        }
    }

    public void setCardFilePath(final Path cardFilePath) {
        this.cardFilePath = cardFilePath;
    }
}
