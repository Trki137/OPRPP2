package hr.fer.zemris.java.scripting.elems;

/**
 * Represents operator element
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class ElementOperator extends Element{

    /**
     * Represent symbol that is stored
     */
    private String symbol;

    /**
     * Constructor for ElementOperator
     *
     * @param symbol which we convert from Character to String
     */
    public ElementOperator(Character symbol){
        super();
        this.symbol = Character.toString(symbol);
    }

    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns symbol
     *
     * @return String symbol
     */
    public String asText(){
        return symbol;
    }
}
