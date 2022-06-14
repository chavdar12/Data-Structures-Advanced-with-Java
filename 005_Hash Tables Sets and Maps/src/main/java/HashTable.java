import java.util.Iterator;
import java.util.LinkedList;

public class HashTable<K, V> implements Iterable<KeyValue<K, V>> {

    private static final int INITIAL_CAPACITY = 16;

    private static final double LOAD_FACTOR = 0.75;

    private int size;

    private int capacity;

    private int maxItems;

    private LinkedList<KeyValue<K, V>>[] hashtable;

    public HashTable() {
        this(INITIAL_CAPACITY);
    }

    @SuppressWarnings(value = "unchecked")
    public HashTable(int capacity) {
        this.hashtable = new LinkedList[capacity];
        this.maxItems = (int) (capacity * LOAD_FACTOR);
        this.capacity = capacity;
    }

    public void add(K key, V value) {
        this.checkForGrow();

        int index = Math.abs(key.hashCode()) % this.hashtable.length;

        if (this.hashtable[index] == null) {
            this.hashtable[index] = new LinkedList<>();
        }

        var linkedList = this.hashtable[index];

        for (var kvp : linkedList) {

            if (kvp.getKey().equals(key)) {

                throw new IllegalArgumentException(String.format("Key %s already exists", key));
            }
        }

        var newKVP = new KeyValue<>(key, value);

        linkedList.addLast(newKVP);

        this.setSize(this.size() + 1);
    }

    public int size() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int capacity() {
        return this.capacity;
    }

    public void addOrReplace(K key, V value) {
        int index = this.getIndex(key);

        if (this.hashtable[index] != null) {

            for (var kvp : this.hashtable[index]) {

                if (kvp.getKey().equals(key)) {

                    kvp.setValue(value);

                    return;
                }
            }
        }

        this.add(key, value);
    }

    public V get(K key) {
        int index = this.getIndex(key);

        if (this.hashtable[index] != null) {

            for (var kvp : this.hashtable[index]) {

                if (kvp.getKey().equals(key)) {

                    return kvp.getValue();
                }
            }
        }

        throw new IllegalArgumentException("Key is not presented in the hashtable");
    }

    public KeyValue<K, V> find(K key) {
        int index = this.getIndex(key);

        if (this.hashtable[index] != null) {

            for (var kvp : this.hashtable[index])

                if (kvp.getKey().equals(key)) {

                    return kvp;
                }
        }

        return null;
    }

    public boolean containsKey(K key) {
        int index = Math.abs(key.hashCode()) % this.hashtable.length;

        if (this.hashtable[index] != null) {

            var linkedList = this.hashtable[index];

            for (var kvp : linkedList) {

                if (kvp.getKey().equals(key)) {

                    return true;
                }
            }
        }

        return false;
    }

    public boolean remove(K key) {
        int index = this.getIndex(key);

        var linkedList = this.hashtable[index];

        if (linkedList != null) {

            for (var kvp : linkedList) {

                if (kvp.getKey().equals(key)) {

                    linkedList.remove(kvp);
                    this.setSize(this.size() - 1);

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Iterator<KeyValue<K, V>> iterator() {
        var hashTableIterator = new LinkedList<KeyValue<K, V>>();

        for (var linkedList : this.hashtable) {

            if (linkedList != null) {

                hashTableIterator.addAll(linkedList);
            }
        }

        return hashTableIterator.iterator();
    }

    private void checkForGrow() {

        if (this.size() >= this.maxItems) {

            this.grow();
        }
    }

    private void grow() {
        this.capacity *= 2;
        this.maxItems = (int) (this.capacity * LOAD_FACTOR);

        var newHashtable = new HashTable<K, V>(capacity);

        for (var kvp : this) {

            newHashtable.add(kvp.getKey(), kvp.getValue());
        }

        this.hashtable = newHashtable.hashtable;
    }

    private int getIndex(K key) {

        return Math.abs(key.hashCode()) % this.hashtable.length;
    }
}