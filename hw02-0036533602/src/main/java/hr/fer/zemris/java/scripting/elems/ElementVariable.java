package hr.fer.zemris.java.scripting.elems;

/**
 * Represents variable element
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class ElementVariable extends Element {
    /**
     * Name of variable
     */
    private String name;

    /**
     * Constructor for ElementVariable
     *
     * @param name name of variable
     */
    public ElementVariable(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns name of variable
     * @return name
     */

    @Override
    public String asText(){
        return name;
    }
}
