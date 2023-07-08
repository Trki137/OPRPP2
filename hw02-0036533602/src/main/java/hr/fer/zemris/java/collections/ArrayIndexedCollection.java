package hr.fer.zemris.java.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 *
 * @author Dean Trkulja
 * @version 1.0
 *
 *
 * */

public class ArrayIndexedCollection implements List{

    /**
     * Number of element stored in {@code elements}
     * Value can be from 0 to elements lenght
     */
    private int size;
    /**
     * Array which stores values
     */
    private Object[] elements;

    private long modificationCount = 0;

    /**
     * Default size of {@code elements} when it's not specified by user
     */
    private static final int DEFAULT_ARRAY_SIZE = 16;


    /**
     *
     * @param collection elements of this collection are copied into our new collection
     * @param initialSize initial size of our collection
     *
     */
    public ArrayIndexedCollection(Collection collection, int initialSize){
        if(initialSize < 1) throw new IllegalArgumentException();
        if(collection == null) throw new NullPointerException();

        elements = new Object[Math.max(initialSize,collection.size())];
        addAll(collection);

        this.size = collection.size();
    }

    /**
     * This constructor delegates it's job to more complex constructor with default size
     * @param collection elements of this collection are copied into our new collection
     *
     */

    public ArrayIndexedCollection(Collection collection){
        this(collection,DEFAULT_ARRAY_SIZE);
    }

    /**
     * Initializes an array of object
     * @param initialCapacity initial size of our collection
     */

    public ArrayIndexedCollection(int initialCapacity){
        if(initialCapacity < 1) throw new IllegalArgumentException();

        size = 0;
        elements = new Object[initialCapacity];
    }

    /**
     * Default constructor that delegates it's job to more complex constructor
     * It will create a collction that has size of 16
     */
    public ArrayIndexedCollection(){
        this(DEFAULT_ARRAY_SIZE);
    }


    /**
     *
     * Inserts value in specific position
     * Throws 2 exceptions:
     *    - IndexOutOfBoundsException is thrown when position is greater then size of collecton or smaller then 0
     *    - NullPointerException is thrown when null value occurs in {@param value}
     *
     * @param value value that we store in our collection
     * @param position in which position do we want to store element, eligible value is between 0 and current size of collection
     *
     */
    @Override
    public void insert(Object value, int position){
        if(position < 0 || position > size) throw new IndexOutOfBoundsException();
        if(value == null) throw new NullPointerException();

        if(position == size) add(value);
        else{

            Object helpVariable = elements[position];
            Object secondHelpVariable;

            elements[position] = value;

            for(int i = position + 1; i <= size; i++){

                secondHelpVariable = elements[i];
                elements[i] = helpVariable;
                helpVariable = secondHelpVariable;

            }
            modificationCount++;
            size++;

        }
    }

    /**
     * Returns value of collection in position {@param index}
     * Throws IndexOutOfBoundsException when index is smaller then 0 or greater then size
     *
     * @param index specifies index of element that we want to get
     * @return element which is located at position {@param index}
     *
     */
    @Override
    public Object get(int index){
        if(index >= 0 && index <= size-1) return elements[index];
        throw new IndexOutOfBoundsException();
    }

    /**
     * Returns index of element that we are searching
     *
     * @param value element that we are searching for
     * @return -1 if element doesn't exists or value is null
     *          Index of element if elements exists
     */
    @Override
    public int indexOf(Object value){
        if(value == null) return -1;

        for(int i = 0; i < size; i++)
            if(elements[i].equals(value)) return i;

        return -1;
    }

    /**
     * Removes element in a way that shifts element to the left by one position from index value and last element is set to null
     * Throws IllegalArgumentException when index is greater then size - 1 or smaller then 0
     *
     * @param index element which is located at position {@param index} is deleted
     */
    @Override
    public void remove(int index){
        if(index < 0 || index > size - 1) throw new IllegalArgumentException();

        for(int i = index; i < size; i++){

            if(i  == size - 1)
                elements[i] = null;

            elements[i] = elements[i+1];
        }
        modificationCount++;
        size--;
    }

