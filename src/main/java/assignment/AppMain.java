package assignment;

import assignment.basket.Basket;
import assignment.basket.BasketService;
import assignment.catalog.CatalogService;
import assignment.catalog.ConfiguredProduct;
import assignment.checkout.CheckoutService;
import assignment.checkout.LoyaltyCard;
import assignment.order.OrderItem;
import assignment.order.OrderService;
import assignment.receipt.Receipt;
import assignment.receipt.ReceiptRenderer;
import assignment.receipt.ReceiptRendererBuilder;
import assignment.receipt.RendererType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

/**
 * The idea of the application is as follows:
 * <ol>
 *     <li>Parse user given input (with {@link assignment.order.OrderService})</li>
 *     <li>Validate user input: check if combination of products is valid and if products can be ordered {@link assignment.catalog.CatalogService}</li>
 *     <li>Create a shopping/item basket containing ordered items with their prices and discounts (with {@link assignment.basket.BasketService})</li>
 *     <li>Create a receipt and updating the loyalty card (with {@link assignment.checkout.CheckoutService})</li>
 *     <li>Render receipt to console (with {@link assignment.receipt.ReceiptRenderer})</li>
 * </ol>
 * Please read the service component Api doc comments to get more insights. They are the entry points for each package.
 * The implementation goes with the 'package-by-feature' structuring approach.
 */
public class AppMain {

    private OrderService orderService;
    private CatalogService catalogService;
    private BasketService basketService;
    private CheckoutService checkoutService;
    private ReceiptRenderer receiptRenderer;

    public void start(final String[] args) throws IOException {
        if (args.length < 1) {
            displayHelp();
            return;
        }

        // parse input
        final Properties orderDefinition = new Properties();
        orderDefinition.load(new StringReader(args[0].replaceAll(",","\n")));
        final List<OrderItem> order = orderService.createOrder(orderDefinition);

        LoyaltyCard loyaltyCard = new LoyaltyCard(new Properties());
        if(args.length>1) {
            // this is not safe to access files on disk, dont do that in production!
            final Path cardFile = Paths.get("./" + args[1]);
            if(Files.exists(cardFile)) {
                final Properties stamps = new Properties();
                try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(cardFile), StandardCharsets.UTF_8)) {
                    stamps.load(reader);
                }
                loyaltyCard = new LoyaltyCard(stamps);
            }
        }

        // process order
        final List<String> validationErrors = catalogService.validateOrder(order);
        if(validationErrors.size()>0) {
            validationErrors.forEach(System.err::println);
            return;
        }
        final List<ConfiguredProduct> productList = catalogService.getProducts(order);
        final Basket basket = basketService.createBasket(productList, loyaltyCard);
        final Receipt receipt = checkoutService.checkout(basket, loyaltyCard);

        // render
        receiptRenderer.render(receipt, System.out);
    }

    public static void main(String[] args) throws IOException {
        final AppMain appMain = new AppMain();

        // prepare service components (missing DI)
        final OrderService orderService = new OrderService();
        appMain.setOrderService(orderService);

        final CatalogService catalogService = new CatalogService();
        catalogService.setItemListFilePath("./src/main/resources/item_catalog.csv");
        catalogService.setProductListFilePath("./src/main/resources/products.csv");
        catalogService.init();

        final BasketService basketService = new BasketService();
        basketService.init();

        final CheckoutService checkoutService = new CheckoutService();
        checkoutService.setCardFilePath(Paths.get("./loyaltyCard.properties"));

        final ReceiptRenderer receiptRenderer = new ReceiptRendererBuilder(RendererType.PLAIN_TEXT)
                .withHeader(Files.readString(Paths.get("./src/main/resources/receipt_header.txt"), StandardCharsets.UTF_8))
                .withFooter(Files.readString(Paths.get("./src/main/resources/receipt_footer.txt"), StandardCharsets.UTF_8))
                .build();

        appMain.setCatalogService(catalogService);
        appMain.setBasketService(basketService);
        appMain.setCheckoutService(checkoutService);
        appMain.setReceiptRenderer(receiptRenderer);

        appMain.start(args);
    }

    public static void displayHelp() {
        System.out.println("");
        System.out.println("Charlene's Coffee Corner");
        System.out.println("");
        System.out.println("usage: ");
        System.out.println("java assignment.AppMain orderItems <optional:loyaltyCard>");
        System.out.println("");
        System.out.println("orderItems: a comma separated list of product_key with optional extras and order quantity");
        System.out.println("e.g.: coffee_small=1 ................................. order of 1 small coffee");
        System.out.println("e.g.: coffee_small/milk_cold=1 ....................... order of 1 small coffee with cold milk");
        System.out.println("e.g.: coffee_small/milk_cold/special_roast=3 ......... order of 3 small coffee with cold milk and special roast");
        System.out.println("e.g.: coffee_small=1,coffee_large=2 .................. order of 1 small coffee and 2 large coffee");
        System.out.println("");
        System.out.println("loyaltyCard: (optional) path to the loyalty card file previously generated by the app");
        System.out.println("");
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void setCatalogService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public void setBasketService(BasketService basketService) {
        this.basketService = basketService;
    }

    public void setCheckoutService(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    public void setReceiptRenderer(ReceiptRenderer receiptRenderer) {
        this.receiptRenderer = receiptRenderer;
    }
}
