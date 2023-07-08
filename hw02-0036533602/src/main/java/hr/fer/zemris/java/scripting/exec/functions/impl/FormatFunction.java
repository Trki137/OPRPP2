package hr.fer.zemris.java.scripting.exec.functions.impl;

import hr.fer.zemris.java.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.scripting.exec.functions.ScripterFunction;
import hr.fer.zemris.java.webserver.RequestContext;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Stack;
/**
 * This class implements {@link ScripterFunction} that formats decimal values
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class FormatFunction implements ScripterFunction {
    @Override
    public void execute(Stack<ValueWrapper> multistack, RequestContext context) {

        String format = (String) multistack.pop().getValue();
        Object value = multistack.pop().getValue();

        DecimalFormat decimalFormat = new DecimalFormat(format);
        String formatted = decimalFormat.format(value);
        multistack.push(new ValueWrapper(formatted));
    }
}
