package hr.fer.zemris.java.scripting.exec.functions.impl;

import hr.fer.zemris.java.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.scripting.exec.functions.ScripterFunction;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * This class implements {@link ScripterFunction} that duplicates top of stack
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class DupFunction implements ScripterFunction {
    @Override
    public void execute(Stack<ValueWrapper> multistack, RequestContext context) {
        ValueWrapper valueWrapper = multistack.peek();
        multistack.push(new ValueWrapper(valueWrapper.getValue()));
    }
}