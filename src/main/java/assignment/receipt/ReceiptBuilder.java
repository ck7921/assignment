package assignment.receipt;

import java.time.Instant;
import java.util.function.Consumer;

public class ReceiptBuilder {

    private ReceiptImpl receipt;

    public ReceiptBuilder() {
        this.receipt = new ReceiptImpl();
    }

    public ReceiptBuilder withId(final String id) {
        this.receipt.setId(id);
        return this;
    }

    public ReceiptBuilder withCreationTimestamp(final Instant creationTimestamp) {
        this.receipt.setCreationTimestamp(creationTimestamp);
        return this;
    }

    public ReceiptBuilder withGrandTotalDisplay(final String grandTotalDisplay) {
        receipt.setGrandTotalDisplay(grandTotalDisplay);
        return this;
    }

    public ReceiptBuilder withGrandTotalDiscountDisplay(final String grandTotalDiscountDisplay) {
        receipt.setGrandTotalDiscount(grandTotalDiscountDisplay);
        return this;
    }

    public ReceiptBuilder addAdditionalText(final String text) {
        if (text != null) {
            this.receipt.getAdditionalText().add(text);
        }
        return this;
    }

    public LineItemBuilder<ReceiptBuilder> buildLineItem() {
        return new LineItemBuilder<>(this, (item) -> {
            receipt.getLineItems().add(item);
        });
    }

    public Receipt build() {
        final ReceiptImpl result = new ReceiptImpl(receipt);
        validate(result);
        this.receipt = new ReceiptImpl();
        return result;
    }

    private void validate(final ReceiptImpl receipt) {
        if (receipt == null) {
            throw new IllegalArgumentException("receipt must not be null");
        }
        if (receipt.getCreationTimestamp() == null) {
            throw new IllegalStateException("creation timestamp missing");
        }
        if (receipt.getId() == null) {
            throw new IllegalStateException("receipt id is missing");
        }
    }

    public class LineItemBuilder<PARENT_TYPE> {
        private final PARENT_TYPE parent;
        private ReceiptImpl.LineItemImpl lineItem = receipt.createLineItem();
        private Consumer<LineItem> itemAttachment;

        private LineItemBuilder(final PARENT_TYPE parent, Consumer<LineItem> itemAttachment) {
            this.parent = parent;
            this.itemAttachment = itemAttachment;
        }

        public LineItemBuilder<LineItemBuilder<PARENT_TYPE>> addSubItem() {
            if (parent instanceof LineItemBuilder) {
                throw new IllegalStateException("currently only one level of sub item is supported");
            }
            return new LineItemBuilder<>(this, (subItem) -> {
                lineItem.getSubItems().add(subItem);
            });
        }

        public LineItemBuilder<PARENT_TYPE> withDisplayCount(final String displayCount) {
            lineItem.setDisplayCount(displayCount);
            return this;
        }

        public LineItemBuilder<PARENT_TYPE> withDisplayName(final String displayName) {
            lineItem.setDisplayName(displayName);
            return this;
        }

        public LineItemBuilder<PARENT_TYPE> withDisplayDiscount(final String displayDiscount) {
            lineItem.setDisplayDiscount(displayDiscount);
            return this;
        }

        public LineItemBuilder<PARENT_TYPE> withDisplayPrice(final String displayPrice) {
            lineItem.setDisplayPrice(displayPrice);
            return this;
        }

        public LineItemBuilder<PARENT_TYPE> withDisplayTotal(final String displayTotal) {
            lineItem.setDisplayTotal(displayTotal);
            return this;
        }

        public PARENT_TYPE add() {
            this.itemAttachment.accept(this.lineItem);
            this.lineItem = null;
            return parent;
        }
    }

}
