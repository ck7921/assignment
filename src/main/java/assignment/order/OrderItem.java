package assignment.order;

import java.util.List;

public interface OrderItem {

    String getProductKey();

    List<String> getExtras();

    int getQuantity();
}
