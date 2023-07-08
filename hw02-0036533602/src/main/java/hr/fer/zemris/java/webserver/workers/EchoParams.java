package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Objects;

/**
 * This class is an implementation of {@link IWebWorker} that will print out all parameters from url and there value
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class EchoParams implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) throws Exception {

        context.setMimeType("text/plain");

        StringBuilder sb = new StringBuilder("");

        if (!Objects.isNull(context.getParameterNames())) {
            for (String paramNames : context.getParameterNames()) {
                String value = context.getParameter(paramNames);
                sb.append(paramNames).append(" --> ").append(value).append("\n");
            }
        }

        context.setContentLength((long) sb.toString().length());
        context.write(sb.toString().getBytes());

    }
}
