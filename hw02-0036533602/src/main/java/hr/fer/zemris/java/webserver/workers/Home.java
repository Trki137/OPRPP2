package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Objects;

/**
 * This class is an implementation of {@link IWebWorker} that will store a background color from persistent parameters,
 * if it doesn't exist then color #7F7F7F is stored in temporary parameters
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class Home implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        String bg = context.getPersistentParameter("bgcolor");

        context.setTemporaryParameters("background", Objects.isNull(bg) ? "#7F7F7F" : bg);

        context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
    }
}
