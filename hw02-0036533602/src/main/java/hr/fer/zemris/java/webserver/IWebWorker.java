package hr.fer.zemris.java.webserver;
/**
 * Interface that represents servlet.
 * Main job is to prepare data and then write it or proceed request to another {@link IWebWorker} or smscr script
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public interface IWebWorker {
    public void processRequest(RequestContext context) throws Exception;
}