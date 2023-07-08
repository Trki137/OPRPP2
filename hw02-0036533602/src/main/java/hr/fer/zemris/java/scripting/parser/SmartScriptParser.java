package hr.fer.zemris.java.scripting.parser;

import hr.fer.zemris.java.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.collections.ObjectStack;
import hr.fer.zemris.java.scripting.elems.*;
import hr.fer.zemris.java.scripting.lexer.LexerState;
import hr.fer.zemris.java.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.scripting.lexer.TokenType;
import hr.fer.zemris.java.scripting.nodes.*;

import java.util.Arrays;

/**
 * Implementation of parser
 */
public class SmartScriptParser {
    /**
     * Helps us with building document tree
     */
    private ObjectStack documentTree;
    /**
     * SmartScriptLexer that we will use
     */
    private SmartScriptLexer smartScriptLexer;

    /**
     * Contructor of SmartScriptParser
     *
     * @param documnetBody text that parser will try to parse
     */

    public SmartScriptParser(String documnetBody){
        smartScriptLexer = new SmartScriptLexer(documnetBody);
        documentTree = new ObjectStack();
        parse();
    }

    /**
     * Return root of document tree
     *
     * @return DocumentNode
     */

    public DocumentNode getDocumentNode() {
        return (DocumentNode) documentTree.peek();
    }

    /**
     * Method that parses whole document
     */

    private void parse(){
        documentTree.push(new DocumentNode());

        while(true){
            smartScriptLexer.nextToken();
            TokenType type = smartScriptLexer.getToken().getType();
            if(type == TokenType.EOF)
                break;

            if(type == TokenType.TAG_START){
                smartScriptLexer.setState(LexerState.TAG);
            }
            if(type == TokenType.STRING){
                String text = (String) smartScriptLexer.getToken().getValue();
                Node root = (Node) documentTree.peek();
                root.addChildNode(new TextNode(text));
            }
            if(type == TokenType.IDN){
                if(smartScriptLexer.getToken().getValue().equals("FOR"))
                    forLoopParser();
                else if(smartScriptLexer.getToken().getValue().equals("="))
                    equalsParser();
                else if(smartScriptLexer.getToken().getValue().equals("END")){
                    documentTree.pop();
                    smartScriptLexer.nextToken();
                    smartScriptLexer.setState(LexerState.BASIC);
                }

                else throw new SmartScriptParserException("Can't parse this document");
            }
            if(!documentTree.isEmpty()) throw new SmartScriptParserException("Can't parse this document because there are to many END tags");

        }
    }

    /**
     * Creates ForLoopNode and attaches to parent
     */
    private void forLoopParser(){
        ArrayIndexedCollection elements = getAllElements();

        if(elements.size() < 3 || elements.size() > 4) throw new SmartScriptParserException("Coulnd't parse for loop");
        Element element = elements.size() == 4 ? (Element) elements.get(3) : null;

        ForLoopNode forLoopNode = new ForLoopNode(
                (ElementVariable) elements.get(0),
                (Element) elements.get(1),
                (Element) elements.get(2),
                element
        );

        ((Node)documentTree.peek()).addChildNode(forLoopNode);
        documentTree.push(forLoopNode);
    }

    /**
     * Creates EchoNode and attaches to parent
     */
    private void equalsParser(){
        ArrayIndexedCollection elements = getAllElements();

       Node root = (Node) documentTree.peek();
       root.addChildNode(new EchoNode(Arrays.copyOf(elements.toArray(), elements.size(), Element[].class)));
    }

    /**
     * Helper function to avoid duplicating code.
     * Stores all elements until TAG_END
     *
     * @return ArrayIndexCollection an array of Elements
     */

    private ArrayIndexedCollection getAllElements(){
        ArrayIndexedCollection elements = new ArrayIndexedCollection();

        smartScriptLexer.nextToken();

        while(smartScriptLexer.getToken().getType() != TokenType.TAG_END){
            TokenType type = smartScriptLexer.getToken().getType();
            Object value = smartScriptLexer.getToken().getValue();
            if(type == TokenType.IDN) elements.add(new ElementVariable((String) value));
            if(type == TokenType.FUNCTION) elements.add(new ElementFunction((String) value));
            if(type == TokenType.STRING) elements.add(new ElementString((String) value));
            if(type == TokenType.DOUBLE) elements.add(new ElementConstantDouble((Double) value));
            if(type == TokenType.INTEGER) elements.add(new ElementConstantInteger((Integer) value));
            if(type == TokenType.OPERATOR) elements.add(new ElementOperator((Character) value));
            if(type == TokenType.EOF) throw new SmartScriptParserException("Can't parse equals");

            smartScriptLexer.nextToken();
        }
        smartScriptLexer.setState(LexerState.BASIC);
        return  elements;
    }

}
