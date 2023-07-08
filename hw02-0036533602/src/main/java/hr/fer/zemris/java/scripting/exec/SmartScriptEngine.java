package hr.fer.zemris.java.scripting.exec;

import hr.fer.zemris.java.scripting.elems.*;
import hr.fer.zemris.java.scripting.exec.functions.ScripterFunction;
import hr.fer.zemris.java.scripting.exec.functions.impl.*;
import hr.fer.zemris.java.scripting.nodes.*;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.util.*;

/**
 * This class represents engine for parsing smsrc files
 */

public class SmartScriptEngine {
    /**
     * Main documnet node of our smsrc file
     */
    private DocumentNode documentNode;
    /**
     * Instance of {@link RequestContext}
     */
    private RequestContext requestContext;
    /**
     * Multistack for our engine
     */
    private ObjectMultistack multistack = new ObjectMultistack();

    /**
     *  Map of all functions and it corresponding actions
     */
    private final Map<String, ScripterFunction> scripterFunctionMap;

    private INodeVisitor visitor = new INodeVisitor() {
        @Override
        public void visitTextNode(TextNode node) {
            try {
                requestContext.write(node.getText());
            }catch (IOException e){
                System.out.println("Problem with writing text node");
            }
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            multistack.push(node.getVariable().asText(),new ValueWrapper(node.getStartExpression().asText()));
            ValueWrapper end = new ValueWrapper(node.getEndExpression().asText());
            ValueWrapper step = new ValueWrapper(node.getStepExpression().asText());

            while(end.numCompare(multistack.peek(node.getVariable().asText()).getValue()) >= 0){
                for(int i = 0; i < node.getChildren().size(); i++){
                    node.getChild(i).accept(this);
                }
                multistack.peek(node.getVariable().asText()).add(step.getValue());
            }

        }

        @Override
        public void visitEchoNode(EchoNode node) {
            Stack<ValueWrapper> temporaryStack = new Stack<>();
            for(Element element : node.getElements()){
                if(element instanceof ElementString elementString)
                    temporaryStack.push(new ValueWrapper(elementString.asText()));
                if(element instanceof ElementConstantInteger elementConstantInteger)
                    temporaryStack.push(new ValueWrapper(elementConstantInteger.getValue()));
                if(element instanceof ElementConstantDouble elementConstantDouble)
                    temporaryStack.push(new ValueWrapper(elementConstantDouble.getValue()));
                if(element instanceof ElementVariable elementVariable){
                    Object value = multistack.peek((elementVariable.getName())).getValue();
                    temporaryStack.push(new ValueWrapper(value));
                }
                if(element instanceof ElementOperator elementOperator){
                    String symbol = elementOperator.getSymbol();

                    ValueWrapper secondParam = temporaryStack.pop();
                    ValueWrapper firstParam = temporaryStack.pop();

                    switch (symbol){
                        case "+" -> firstParam.add(secondParam.getValue());
                        case "-" -> firstParam.subtract(secondParam.getValue());
                        case "/" -> firstParam.divide(secondParam.getValue());
                        case "*" -> firstParam.multiply(secondParam.getValue());
                        default -> throw new IllegalArgumentException("Invalid symbol for operations");
                    }

                    temporaryStack.push(firstParam);
                  }

                if(element instanceof ElementFunction elementFunction){
                    scripterFunctionMap.get(elementFunction.getName().substring(1)).execute(temporaryStack,requestContext);
                }

            }

            if(temporaryStack.isEmpty()) return;

            List<ValueWrapper> leftoverList = new ArrayList<>();
            while(!temporaryStack.isEmpty()){
                leftoverList.add(temporaryStack.pop());
            }
            try {
                for (int i = leftoverList.size() - 1; i >= 0; i--) {
                    String toWrite = leftoverList.get(i).toString();
                    toWrite = toWrite.replace("\\\\r\\\\n", "\r\n");
                    requestContext.write(toWrite);
                }
            }catch (IOException e){
                System.out.println("Something went wrong while writing leftovers");
            }
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            for(int i = 0;  i < node.numberOfChildren(); i++)
                node.getChild(i).accept(this);
        }
    };

    public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext){
        this.documentNode = documentNode;
        this.requestContext = requestContext;
        this.scripterFunctionMap = new HashMap<>();

        scripterFunctionMap.put("sin", new SinFunction());
        scripterFunctionMap.put("decfmt", new FormatFunction());
        scripterFunctionMap.put("dup", new DupFunction());
        scripterFunctionMap.put("swap", new SwapFunction());
        scripterFunctionMap.put("setMimeType", new SetMimeTypeFunction());
        scripterFunctionMap.put("paramGet", new ParamGetFunction());
        scripterFunctionMap.put("pparamGet", new PersistentParamGet());
        scripterFunctionMap.put("pparamSet", new PersistentParamSet());
        scripterFunctionMap.put("pparamDel", new PersistentParamDel());
        scripterFunctionMap.put("tparamGet", new TemporaryParamGet());
        scripterFunctionMap.put("tparamSet", new TemporaryParamSet());
        scripterFunctionMap.put("tparamDel", new TemporaryParamDel());

    }

    public void execute(){
        documentNode.accept(visitor);
    }
}
