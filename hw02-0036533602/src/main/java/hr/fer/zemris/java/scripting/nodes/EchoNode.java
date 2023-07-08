package hr.fer.zemris.java.scripting.nodes;

import hr.fer.zemris.java.scripting.elems.Element;

import java.util.Arrays;

/**
 * Represents some expression
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class EchoNode extends Node{
    /**
     * all elements that were read in original document after =
     */
    Element[] elements;

    public EchoNode(Element[] elements){
        this.elements = Arrays.copyOf(elements, elements.length);
    }

    /**
     *
     * @return String that was read after = tag in original document
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{$= ");

        for(int i = 0; i < this.elements.length; i++)
            sb.append(this.elements[i].asText()).append(" ");

        sb.append("$}");
        return sb.toString();
    }

    public Element[] getElements() {
        return elements;
    }

    /**
     * Check if 2 EchoNode object are equal
     *
     * @param obj the value we compare EchoNode object
     * @return true if they are equal else returns false
     */

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof EchoNode)) return false;

        EchoNode other = (EchoNode) obj;

        if(other.elements.length != this.elements.length) return false;

        return Arrays.equals(this.elements, other.elements);
    }

    /**
     * Return hash of object
     *
     * @return int hash
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.elements);
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitEchoNode(this);
    }
}
