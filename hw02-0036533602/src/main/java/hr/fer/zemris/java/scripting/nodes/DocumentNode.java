package hr.fer.zemris.java.scripting.nodes;

/**
 * Root node of a document
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class DocumentNode extends Node{

    /**
     * Document tree represent as String.
     * Returns similar String as input which we parsed
     *
     * @return String document tree
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < this.numberOfChildren(); i++){
            sb.append(this.getChild(i).toString());
        }
        return sb.toString();
    }

    /**
     *
     * @param obj another Document node
     * @return true if 2 document node are same or else false
     */

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof DocumentNode)) return false;

        DocumentNode other = (DocumentNode) obj;

        if(other.numberOfChildren() != this.numberOfChildren()) return false;
        for(int i = 0; i < this.numberOfChildren(); i++){
            Node ch1 = other.getChild(i);
            Node ch2 = this.getChild(i);
            if(!ch1.equals(ch2)) return false;
        }

        return true;
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitDocumentNode(this);
    }
}
