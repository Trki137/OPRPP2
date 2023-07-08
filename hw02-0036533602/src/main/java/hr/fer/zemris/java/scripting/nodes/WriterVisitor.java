package hr.fer.zemris.java.scripting.nodes;

public class WriterVisitor implements INodeVisitor{

    @Override
    public void visitTextNode(TextNode node) {
        System.out.println(node.toString());
    }

    @Override
    public void visitForLoopNode(ForLoopNode node) {
        System.out.println(node.toString());
    }

    @Override
    public void visitEchoNode(EchoNode node) {
        System.out.println(node.toString());
    }

    @Override
    public void visitDocumentNode(DocumentNode node) {
        for(int i = 0; i < node.numberOfChildren(); i++){
            node.getChild(i).accept(this);
        }
    }
}
