package hr.fer.zemris.java.scripting.lexer;

/**
 * Types of token for our lexer
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public enum TokenType {
    /**
     * Identifier, FOR, = or END
     */
    IDN,
    /**
     * End of file
     */
    EOF,
    /**
     * Represent int
     */
    INTEGER,
    /**
     * Represents double
     */
    DOUBLE,
    /**
     * Represents string
     */
    STRING,
    /**
     * Represents function
     */
    FUNCTION,
    /**
     * Represent following operators: + - * / ^
     */
    OPERATOR,
    /**
     * Opening tag -> {&
     */
    TAG_START,
    /**
     * Closing tag -> &}
     */
    TAG_END

}
