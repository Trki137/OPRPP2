package hr.fer.zemris.java.scripting.parser;

/**
 * Exception class for parser
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class SmartScriptParserException extends RuntimeException{
    /**
     * Default constructor
     */
    public SmartScriptParserException(){
        super();
    }

    /**
     * Constructor with message
     * @param msg message
     */

    public SmartScriptParserException(String msg){
        super(msg);
    }

}
