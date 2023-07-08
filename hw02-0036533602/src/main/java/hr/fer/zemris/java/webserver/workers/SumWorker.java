package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;
/**
 * This class is an implementation of {@link IWebWorker} that will sum 2 values, a and b, passed as parameters and will show image based on result
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class SumWorker implements IWebWorker {
    private static final String EVEN_IMAGE = "dog.jpg";
    private static final String ODD_IMAGE = "dog2.jpg";
    @Override
    public void processRequest(RequestContext context) throws Exception {

        String firstParam = context.getParameter("a");
        String secondParam = context.getParameter("b");

        Integer a,b;

        try{
            a = Integer.parseInt(firstParam);
        }catch (Exception e){
            a = 1;
        }
        try{
            b = Integer.parseInt(secondParam);
        }catch (Exception e){
            b = 2;
        }
        int res = a + b;
        context.setTemporaryParameters("zbroj", Integer.toString(res));
        context.setTemporaryParameters("varA", Integer.toString(a));
        context.setTemporaryParameters("varB", Integer.toString(b));
        context.setTemporaryParameters("imgName", res % 2 == 0 ? EVEN_IMAGE : ODD_IMAGE);

        context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
    }
}
