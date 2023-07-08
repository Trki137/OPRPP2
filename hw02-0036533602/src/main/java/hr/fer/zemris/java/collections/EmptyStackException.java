package hr.fer.zemris.java.collections;

/**
 * Implementation of own Runtime exception
 * Will use for our stack implementation
 *
 * @author Dean Trkulja
 * @version 1.0
 *
 */
public class EmptyStackException extends RuntimeException{

    /**
     * Default constructor
     */
    public EmptyStackException(){
        super();
    }

    /**
     * Constructor with message problem
     *
     * @param description for message to user
     */
    public EmptyStackException(String description){
        super(description);
    }
}
