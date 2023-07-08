package hr.fer.zemris.java.scripting.exec.functions.impl;

import hr.fer.zemris.java.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.scripting.exec.functions.ScripterFunction;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;
/**
 * This class implements {@link ScripterFunction} that deletes a temporary parameter from context
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class TemporaryParamDel implements ScripterFunction {
    @Override
    public void execute(Stack<ValueWrapper> multistack, RequestContext context) {
        String name = (String) multistack.pop().getValue();
        context.removeTemporaryParameter(name);
    }
}