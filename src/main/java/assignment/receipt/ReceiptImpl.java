package assignment.receipt;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class ReceiptImpl implements Receipt {

    private String id;
    private Instant creationTimestamp;
    private final List<LineItem> lineItems = new LinkedList<>();
    private final List<String> additionalText = new LinkedList<>();
    private String grandTotalDisplay;
    private String grandTotalDiscount;

    public ReceiptImpl() {
    }

    public ReceiptImpl(final ReceiptImpl src) {
        this.id = src.id;
        this.creationTimestamp = src.creationTimestamp;
        final List<assignment.receipt.LineItem> deepClonedLineItems = src.lineItems.stream()
                .map(LineItemImpl::new)
                .collect(Collectors.toList());
        this.lineItems.addAll(deepClonedLineItems);
        this.additionalText.addAll(src.additionalText);
        this.grandTotalDisplay = src.grandTotalDisplay;
        this.grandTotalDiscount = src.grandTotalDiscount;
    }

    @Override
    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    @Override
    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public List<LineItem> getLineItems() {
        return lineItems;
    }

    @Override
    public List<String> getAdditionalText() {
        return additionalText;
    }

    LineItemImpl createLineItem() {
        return new LineItemImpl();
    }

    @Override
    public String getGrandTotalDisplay() {
        return grandTotalDisplay;
    }

    public void setGrandTotalDisplay(String grandTotalDisplay) {
        this.grandTotalDisplay = grandTotalDisplay;
    }

    @Override
    public String getGrandTotalDiscount() {
        return grandTotalDiscount;
    }

    public void setGrandTotalDiscount(String grandTotalDiscount) {
        this.grandTotalDiscount = grandTotalDiscount;
    }

    static class LineItemImpl implements LineItem {
        private String displayCount;
        private String displayName;
        private String displayDiscount;
        private String displayPrice;
        private String displayTotal;
        private List<LineItem> subItems = new LinkedList<>();

        private LineItemImpl() {
        }

        private LineItemImpl(final LineItem src) {
            this.displayCount = src.getDisplayCount();
            this.displayName = src.getDisplayName();
            this.displayDiscount = src.getDisplayDiscount();
            this.displayPrice = src.getDisplayPrice();
            this.displayTotal = src.getDisplayTotal();
            final List<LineItemImpl> deepClone = src.getSubItems().stream()
                    .map(LineItemImpl::new)
                    .collect(Collectors.toList());
            this.subItems.addAll(deepClone);
        }

        void setDisplayCount(String displayCount) {
            this.displayCount = displayCount;
        }

        void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        void setDisplayDiscount(String displayDiscount) {
            this.displayDiscount = displayDiscount;
        }

        void setDisplayPrice(String displayPrice) {
            this.displayPrice = displayPrice;
        }

        void setDisplayTotal(String displayTotal) {
            this.displayTotal = displayTotal;
        }

        @Override
        public String getDisplayCount() {
            return displayCount;
        }

        @Override
        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String getDisplayDiscount() {
            return displayDiscount;
        }

        @Override
        public String getDisplayPrice() {
            return displayPrice;
        }

        @Override
        public String getDisplayTotal() {
            return displayTotal;
        }

        @Override
        public List<LineItem> getSubItems() {
            return subItems;
        }
    }



}
