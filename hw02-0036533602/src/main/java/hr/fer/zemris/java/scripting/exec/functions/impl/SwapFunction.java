package hr.fer.zemris.java.scripting.exec.functions.impl;

import hr.fer.zemris.java.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.scripting.exec.functions.ScripterFunction;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;
/**
 * This class implements {@link ScripterFunction} that swaps top 2 values on stcak
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class SwapFunction implements ScripterFunction {
    @Override
    public void execute(Stack<ValueWrapper> multistack, RequestContext context) {
        ValueWrapper first = multistack.pop();
        ValueWrapper second  = multistack.pop();

        multistack.push(first);
        multistack.push(second);
    }
}
