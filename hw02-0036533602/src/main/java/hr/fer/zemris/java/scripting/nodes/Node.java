package hr.fer.zemris.java.scripting.nodes;

import hr.fer.zemris.java.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.scripting.parser.SmartScriptParserException;

/**
 * Base class for all other nodes in this package
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class Node implements Visitable {
    /**
     * Children of a node
     */
    private ArrayIndexedCollection children;

    /**
     * Default constructor, sets children to null
     */
    public Node() {
        this.children = null;
    }

    /**
     * Adds a child to a node
     *
     * @param child node that will be child of current node
     */
    public void addChildNode(Node child){
        if(children == null) children = new ArrayIndexedCollection();
        children.add(child);
    }

    /**
     * Returns number of children that node has
     *
     * @return int number of children that node has
     */

    public int numberOfChildren(){
        return children == null ? 0 :children.size();
    }

    /**
     * Return Node that is in position {@index}
     *
     * @param index position of node
     * @return Node child that is at position {@param index}
     * @throws SmartScriptParserException if there are no children
     * @throws IndexOutOfBoundsException if index is smaller than 0 or bigger than number of children
     */
    public Node getChild(int index){
        if(children == null) throw new SmartScriptParserException("There are no children in this node");
        if(index >= children.size() || index < 0) throw new IndexOutOfBoundsException("Invalid index of child");
        return (Node) children.get(index);
    }

    public ArrayIndexedCollection getChildren() {
        return children;
    }

    @Override
    public void accept(INodeVisitor visitor) {

    }
}
