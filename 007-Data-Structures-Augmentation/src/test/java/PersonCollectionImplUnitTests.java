import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

// Runs every PersonCollectionUnitTests case against the fast implementation as well.
public class PersonCollectionImplUnitTests extends PersonCollectionUnitTests {

    @Before
    @Override
    public void setUp() {
        this.people = new PersonCollectionImpl();
    }

    @Test
    public void findAllByNameAndTown_ShouldNotMixConcatenatedKeys() {
        this.people.add("a@x.com", "Ab", 20, "c");
        this.people.add("b@x.com", "A", 20, "bc");

        int count = 0;
        for (Person person : this.people.findAll("Ab", "c")) {
            Assert.assertEquals("a@x.com", person.getEmail());
            count++;
        }

        Assert.assertEquals(1, count);
    }
}
