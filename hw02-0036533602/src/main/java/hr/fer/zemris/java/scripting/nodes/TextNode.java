package hr.fer.zemris.java.scripting.nodes;

import java.util.Objects;

/**
 * Represent node that is text
 */
public class TextNode extends Node{
    /**
     * Value of TextNode
     */
    private String text;

    /**
     * Constructor of TextNode
     *
     * @param text value of TextNode
     */
    public TextNode(String text) {
        super();
        this.text = text;
    }

    public String getText() {
        return text;
    }

    /**
     * Returns text that was read in String when parsed
     *
     * @return String text that was read in String when parsed
     */
    @Override
    public String toString() {
        return this.text.replace("\\", "\\\\").replace("{", "\\{");
    }

    /**
     * Check if 2 TextNode object are equal
     *
     * @param obj the value we compare TextNode object
     * @return true if they are equal else returns false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof TextNode)) return false;

        TextNode other = (TextNode) obj;

        return other.text.equals(this.text);
    }

    /**
     * Return hash of object
     *
     * @return int hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.text);
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitTextNode(this);
    }
}
