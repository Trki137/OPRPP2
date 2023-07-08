package hr.fer.zemris.java.scripting.elems;

/**
 * Represents string element
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class ElementString extends Element{
    private String value;

    /**
     * Constructor for ElementString
     *
     * @param value value of element string
     */

    public ElementString(String value) {
        super();
        this.value = value;
    }

    /**
     * Returns value
     *
     * @return value
     */

    public String asText(){
        return value;
    }
}
