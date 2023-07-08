package hr.fer.zemris.java.scripting.exec.functions;

import hr.fer.zemris.java.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

public interface ScripterFunction {
    void execute(Stack<ValueWrapper> multistack, RequestContext context);
}
