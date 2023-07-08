package hr.fer.zemris.java.collections;

/**
 * Processor class is base class which will be used do to sam simple job
 * Has only one method process
 *
 * @author Dean Trkulja
 * @version 1.0
 *
 */
public interface Processor {

    /**
     * Method takes one parameter and will process it in some way
     *
     * @param value element which will be used in some action
     */
    public void process(Object value);

}