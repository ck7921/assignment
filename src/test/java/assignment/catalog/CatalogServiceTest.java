package assignment.catalog;

import assignment.order.OrderItem;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CatalogServiceTest {

    @Test
    void simpleValidationTest() throws IOException {
        CatalogService service = prepareTestService();

        List<OrderItem> orderItemList = validOrder();
        List<String> result = service.validateOrder(orderItemList);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        orderItemList = invalidOrder();
        result = service.validateOrder(orderItemList);

        assertNotNull(result);
        assertEquals(2,result.size(),"invalid count of validation errors");
    }

    @Test
    void configureProductsTest() throws IOException {
        CatalogService service = prepareTestService();

        List<OrderItem> orderItemList = validOrderWithExtra();
        List<ConfiguredProduct> result = service.getProducts(orderItemList);

        assertNotNull(result);
        assertEquals(2,result.size(),"invalid count of validation errors");
    }

    private CatalogService prepareTestService() throws IOException {
        String itemCatalogCsv = "key,displayName,salesPrice,itemClass\n";
        itemCatalogCsv += "coffee_small,Coffee small,2.50,coffee\n";
        itemCatalogCsv += "milk_extra,Extra Milk,0.30,milk\n";
        itemCatalogCsv += "bacon_roll,Bacon Roll,4.50,food\n";
        final InputStream catalogStream = new ByteArrayInputStream(itemCatalogCsv.getBytes(StandardCharsets.UTF_8));

        String productCsv = "baseProduct,options\n";
        productCsv += "coffee_small,milk\n";
        productCsv += "bacon_roll,\n";
        final InputStream productStream = new ByteArrayInputStream(productCsv.getBytes(StandardCharsets.UTF_8));

        CatalogService service = new CatalogService();
        service.setItemListStreamSource(() -> catalogStream);
        service.setProductListStreamSource(() -> productStream);
        service.init();
        return service;
    }

    private List<OrderItem> validOrder() {
        List<OrderItem> orderItemList = new LinkedList<>();
        orderItemList.add(createOrderItem("coffee_small",3, Collections.emptyList()));
        orderItemList.add(createOrderItem("bacon_roll",2, Collections.emptyList()));
        return orderItemList;
    }

    private List<OrderItem> validOrderWithExtra() {
        List<OrderItem> orderItemList = new LinkedList<>();
        orderItemList.add(createOrderItem("coffee_small",3, Collections.singletonList("milk_extra")));
        orderItemList.add(createOrderItem("bacon_roll",2, Collections.emptyList()));
        return orderItemList;
    }

    private List<OrderItem> invalidOrder() {
        List<OrderItem> orderItemList = new LinkedList<>();
        orderItemList.add(createOrderItem("bacon_roll",2, Collections.singletonList("milk_extra")));
        orderItemList.add(createOrderItem("milk_extra",1, Collections.emptyList()));
        return orderItemList;
    }

    private OrderItem createOrderItem(String key, int qty, List<String> extras) {
        return new OrderItem() {
            @Override
            public String getProductKey() {
                return key;
            }

            @Override
            public List<String> getExtras() {
                return extras;
            }

            @Override
            public int getQuantity() {
                return qty;
            }
        };
    }
}