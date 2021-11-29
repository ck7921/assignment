package assignment.order;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @Test
    public void simpleOrderCreationTest() {
        Properties p = new Properties();
        p.put("coffee/milk","2");
        p.put("bacon_roll","3");

        OrderService service = new OrderService();
        final List<OrderItem> order = service.createOrder(p);

        assertNotNull(order);
        assertEquals(2, order.size(), "invalid count of items parsed");
        assertTrue(order
                .stream().map(OrderItem::getProductKey).anyMatch("coffee"::equals), "missing product coffee");
        assertTrue(order
                .stream().map(OrderItem::getProductKey).anyMatch("bacon_roll"::equals), "missing product coffee");

        int baconRollQty = order
                .stream()
                .filter(item -> "bacon_roll".equals(item.getProductKey()))
                .map(OrderItem::getQuantity)
                .findAny().orElse(-1);
        assertEquals(3, baconRollQty, "invalid qty of bacon roll");

        int coffeeQty = order
                .stream()
                .filter(item -> "coffee".equals(item.getProductKey()))
                .map(OrderItem::getQuantity)
                .findAny().orElse(-1);
        assertEquals(2, coffeeQty, "invalid qty of coffee");

        int coffeeExtraCount = order
                .stream()
                .filter(item -> "coffee".equals(item.getProductKey()))
                .mapToInt(item -> item.getExtras().size())
                .sum();
        assertEquals(1, coffeeExtraCount, "missing extras of coffee");
    }

}