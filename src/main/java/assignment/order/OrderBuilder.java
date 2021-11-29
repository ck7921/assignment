package assignment.order;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class OrderBuilder {

    private OrderItemImpl item;
    private List<OrderItem> items = new LinkedList<>();

    public OrderBuilder withProduct(final String productKey) {
        this.item = new OrderItemImpl();
        this.item.setProductKey(productKey);
        this.item.setQuantity(1);
        return this;
    }

    public OrderBuilder withQuantity(final int quantity) {
        this.item.setQuantity(quantity);
        return this;
    }

    public OrderBuilder withExtra(final String extra) {
        this.item.getExtras().add(extra);
        return this;
    }

    public OrderBuilder withExtras(final String... extras) {
        this.item.getExtras().addAll(Arrays.asList(extras));
        return this;
    }

    public OrderBuilder addProduct() {
        this.items.add(item);
        this.item = null;
        return this;
    }

    public List<OrderItem> build() {
        if(item!=null) {
            throw new IllegalStateException("order item construction not completed");
        }
        List<OrderItem> result = this.items;
        this.items = new LinkedList<>();
        return result;
    }

}
