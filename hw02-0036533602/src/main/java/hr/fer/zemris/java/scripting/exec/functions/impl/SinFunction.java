package hr.fer.zemris.java.scripting.exec.functions.impl;

import hr.fer.zemris.java.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.scripting.exec.functions.ScripterFunction;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Objects;
import java.util.Stack;
/**
 * This class implements {@link ScripterFunction} that calculates sin function, and it's result stores on stack
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class SinFunction implements ScripterFunction {
    @Override
    public void execute(Stack<ValueWrapper> multistack, RequestContext context) {
        Double x;
        Object value = multistack.pop().getValue();
        if(value instanceof Integer) x = ((Integer)value).doubleValue();
        else x =(Double) value;

        double res = Math.sin(x);
        multistack.push(new ValueWrapper(res));
    }
}