    /**
     *
     * @return size of collection
     */
    @Override
    public int size(){
        return  this.size;
    }

    /**
     * Checks if element exists in collection
     *
     * @param value element that we are searching for in our collection
     * @return true if exists else false
     *
     */
    @Override
    public boolean contains(Object value){
        return indexOf(value) != -1;
    }

    /**
     * Removes first element in collection which has value equal to {@param value}
     *
     * @param value element that we want remove from collection
     * @return true if element is removed in other cases return false
     */
    @Override
    public boolean remove(Object value){

        int index = indexOf(value);
        if (index == -1) return false;
        remove(index);
        return true;
    }



    /**
     * copies our collection to array
     * @return array of objects
     */
    @Override
    public Object[] toArray(){
        return Arrays.copyOf(elements, size);
    }

    /**
     * Adds new element to collection to first available spot in {@code elements}
     * If {@code elements} doesn't have space anymore we create new array with double capacity and copy values from old array to new
     * Throws NullPointerException if {@param value} is null
     *
     * @param value value that we want to store in collection
     */

    @Override
    public void add(Object value){
        if(value == null) throw new NullPointerException();

        if(size >= elements.length){
            Object[] newArray = new Object[elements.length * 2];

            for (int i = 0; i < elements.length; i++)
                newArray[i] = elements[i];

            this.elements = newArray;
        }

        modificationCount++;
        elements[size++] = value;

    }

    /**
     * Clears {@code elements} and sets size to 0
     */
    @Override
    public void clear(){

        for(int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
        modificationCount++;

    }

    /**
     *  To create an object that will get us all elements of ArrayIndexCollection
     *
     * @return ElementsGetter creates and returns object that has implemented ElementsGetter interface
     */

    @Override
    public ElementsGetter createElementsGetter(){
        return new ElementsGetterImpl(this);
    }

    /**
     * @param col
     * @param tester
     */

    @Override
    public void addAllSatisfying(Collection col, Tester tester) {
        ElementsGetter getter = col.createElementsGetter();
        getter.processRemaining( value-> {
            if(tester.test(value)) add(value);
        });
    }

    /**
     * Private class that implements interface ElementsGetter.
     * It will be used to get elements in order from index 0 to index size - 1
     */

    private static class ElementsGetterImpl implements  ElementsGetter{
        /**
         * Reference to ArrayIndexedCollection that we are using
         */
        ArrayIndexedCollection array;
        /**
         * Size of an ArrayIndexedCollection
         */
        int size;
        /**
         * Current index, starts from 0 and goes to size - 1
         */
        int index = 0;

        /**
         * Variable that tracks if list has been modified since creating this object
         */
        long savedModificationCount;

        /**
         * Constructor for ElemntsGetterImpl
         *
         * @param array reference to current ArrayIndexedCollection element
         */
        ElementsGetterImpl(ArrayIndexedCollection array){
            this.array = array;
            this.size = array.size;
            this.savedModificationCount = array.modificationCount;
        }

        /**
         * Checks if there are more elements available
         *
         * @return boolean if there is more elements returns true
         */

        @Override
        public boolean hasNextElement() {
            if(savedModificationCount != array.modificationCount) throw new ConcurrentModificationException();

            return index < size;
        }

        /**
         * Returns next element
         *
         * @return Object returns next element
         */

        @Override
        public Object getNextElement() {
            if(!hasNextElement())
                throw new NoSuchElementException("There are no more elements");

            return array.elements[index++];
        }


        /**
         * All remaining elements are processed by an instance that has implemented interface Processor
         *
         * @param p instance of Processor class
         */
        @Override
        public void processRemaining(Processor p) {
            while (hasNextElement()){
                p.process(getNextElement());
            }
        }
    }


}
