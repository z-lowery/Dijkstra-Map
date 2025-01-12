package Tests;

import src.HashtableMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.*;

public class HashtableMapTests {
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
