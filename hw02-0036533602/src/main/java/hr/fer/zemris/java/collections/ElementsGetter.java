package hr.fer.zemris.java.collections;

/**
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public interface ElementsGetter {
    /**
     * Checks if there are more elements available
     *
     * @return boolean if there is more elements retuns true
     */
    boolean hasNextElement();

    /**
     * Returns next element
     *
     * @return Object returns next element
     */
    Object getNextElement();

    /**
     * All remaining elements are processed by an instance that has implemented interface Processor
     *
     * @param p instance of Processor class
     */
    void processRemaining(Processor p);
}
