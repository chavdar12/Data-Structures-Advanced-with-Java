import java.util.*;
import java.util.stream.Stream;

public class ShoppingCentre {
    private Map<String, List<Product>> productsByProducer;

    public ShoppingCentre() {
        this.productsByProducer = new HashMap<>();
    }

    public String addProduct(String name, double price, String producer) {
        Product product = new Product(name, price, producer);

        this.productsByProducer.putIfAbsent(producer, new ArrayList<>());
        this.productsByProducer.get(producer).add(product);

        return "Product added" + System.lineSeparator();

    }

    public String delete(String name, String producer) {
        List<Product> products = this.productsByProducer.get(producer);
        int deleted = 0;

        if (products != null) {
            int size = products.size();
            products.removeIf(product -> product.getName().equals(name));
            deleted = size - products.size();

            if (products.isEmpty()) {
                this.productsByProducer.remove(producer);
            }
        }

        return getDeletedMessage(deleted);
    }

    public String delete(String producer) {
        List<Product> removeByProducer = this.productsByProducer.remove(producer);

        return getDeletedMessage(removeByProducer == null ? 0 : removeByProducer.size());
    }

    public String findProductsByName(String name) {
        return getSortedProducts(this.productsByProducer.values()
                .stream()
                .flatMap(List::stream)
                .filter(product -> product.getName().equals(name)));
    }

    public String findProductsByProducer(String producer) {
        return getSortedProducts(this.productsByProducer.getOrDefault(producer, Collections.emptyList()).stream());
    }

    public String findProductsByPriceRange(double priceFrom, double priceTo) {
        return getSortedProducts(this.productsByProducer.values()
                .stream()
                .flatMap(List::stream)
                .filter(p -> p.getPrice() >= priceFrom && p.getPrice() <= priceTo));
    }

    private String getSortedProducts(Stream<Product> products) {
        StringBuilder sb = new StringBuilder();
        products.sorted().forEach(product -> sb.append(product).append(System.lineSeparator()));

        return getStringToReturn(sb.toString());
    }

    private String getDeletedMessage(int deleted) {
        if (deleted > 0) {
            return deleted + " products deleted" + System.lineSeparator();
        }

        return "No products found" + System.lineSeparator();
    }

    private String getStringToReturn(String toReturn) {
        if (toReturn.length() > 0) {
            return toReturn;
        } else {
            return "No products found" + System.lineSeparator();
        }
    }
}
