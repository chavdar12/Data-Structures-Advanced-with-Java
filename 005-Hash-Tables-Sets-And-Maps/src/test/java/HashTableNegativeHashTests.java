import org.junit.Assert;
import org.junit.Test;

public class HashTableNegativeHashTests {

    @Test
    public void add_KeyWithMinIntHashCode_ShouldWork() {
        HashTable<Integer, String> hashTable = new HashTable<>(10);
        Integer key = Integer.MIN_VALUE; // Math.abs(MIN_VALUE) is still negative; -2^31 % 10 == -8

        hashTable.add(key, "min");

        Assert.assertTrue(hashTable.containsKey(key));
        Assert.assertEquals("min", hashTable.get(key));
        Assert.assertTrue(hashTable.remove(key));
        Assert.assertEquals(0, hashTable.size());
    }
}
