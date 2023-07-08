package hr.fer.zemris.java.scripting.exec.functions.impl;

import hr.fer.zemris.java.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.scripting.exec.functions.ScripterFunction;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;
/**
 * This class implements {@link ScripterFunction} that sets new temporary parameter for context
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class TemporaryParamSet implements ScripterFunction {
    @Override
    public void execute(Stack<ValueWrapper> multistack, RequestContext context) {
        String name = (String) multistack.pop().getValue();

        String value;
        Object objectValue = multistack.pop().getValue();
        if(objectValue instanceof Integer) value = Integer.toString((Integer) objectValue);
        else if(objectValue instanceof Double) value = Double.toString((Double) objectValue);
        else value = (String) objectValue;

        context.setTemporaryParameters(name,value);
    }
}