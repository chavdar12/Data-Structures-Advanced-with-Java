import java.util.*;
import java.util.stream.Collectors;

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
        int size = this.productsByProducer.size();
        int newSIze = 0;

        if (this.productsByProducer.containsKey(producer)) {
            List<Product> removeByProducer =
                    this.productsByProducer
                            .remove(producer)
                            .stream()
                            .filter(product -> product.getName().equals(name))
                            .collect(Collectors.toList());


            newSIze = size - this.productsByProducer.size();
        }

        if (newSIze > 0) {
            return newSIze + " products deleted" + System.lineSeparator();
        } else {
            return "No products found" + System.lineSeparator();
        }
    }

    public String delete(String producer) {

        List<Product> removeByProducer = this.productsByProducer.remove(producer);
        int size = removeByProducer.size();

        if (size > 0) {
            return size + " products deleted" + System.lineSeparator();
        }

        return "No products found" + System.lineSeparator();
    }

    public String findProductsByName(String name) {
        StringBuilder sb = new StringBuilder();

        if (!this.productsByProducer.isEmpty()) {

            for (List<Product> list : productsByProducer.values()) {

                list.stream()
                        .filter(product -> product.getName().equals(name))
                        .sorted(Comparator.comparing(Product::getName)
                                .thenComparing(Product::getProducer)
                                .thenComparing(Product::getPrice))
                        .forEach(product -> sb.append(product).append(System.lineSeparator()));
            }
        }

        String toReturn = sb.toString();

        return getStringToReturn(toReturn);
    }

    public String findProductsByProducer(String producer) {
        StringBuilder sb = new StringBuilder();

        if (this.productsByProducer.containsKey(producer)) {
            this.productsByProducer.get(producer)
                    .stream()
                    .sorted(Comparator.comparing(Product::getName)
                            .thenComparing(Product::getProducer)
                            .thenComparing(Product::getPrice))
                    .forEach(product -> {
                        sb.append(product.toString()).append(System.lineSeparator());
                    });

        }
        String toReturn = sb.toString();

        return getStringToReturn(toReturn);
    }

    public String findProductsByPriceRange(double priceFrom, double priceTo) {
        StringBuilder sb = new StringBuilder();

        if (!this.productsByProducer.isEmpty()) {

            for (List<Product> list : productsByProducer.values()) {

                list.stream()
                        .filter(p -> p.getPrice() >= priceFrom && p.getPrice() <= priceTo)
                        .sorted(Comparator.comparing(Product::getName)
                                .thenComparing(Product::getProducer)
                                .thenComparing(Product::getPrice))
                        .forEach(product -> sb.append(product).append(System.lineSeparator()));
            }
        }

        String toReturn = sb.toString();

        return getStringToReturn(toReturn);
    }

    private String getStringToReturn(String toReturn) {
        if (toReturn.length() > 0) {
            return toReturn;
        } else {
            return "No products found" + System.lineSeparator();
        }
    }
}
