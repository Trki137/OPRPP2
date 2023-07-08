package hr.fer.zemris.java.scripting.nodes;

public interface INodeVisitor {
    public void visitTextNode(TextNode node);
    public void visitForLoopNode(ForLoopNode node);
    public void visitEchoNode(EchoNode node);
    public void visitDocumentNode(DocumentNode node);
}
