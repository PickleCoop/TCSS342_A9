public class MyHashTable<Key extends Comparable<Key>, Value> {
    protected Integer capacity = 32768;
    protected Key[] keyBuckets;
    protected Value[] valueBuckets;
    protected Integer size;
    protected MyArrayList<Key> keys;
    public Integer comparisons;
    public Integer maxProbe;

    public MyHashTable(Integer capacity) {
        this.capacity = capacity;
        keyBuckets = (Key[]) new Comparable[this.capacity];
        valueBuckets = (Value[]) new Comparable[this.capacity];
        size = 0;
        keys = new MyArrayList<>();
        comparisons = 0;
        maxProbe = 0;
    }

    private Integer hash(Key key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public Value get(Key key) {
        if (key == null) {
            return null;
        }
        int index = hash(key);
        for (int i = index; i < capacity; i++) {
            if (keyBuckets[i] == null) {
                break;
            }
            if (keyBuckets[i].equals(key)) {
                return valueBuckets[i];
            }
        }
        return null;
    }

    public void put(Key key, Value value) {
        int index = hash(key);
        int probe = 0;
        for (int i = index; i < capacity; i++) {
            probe++;
            comparisons++;
            if (keyBuckets[i] != null && keyBuckets[i].equals(key)) {
                valueBuckets[i] = value;
                return;
            } else if (keyBuckets[i] == null) {
                keyBuckets[i] = key;
                valueBuckets[i] = value;
                size++;
                keys.insert(key, keys.size());
                if (probe > maxProbe) {
                    maxProbe = probe;
                }
                return;
            }
        }
    }

    public Integer size() {
        return size;
    }

    public String toString() {
        StringBuilder temp = new StringBuilder("[");
        boolean firstElement = true;
        for (int i = 0; i < capacity; i++) {
            if (keyBuckets[i] != null) {
                if (!firstElement) {
                    temp.append(", ");
                } else {
                    firstElement = false;
                }
                temp.append(keyBuckets[i]).append(":").append(valueBuckets[i]);
            }
        }
        temp.append("]");
        return temp.toString();
    }
}
