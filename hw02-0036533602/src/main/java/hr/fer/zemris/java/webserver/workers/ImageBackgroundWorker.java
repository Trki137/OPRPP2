package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Objects;

public class ImageBackgroundWorker implements IWebWorker {
    private static final String IMAGE1 = "./images/dog.jpg";
    private static final String IMAGE2 = "./images/dog2.jpg";

    @Override
    public void processRequest(RequestContext context) throws Exception {

        context.setTemporaryParameters("image1", IMAGE1);
        context.setTemporaryParameters("image2", IMAGE2);

        context.getDispatcher().dispatchRequest("/private/pages/imageScript.smscr");
    }
}
