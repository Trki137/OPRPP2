package hr.fer.zemris.java.scripting.exec.functions.impl;

import hr.fer.zemris.java.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.scripting.exec.functions.ScripterFunction;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Objects;
import java.util.Stack;
/**
 * This class implements {@link ScripterFunction} that gets temporary parameter from context
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class TemporaryParamGet implements ScripterFunction {
    @Override
    public void execute(Stack<ValueWrapper> multistack, RequestContext context) {
        ValueWrapper dv = multistack.pop();
        String name = (String) multistack.pop().getValue();

        String value = context.getTemporaryParameter(name);
        multistack.push(Objects.isNull(value) ? dv : new ValueWrapper(value));

    }
}