import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShoppingCentreTest {
    private static final String NL = System.lineSeparator();

    private ShoppingCentre centre;

    @Before
    public void setUp() {
        this.centre = new ShoppingCentre();
        this.centre.addProduct("IdeaPad", 500, "Lenovo");
        this.centre.addProduct("ThinkPad", 900, "Lenovo");
        this.centre.addProduct("IdeaPad", 450, "Lenovo");
        this.centre.addProduct("IdeaPad", 300, "Acer");
        this.centre.addProduct("MacBook", 1500, "Apple");
    }

    @Test
    public void addProduct_ShouldReturnMessage() {
        Assert.assertEquals("Product added" + NL, this.centre.addProduct("X", 1, "Y"));
    }

    @Test
    public void deleteByNameAndProducer_ShouldDeleteOnlyMatchingProducts() {
        Assert.assertEquals("2 products deleted" + NL, this.centre.delete("IdeaPad", "Lenovo"));
        Assert.assertEquals("{ThinkPad;Lenovo;900.00}" + NL, this.centre.findProductsByProducer("Lenovo"));
    }

    @Test
    public void deleteByNameAndProducer_NoMatch_ShouldReturnNotFound() {
        Assert.assertEquals("No products found" + NL, this.centre.delete("MacBook", "Lenovo"));
        Assert.assertEquals("No products found" + NL, this.centre.delete("IdeaPad", "Missing"));
    }

    @Test
    public void deleteByProducer_ShouldDeleteAll() {
        Assert.assertEquals("3 products deleted" + NL, this.centre.delete("Lenovo"));
        Assert.assertEquals("No products found" + NL, this.centre.findProductsByProducer("Lenovo"));
    }

    @Test
    public void deleteByProducer_Missing_ShouldReturnNotFound() {
        Assert.assertEquals("No products found" + NL, this.centre.delete("Missing"));
    }

    @Test
    public void findProductsByName_ShouldSortAcrossProducers() {
        Assert.assertEquals(
                "{IdeaPad;Acer;300.00}" + NL + "{IdeaPad;Lenovo;450.00}" + NL + "{IdeaPad;Lenovo;500.00}" + NL,
                this.centre.findProductsByName("IdeaPad"));
    }

    @Test
    public void findProductsByPriceRange_ShouldBeInclusiveAndSorted() {
        Assert.assertEquals(
                "{IdeaPad;Lenovo;500.00}" + NL + "{ThinkPad;Lenovo;900.00}" + NL,
                this.centre.findProductsByPriceRange(500, 900));
        Assert.assertEquals("No products found" + NL, this.centre.findProductsByPriceRange(1, 2));
    }
}
