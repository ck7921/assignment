package assignment.order;

import assignment.utils.parsing.NumberParserHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * This services parses a user given raw input into formal java objects.
 * Having the request decoupled from the given raw input the input
 * method can be changed easily without effecting other code.
 */
public class OrderService {

    public List<OrderItem> createOrder(final Properties p) {
        final OrderBuilder orderBuilder = new OrderBuilder();
        for (Object key : p.keySet()) {
            final String[] components = key.toString().split("/");
            final Object qty = p.get(key);
            final int quantity = qty==null ? 1 : NumberParserHelper.parseInt(qty.toString(), 1);
            if(quantity<1) {
                System.err.println("invalid quantity defined for product " + components[0]);
                throw new IllegalArgumentException("order definition invalid");
            }

            orderBuilder.withProduct(components[0])
                    .withQuantity(quantity);
            Arrays.stream(components)
                    .skip(1)
                    .forEach(orderBuilder::withExtra);
            orderBuilder.addProduct();
        }

        return orderBuilder.build();
    }

}
