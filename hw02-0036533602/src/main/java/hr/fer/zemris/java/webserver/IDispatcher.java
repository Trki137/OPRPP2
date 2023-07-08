package hr.fer.zemris.java.webserver;

/**
 * Interface for dispatching request to another page or another {@link IWebWorker}
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public interface IDispatcher {
    void dispatchRequest(String urlPath) throws Exception;

}
