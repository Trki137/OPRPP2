package hr.fer.zemris.java.scripting.nodes;

import hr.fer.zemris.java.scripting.elems.Element;
import hr.fer.zemris.java.scripting.elems.ElementVariable;

import java.util.Objects;

/**
 * Node for for loop tag
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class ForLoopNode extends Node{
    /**
     * First variable of for loop
     */
    ElementVariable variable;
    /**
     * Starting value of for loop
     */
    Element startExpression;
    /**
     * Ending value of for loop
     */
    Element endExpression;
    /**
     * Step value of for loop
     */
    Element stepExpression;

    /**
     * Constructor for ForLoopNode
     *
     * @param variable first variable
     * @param startExpression Starting value
     * @param endExpression Ending value
     * @param stepExpression Step value
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression){
        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    public ElementVariable getVariable() {
        return variable;
    }

    public Element getStartExpression() {
        return startExpression;
    }

    public Element getEndExpression() {
        return endExpression;
    }

    public Element getStepExpression() {
        return stepExpression;
    }

    /**
     * Constructor that delegates job to other constructor
     *
     * @param variable First variable
     * @param startExpression Starting value
     * @param endExpression Ending value
     */

    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression){
        this(variable,startExpression,endExpression,null);
    }

    /**
     * Returns for loop that was read in original document
     *
     * @return String for loop that was read in original document
     */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{$FOR ");

        sb.append(variable.asText()).append(" ");
        sb.append(startExpression.asText()).append(" ");
        sb.append(endExpression.asText()).append(" ");
        if(stepExpression != null)
            sb.append(stepExpression.asText()).append(" ");

        sb.append("$}");

        for(int i = 0; i < this.numberOfChildren(); i++)
            sb.append(getChild(i).toString());

        sb.append("{&END$}");

        return sb.toString();
    }

    /**
     * Checks if 2 ForLoopNodes are equal
     *
     * @param obj the value we compare ForLoopNode object
     * @return true if they are equal or else false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof ForLoopNode)) return false;

        ForLoopNode other = (ForLoopNode) obj;

        if(!other.variable.equals(this.variable)) return false;
        if(!other.stepExpression.equals(this.stepExpression)) return false;
        if(!other.endExpression.equals(this.endExpression)) return false;
        if(!other.stepExpression.equals(this.stepExpression)) return false;

        return true;
    }

    /**
     * Return hash of object
     *
     * @return int hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(variable,endExpression,stepExpression,startExpression);
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitForLoopNode(this);
    }
}
