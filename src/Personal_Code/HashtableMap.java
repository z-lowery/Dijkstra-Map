package src.Personal_Code;

import src.Provided_Code.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.security.Key;
import java.util.*;

public class HashtableMap<KeyType, ValueType> implements MapADT {

    protected LinkedList<Pair>[] table; // a one-dimensional array with type LinkedList<Pair>

    protected class Pair {

        public KeyType key;
        public ValueType value;

        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }

    }

    // Constructors
    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity){
        /*
         * Java doesn't allow direct creation of arrays with generic types due to type
         * erasure at runtime. So we create a ray LinkedList before casting it to
         * LinkedList<Pair>[] to obtain the desired behavior (the array holding references
         * to LinkedList<Pair> objects).
         */
        table = (LinkedList<Pair>[]) new LinkedList[capacity];
    }

    public HashtableMap(){ // with default capacity = 64
        this(64);
    }

    /**
     * Adds a new key,value pair/mapping to this collection.
     * @param key the key of the key,value pair
     * @param value the value that key maps to
     * @throws NullPointerException if key is null
     * @throws IllegalArgumentException if key already maps to a value
     */
    @Override
    public void put(Object key, Object value) throws IllegalArgumentException {
        if(key == null){
            throw new NullPointerException("null keys are not allowed");
        }
        if(containsKey(key)){
            throw new IllegalArgumentException("table already contains key object [" + key + "]");
        }

        // get hash
        int hash = getHash(key, table.length);;
        // initialize a new linkedList if list is null at table[hash]
        if (table[hash] == null) {
            table[hash] = new LinkedList<>();
        }

        // add Pair object to table[hash]
        table[hash].add(new Pair((KeyType) key, (ValueType) value));

        // if load factor is exceeded, double capacity and re-hash all elements in table
        if(((double) getSize() / getCapacity()) >= 0.80){
            increaseCapacity();
        }
    }

    /**
     * Double capacity and re-hash all elements in table field
     */
    public void increaseCapacity(){
        LinkedList<Pair>[] newList = new LinkedList[getCapacity() * 2];
        int hash;

        for (LinkedList<Pair> list : table) {
            if(list != null) {
                for (Pair pair : list) {
                    hash = getHash(pair.key, newList.length);
                    if (newList[hash] == null) {
                        newList[hash] = new LinkedList<>();
                    }
                    newList[hash].add(new Pair(pair.key, pair.value));
                }
            }
        }

        table = newList;
    }

    /**
     * Get the hash from an object key
     * @param key object to be hashed
     * @param capacity is the capacity of the table
     * @return hash
     */
    public int getHash(Object key, int capacity){
        return Math.abs(key.hashCode() % capacity);
    }

    /**
     * Checks whether a key maps to a value in this collection.
     * @param key the key to check
     * @return true if the key maps to a value, and false is the
     *         key doesn't map to a value
     */
    @Override
    public boolean containsKey(Object key) {
        if(key == null){
            throw new NullPointerException("null keys are not allowed");
        }
        // move through each pair object in the linked list located in the table
        int hash = getHash(key, table.length);
        if(table[hash] != null) {
            for (Pair pair : table[hash]) {
                if (pair.key.equals(key)) {
                    return true; // if the key is found, return true
                }
            }
        }

        return false; // return false when the key was never found
    }

    /**
     * Retrieves the specific value that a key maps to.
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    @Override
    public Object get(Object key) throws NoSuchElementException {
        if(key == null){
            throw new NullPointerException("null keys are not allowed");
        }
        // move through each pair object in the linked list located in the table
        int hash = getHash(key, table.length);;
        if(table[hash] != null) {
            for (Pair pair : table[hash]) {
                if (pair.key.equals(key)) {
                    return pair.value; // if the key is found, return the value associated with that key
                }
            }
        }

        throw new NoSuchElementException("Key [" + key + "] " + "not found in table when using get()");
    }

    /**
     * Remove the mapping for a key from this collection.
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    @Override
    public Object remove(Object key) throws NoSuchElementException {
        if(key == null){
            throw new NullPointerException("null keys are not allowed");
        }

        // move through each pair object in the linked list located in the table
        int hash = getHash(key, table.length);;

        if(table[hash] != null) {
            for (int i = 0; i < table[hash].size(); i++) {
                Pair pair = table[hash].get(i);
                if (pair.key.equals(key)) {
                    table[hash].remove(i); // if the key is found, remove object with that key
                    return pair.value; // return value associated with the removed object
                }
            }
        }

        throw new NoSuchElementException("Key [" + key + "] " + "not found in table when using remove()");
    }

    /**
     * Moves through each element in the hashtable and returns 
     * a linked list of all keys in the table
     * @return a LinkedList containing all keys in the Hashtable
     */
    public LinkedList<KeyType> getKeys(){
        LinkedList<KeyType> keyList = new LinkedList<>(); // linked list that will store all keys in the hashtable

        for (int i = 0; i < table.length; i++) { // for each element in the hashtable array
            if(table[i] != null){ // if there is a list at the index i
                for (Pair pair : table[i]) { // take every Pair object in the list and add it to keyList
                    keyList.add(pair.key);
                }
            }
        }

        return keyList;
    }

    /**
     * Removes all key,value pairs from this collection.
     */
    @Override
    public void clear(){
        // iterate through each index in the table array and set each linked list to null to clear the table
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    /**
     * Retrieves the number of keys stored in this collection.
     * @return the number of keys stored in this collection
     */
    @Override
    public int getSize(){
        int size = 0;
        for (int i = 0; i < table.length; i++) {
            if(table[i] != null){
                size += table[i].size();
            }
        }

       return size;
    }

    /**
     * Retrieves this collection's capacity.
     * @return the size of te underlying array for this collection
     */
    @Override
    public int getCapacity(){
        return table.length;
    }


        /**
         * Tests the constructors and basic implementation of some methods:
         *      1) creation of a Hashtable object with default capacity
         *      2) getCapacity() and getSize() methods upon creation
         *      and
         *      3) creation of a Hashtable object with custom capacity
         *      4) getCapacity() and getSize() methods upon creation
         */
        @Test
        public void test_constructors() {
            HashtableMap<Double, Integer> defaultMap = new HashtableMap<>();
            Assertions.assertEquals(64, defaultMap.getCapacity());
            Assertions.assertEquals( 0, defaultMap.getSize());

            HashtableMap<Double, Integer> customMap = new HashtableMap<>(32);
            Assertions.assertEquals(32, customMap.getCapacity());
            Assertions.assertEquals(0, customMap.getSize());
        }

        /**
         * Tests the implementation of the put() method and get() method
         */
        @Test
        public void test_put() {
            HashtableMap<Integer, Integer> map = new HashtableMap<>();
            map.put(444555, 40);
            Assertions.assertEquals(1, map.getSize()); // after a put() call, size should increase by 1
            Assertions.assertTrue(map.containsKey(444555));
            Assertions.assertEquals(40, map.get(444555)); // tests get() method to obtain value associated with object key

            // put() should throw an IllegalArgumentException if table already contains a Pair object with the same key
            Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
                map.put(444555,13);
            });

            // get() should throw a NoSuchElementException if the table does not contain an object with the given key
            Assertions.assertThrowsExactly(NoSuchElementException.class, () -> {
                map.get(23947);
            });
        }

        /**
         * Tests the implementation of the increaseCapacity() method
         */
        @Test
        public void test_increaseCapacity() {
            HashtableMap<Integer, Integer> map = new HashtableMap<>();
            // capacity should only increase (call growTable) after 52 insertions, so check that capacity stays after 51
            // 60 * 0.8 = 51.2 -> where 0.8 is the load factor (increase capacity when capacity reaches 80% or more)
            for (int i = 0; i < 51; i++) {
                map.put(i,i*i);
            }
            Assertions.assertEquals(64, map.getCapacity());
            // check for an increase after one more call of put (total of 52)
            map.put(52,52*52);
            Assertions.assertEquals(128, map.getCapacity());

            Assertions.assertEquals(52, map.getSize());
        }

        /**
         * Tests the implementation of the remove() method
         */
        @Test
        public void test_remove() {
            HashtableMap<Double, Integer> map = new HashtableMap<>();
            map.put(12346, 3);
            map.put(12345, 4);
            map.put(12341, 5);
            Assertions.assertEquals(3, map.getSize()); // test size

            map.remove(12345);
            Assertions.assertEquals(2, map.getSize()); // test size after removal

            // remove() should throw a NoSuchElementException if the table does not contain an object with the given key
            Assertions.assertThrowsExactly(NoSuchElementException.class, () -> {
                map.remove(12345);
            });
        }

        /**
         * Tests the implementation of the test_clear() method
         */
        @Test
        public void test_clear() {
            HashtableMap<Double, Integer> map = new HashtableMap<>();
            for (int i = 0; i < 128; i++) {
                map.put(i,i*i);
            }
            map.clear();
            Assertions.assertEquals(0, map.getSize());

    }
}