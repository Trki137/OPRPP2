package hr.fer.zemris.java.scripting.lexer;

/**
 * Exception class for our lexer
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class LexerException extends RuntimeException{
    /**
     * Default constructor
     */
    public LexerException(){
        super();
    }

    /**
     * Constructor with message
     *
     * @param msg message
     */
    public LexerException(String msg){
        super(msg);
    }
}
