package hr.fer.zemris.java.scripting.nodes;

public interface Visitable {
    public void accept(INodeVisitor visitor);
}
