package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Objects;
/**
 * This class is implementation of {@link IWebWorker} that will change background color
 * if color is valid and will print out message if it was successful or not
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class BgColorWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) throws Exception {
        String hexBg = context.getParameter("bgcolor");
        boolean valid = !Objects.isNull(hexBg) && validBg(hexBg);

        if(valid)
            context.setPersistentParameter("bgcolor", "#"+hexBg);

        StringBuilder sb = new StringBuilder();
        sb.append("<body><html><a href=\"./index2.html\" >index2.html</a><br>");
        sb.append(valid ? "<p>Color is update</p>" : "<p>Color is not updated</p>");
        sb.append("</body></html>");

        context.setContentLength((long)sb.toString().length());
        context.setMimeType("text/html");
        context.write(sb.toString().getBytes());
    }

    private boolean validBg(String hexBg) {
        if(hexBg.length() != 6) return false;
        hexBg = hexBg.toLowerCase();
        for(char hex : hexBg.toCharArray()){
            if(!Character.isDigit(hex) && !(hex >= 'a' && hex <= 'f')) {
                System.out.println(hex);
                return false;
            }
        }
        return true;
    }
}
