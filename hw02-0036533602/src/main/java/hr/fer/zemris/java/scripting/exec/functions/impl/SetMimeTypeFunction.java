package hr.fer.zemris.java.scripting.exec.functions.impl;

import hr.fer.zemris.java.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.scripting.exec.functions.ScripterFunction;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * This class implements {@link ScripterFunction} that sets mime type of context
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class SetMimeTypeFunction implements ScripterFunction {
    @Override
    public void execute(Stack<ValueWrapper> multistack, RequestContext context) {
        String mimeType = (String) multistack.pop().getValue();

        context.setMimeType(mimeType);
    }
}