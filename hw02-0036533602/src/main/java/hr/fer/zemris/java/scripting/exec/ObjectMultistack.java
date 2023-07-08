package hr.fer.zemris.java.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represent type of map, but special kind of map where we can store multiple values on same key.
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class ObjectMultistack {

    /**
     * Internal map that will help us with implementation
     */
    private final Map<String, MultistackEntry> map;

    /**
     * Basic constructor
     */
    public ObjectMultistack(){
        this.map = new HashMap<>();
    }

    /**
     * Pushes data into a map.
     * New data pushed will be on top.
     *
     * @param keyName String - key for map
     * @param valueWrapper ValueWrapper - value to be stored
     */

    public void push(String keyName, ValueWrapper valueWrapper){
        MultistackEntry entry = map.get(keyName);
        map.put(keyName, new MultistackEntry(valueWrapper, entry));
    }

    /**
     * Pops value that is on top for key {@code keyName} and removes it from map
     * After removing if there are no more values for that key, key is removed from map.
     *
     * @param keyName String - key for map
     * @throws EmptyStackException if there is no corresponding key in map
     */

    public ValueWrapper pop(String keyName){
        if(isEmpty(keyName))
            throw new EmptyStackException();

        MultistackEntry entry = map.get(keyName);

        if(Objects.isNull(entry.next)) map.remove(keyName);
        else map.put(keyName, entry.next);

        return entry.value;
    }

    /**
     * Pops value that is on top for key {@code keyName} and doesn't remove from map
     *
     * @param keyName String - key for map
     * @throws EmptyStackException if there is no corresponding key in map
     */
    public ValueWrapper peek(String keyName){
        if(isEmpty(keyName))
            throw new EmptyStackException();

        return map.get(keyName).value;
    }

    /**
     * Checks if there is a entry in a map for key {@code keyName}
     *
     * @param keyName String - key for our map
     * @return boolean - if key doesn't exist returns true, if exists then returns true
     */

    boolean isEmpty(String keyName){
        return !map.containsKey(keyName);
    }

    /**
     * Helper class that will represent a Node in {@link ObjectMultistack}
     *
     * @author Dean Trkulja
     * @version 1.0
     */

    public static class MultistackEntry{

        /**
         * Value to be stored
         */
        private final ValueWrapper value;

        /**
         * Reference to next {@link MultistackEntry}, it can be null
         */
        private final MultistackEntry next;

        /**
         * Constructor for {@code MultistackEntry}
         *
         * @param value ValueWrapper - value to be stored
         * @param next MultistackEntry - next {@link MultistackEntry} object or null
         */
        public MultistackEntry(ValueWrapper value, MultistackEntry next){
            this.next = next;
            this.value = value;
        }
    }
}
