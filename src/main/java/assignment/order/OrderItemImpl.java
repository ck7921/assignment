package assignment.order;

import java.util.LinkedList;
import java.util.List;

class OrderItemImpl implements OrderItem {

    private String productKey;
    private List<String> extras = new LinkedList<>();
    private int quantity;

    @Override
    public String getProductKey() {
        return productKey;
    }

    @Override
    public List<String> getExtras() {
        return extras;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public void setExtras(List<String> extras) {
        this.extras = extras;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
