package hr.fer.zemris.java.scripting.lexer;

/**
 * Represent tokens that our lexer uses
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class Token {
    /**
     * Type of token
     */
    private TokenType type;
    /**
     * Value of token, it can be null
     */
    private Object value;

    /**
     * Constructor for token
     *
     * @param type type of token
     * @param value value of token
     * @throws NullPointerException if {@param type} is null
     */

    public Token(TokenType type, Object value){
        if(type == null) throw new NullPointerException("Type can't be null");

        this.type = type;
        this.value = value;
    }

    /**
     * Returns token type
     * @return TokenType type of token
     */
    public TokenType getType() {
        return type;
    }

    /**
     * Returns token value
     *
     * @return value token value
     */

    public Object getValue() {
        return value;
    }

}
