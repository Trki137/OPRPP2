package hr.fer.zemris.java.scripting.elems;

/**
 * Represents function element
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class ElementFunction extends Element{
    /**
     * name of the function
     */
    private String name;

    /**
     * Constructor for ElementFunction
     *
     * @param name name of the function
     */
    public ElementFunction(String name){
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns name of function
     *
     * @return String name of function
     */
    public String asText(){
        return name;
    }
}
