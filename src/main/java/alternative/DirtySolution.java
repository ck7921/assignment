package alternative;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;

/**
 * NOT THE REAL SOLUTION
 * 
 * this solution is a quick hack. It doesn't do proper validation
 * and it can't be extended. Please look at the other solution in {@link assignment.AppMain}
 */
public class DirtySolution {

    private Consumer<String> writer;

    public static void main(String[] args) {

        if(args.length<1) {
            System.out.println("required input argument e.g. coffee_small=1 is missing");
            System.out.println("if you want to add loyalty points, add another number parameter e.g. coffee_small=1 is missing 4");
        }
        DirtySolution main = new DirtySolution();
        main.setWriter(System.out::println);

        // order.add("coffee_small/extra_milk=1");
  /*      order.add("coffee_small=0");
        order.add("coffee_small/extra_milk/special_roast=1");
        order.add("bacon_roll=0");
        order.add("soft_drink=1");
*/
        final List<String> order = new LinkedList<>(Arrays.asList(args[0].split(",")));
        int collectedStamps = args.length>1 ? Integer.parseInt(args[1]) : 0;

        main.start(order,collectedStamps);
    }

    public void start(final List<String> order, int beverageStamps) {
        Product extraMilk = new Product();
        extraMilk.key = "extra_milk";
        extraMilk.name = "Extra Milk";
        extraMilk.price = new BigDecimal("0.30");

        Product foamedMilk = new Product();
        foamedMilk.key = "foamed_milk";
        foamedMilk.name = "Foamed Milk";
        foamedMilk.price = new BigDecimal("0.50");

        Product specialRoast = new Product();
        specialRoast.key = "special_roast";
        specialRoast.name = "Special Roast Coffee";
        specialRoast.price = new BigDecimal("0.90");

        Map<String, Product> products = new HashMap<>();

        Product p = new Product();
        p.name = "Coffee Small";
        p.type = "beverage";
        p.price = new BigDecimal("2.50");
        p.subProducts.add(extraMilk);
        p.subProducts.add(foamedMilk);
        p.subProducts.add(specialRoast);
        products.put("coffee_small", p);
        p = new Product();
        p.name = "Coffee Medium";
        p.type = "beverage";
        p.price = new BigDecimal("3.00");
        p.subProducts.add(extraMilk);
        p.subProducts.add(foamedMilk);
        p.subProducts.add(specialRoast);
        products.put("coffee_medium", p);
        p = new Product();
        p.name = "Coffee Large";
        p.type = "beverage";
        p.price = new BigDecimal("3.50");
        p.subProducts.add(extraMilk);
        p.subProducts.add(foamedMilk);
        p.subProducts.add(specialRoast);
        products.put("coffee_large", p);

        p = new Product();
        p.name = "Bacon Roll";
        p.type = "food";
        p.price = new BigDecimal("4.50");
        products.put("bacon_roll", p);
        p = new Product();
        p.name = "Freshly squeezed orange juice (0.25)";
        p.type = "beverage";
        p.price = new BigDecimal("3.95");
        products.put("soft_drink", p);

        int freeBeverages = beverageStamps / 4;

        List<Product> basket = new LinkedList<>();
        for (String product : order) {
            String key = product.split("=")[0].split("/")[0];
            int qty = Integer.parseInt(product.split("=")[1]);
            Set<String> extras = new HashSet<>(Arrays.asList(product.split("=")[0].split("/")));

            Product item = products.get(key);
            while (qty-- > 0) {
                basket.add(create(item, extras));
            }
        }

        writer.accept("Charlenes Coffee Corner");
        writer.accept(new String(new byte[70]).replaceAll("\0", "-"));

        long countOfBeverages = basket.stream()
                .filter(item -> "beverage".equals(item.type))
                .count();
        long countOfSnacks = basket.stream()
                .filter(item -> "food".equals(item.type))
                .count();
        long freeExtras = Math.min(countOfBeverages, countOfSnacks);

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        for (final Product product : basket) {

            total = total.add(product.price);
            totalDiscount = totalDiscount.add(product.discount != null ?
                    product.discount.value : BigDecimal.ZERO);
            writer.accept(product.name + " CHF " + product.price.toString());
            if (product.discount != null) {
                writer.accept(product.discount.name + " CHF -" + product.discount.value.toString());
            }

            boolean freeExtraUsed = false;
            for (Product subProduct : product.subProducts) {
                total = total.add(subProduct.price);
                writer.accept(subProduct.name + " CHF " + subProduct.price.toString());
                if (!freeExtraUsed && freeExtras > 0 && subProduct.discount == null) {
                    subProduct.discount = new Discount();
                    subProduct.discount.name = "Free Extra!";
                    subProduct.discount.value = subProduct.price;
                    freeExtraUsed = true;
                    freeExtras--;
                }
                if (subProduct.discount != null) {
                    writer.accept(subProduct.discount.name + " CHF -" + subProduct.discount.value.toString());
                    totalDiscount = totalDiscount.add(subProduct.discount != null ?
                            subProduct.discount.value : BigDecimal.ZERO);
                }
            }

        }


        for (final Product product : basket) {
            if (freeBeverages < 1) break;

            if (!"beverage".equals(product.type)) continue;

            boolean hasDiscount = product.discount != null ||
                    product.subProducts.stream().anyMatch(item -> item.discount != null);
            if (hasDiscount) continue;

            product.discount = new Discount();
            product.discount.name = "Free Beverage! Collect more Stamps!";
            product.discount.value = product.price;
            totalDiscount = totalDiscount.add(product.discount.value);

            writer.accept(product.discount.name + " CHF -" +
                    product.discount.value.toString());

            freeBeverages--;
            beverageStamps -= 4;
        }


        long stamps = basket.stream()
                .filter(item -> "beverage".equals(item.type))
                .filter(item -> item.discount == null)
                .filter(item -> item.subProducts.stream()
                        .filter(sub -> sub.discount != null).count() < 1)
                .count();

        writer.accept(new String(new byte[70]).replaceAll("\0", "-"));
        writer.accept("Total: " + total.subtract(totalDiscount).toString());

        if (stamps > 0) {
            writer.accept("You collected " + stamps + " loyalty stamps!");
        }
        writer.accept("Thanks for visiting Charlenes Coffee Corner");

    }


    public void setWriter(Consumer<String> writer) {
        this.writer = writer;
    }

    private static Product create(final Product p, Set<String> extras) {
        Product orderedItem = new Product();
        orderedItem.name = p.name;
        orderedItem.price = p.price;
        orderedItem.type = p.type;

        if (extras != null) {
            p.subProducts.stream()
                    .filter(prd -> extras.contains(prd.key))
                    .forEach(item -> orderedItem.subProducts.add(create(item, null)));
        }


        return orderedItem;
    }

    private static class Product {
        public String key;
        public String name;
        public String type;
        public BigDecimal price;
        public List<Product> subProducts = new LinkedList<>();
        public Discount discount;
    }

    private static class Discount {
        public String name;
        public BigDecimal value;
    }

}
