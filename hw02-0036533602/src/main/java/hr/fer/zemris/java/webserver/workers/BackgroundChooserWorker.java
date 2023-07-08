package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Objects;
import java.util.Random;

public class BackgroundChooserWorker implements IWebWorker {

    private static final String IMAGE1 = "./images/dog.jpg";
    private static final String IMAGE2 = "./images/dog2.jpg";
    @Override
    public void processRequest(RequestContext context) throws Exception {

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(localDateTime);

        String imageUrl = context.getParameter("imageUrl");

        if(Objects.isNull(imageUrl)){
            int random = new Random().nextInt(0,1);
            if(random == 1) imageUrl = IMAGE1;
            else imageUrl = IMAGE2;
        }else imageUrl = imageUrl.replace("%2F", "/");
        context.setTemporaryParameters("imageURL", imageUrl);
        context.setTemporaryParameters("time", date);

        context.getDispatcher().dispatchRequest("/private/pages/timeScript.smscr");
    }
}
