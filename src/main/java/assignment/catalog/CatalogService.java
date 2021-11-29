package assignment.catalog;

import assignment.order.OrderItem;
import assignment.utils.SimpleValueMapper;
import assignment.utils.csv.CsvRecord;
import assignment.utils.csv.SimpleCsvParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * The Catalog Services knows all about Products and how they can be configured.
 * Normally the data would be loaded from a database or PIM but for
 * this assignment CSV files are used to define a product structure.
 * With this service an order can be verified and also
 * a list of configured products can be created which represent
 * the requested products of the customer in the desired variation.
 * The lambda functions which provide input streams are added for testing purposes.
 */
public class CatalogService {

    private String productListFilePath;
    private String itemListFilePath;

    private Map<String, ItemImpl> items = new HashMap<>();
    private Map<String, ProductImpl> products;
    private final Map<String, Set<String>> productExtras = new HashMap<>();

    private Supplier<InputStream> itemListStreamSource = () -> {
        try {
            return Files.newInputStream(Paths.get(itemListFilePath));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    };

    private Supplier<InputStream> productListStreamSource = () -> {
        try {
            return Files.newInputStream(Paths.get(productListFilePath));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    };

    public void init() throws IOException {

        final SimpleValueMapper<ItemImpl> productMapper = new SimpleValueMapper<>(ItemImpl::new);
        SimpleCsvParser parser = new SimpleCsvParser();

        final InputStream itemSourceStream = itemListStreamSource.get();
        items = parser.parse(itemSourceStream)
                .map(row -> productMapper.map(row.getColumnData()))
                .filter(item -> !items.containsKey(item.getKey()))
                .collect(Collectors.toMap(ItemImpl::getKey, Function.identity()));
        parser.close();

        final InputStream productSourceStream = productListStreamSource.get();
        products = parser.parse(productSourceStream)
                .map(this::createProductDto)
                .collect(Collectors.toMap(ProductImpl::getKey, Function.identity()));
        parser.close();
    }

    private ProductImpl createProductDto(final CsvRecord record) {
        final ProductImpl product = new ProductImpl();
        product.setKey(record.getValue("baseProduct"));
        final ItemImpl item = items.get(product.getKey());
        if(item == null) {
            throw new IllegalStateException("data inconsistent, item missing: " + product.getKey());
        }

        product.setDisplayName(item.getDisplayName());
        product.setItemClass(item.getItemClass());
        product.setSalesPrice(item.getSalesPrice());

        final String optionalExtraItemClasses = record.getValue("options");
        if(optionalExtraItemClasses!=null && optionalExtraItemClasses.length()>0) {
            productExtras.put(product.getKey(), new HashSet<>(Arrays.asList(optionalExtraItemClasses.split(";"))));
        }

        return product;
    }

    public List<String> validateOrder(final List<OrderItem> orderItemList) {
        final List<String> validationErrors = new LinkedList<>();
        if(orderItemList==null || orderItemList.size()<1) {
            validationErrors.add("at least one product must be ordered");
            return validationErrors;
        }

        for (final OrderItem orderItem : orderItemList) {
            if(!products.containsKey(orderItem.getProductKey())) {
                validationErrors.add("product can not be ordered, does it exist? Maybe it's an optional extra?"
                        + orderItem.getProductKey());
                continue;
            }
            if(orderItem.getQuantity()<1) {
                validationErrors.add("invalid quantity for product "
                        + orderItem.getProductKey());
            }
            final List<String> itemExtras = orderItem.getExtras();
            final Set<String> permittedExtras = productExtras.getOrDefault(orderItem.getProductKey(), Collections.emptySet());
            // currently, we permit only one extra of each item class, ordering milk two times, doesn't work
            // this would require a more sophisticated rule definition language
            final Map<String,Integer> extraCounter = new HashMap<>();
            if(itemExtras!=null && itemExtras.size()>0) {
                for (final String extra : orderItem.getExtras()) {
                    final ItemImpl item = items.get(extra);
                    if(item==null) {
                        validationErrors.add("invalid product config for product " + orderItem.getProductKey() + ", extra not permitted: " + extra);
                    }
                    final String itemClass = item.getItemClass();
                    if(!permittedExtras.contains(itemClass)) {
                        validationErrors.add("invalid product config for product " + orderItem.getProductKey() + ", extra not permitted: " + extra);
                    } else {
                        extraCounter.compute(itemClass, (key,val) -> val!=null ? val+1 : 1);
                        if(extraCounter.get(itemClass)>1) {
                            validationErrors.add("invalid product config for product " + orderItem.getProductKey() + ", extra of class "+itemClass+" added more than once");
                        }
                    }
                }
            }
        }

        return validationErrors;
    }

    public List<ConfiguredProduct> getProducts(final List<OrderItem> orderItemList) {
        final List<ConfiguredProduct> result = new LinkedList<>();

        for (final OrderItem orderItem : orderItemList) {
            result.add(convert(orderItem));
        }

        return result;
    }

    private ConfiguredProductImpl convert(final OrderItem orderItem) {
        final ConfiguredProductImpl configuredProduct = new ConfiguredProductImpl();
        final ProductImpl product = products.get(orderItem.getProductKey());

        configuredProduct.setKey(product.getKey());
        configuredProduct.setDisplayName(product.getDisplayName());
        configuredProduct.setSalesPrice(product.getSalesPrice());
        configuredProduct.setQuantity(orderItem.getQuantity());
        configuredProduct.setItemClass(product.getItemClass());

        // add extras
        if(orderItem.getExtras()!=null && orderItem.getExtras().size()>0) {
            for (final String itemExtraKey : orderItem.getExtras()) {
                final ConfiguredProductImpl subItem = convert(itemExtraKey);
                configuredProduct.getSubProducts().add(subItem);
            }
        }
        return configuredProduct;
    }

    private ConfiguredProductImpl convert(final String itemKey) {
        final ConfiguredProductImpl configuredProduct = new ConfiguredProductImpl();
        final ItemImpl item = items.get(itemKey);

        configuredProduct.setKey(item.getKey());
        configuredProduct.setDisplayName(item.getDisplayName());
        configuredProduct.setSalesPrice(item.getSalesPrice());
        configuredProduct.setQuantity(1);
        configuredProduct.setItemClass(item.getItemClass());

        return configuredProduct;
    }

    public void setItemListFilePath(final String itemListFilePath) {
        this.itemListFilePath = itemListFilePath;
    }

    public void setProductListFilePath(final String productListFilePath) {
        this.productListFilePath = productListFilePath;
    }

    public void setItemListStreamSource(Supplier<InputStream> itemListStreamSource) {
        this.itemListStreamSource = itemListStreamSource;
    }

    public void setProductListStreamSource(Supplier<InputStream> productListStreamSource) {
        this.productListStreamSource = productListStreamSource;
    }
}
