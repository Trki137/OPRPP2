package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.util.Util;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * This class represent our server
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class SmartHttpServer {
    /**
     * Server address
     */
    private final String address;
    /**
     * Domain name
     */
    private final String domainName;
    /**
     * Server port
     */
    private final int port;
    /**
     * Number of threads for server
     */
    private final int workerThreads;
    /**
     * Timeout value for session
     */
    private final int sessionTimeout;
    /**
     * All available mime types
     */
    private final Map<String, String> mimeTypes = new HashMap<>();
    /**
     * Thread that listens for upcoming requests
     */
    private final ServerThread serverThread;
    /**
     * Pool of threads
     */
    private ExecutorService threadPool;
    /**
     * Root of our server
     */
    private final Path documentRoot;
    /**
     * Instance of {@link RequestContext}
     */
    private RequestContext context;

    /**
     * Map of all active sessions
     */

    private final Map<String, SessionMapEntry> sessions = new HashMap<>();

    /**
     * For generating random UID
     */
    private final Random sessionRandom = new Random();

    /**
     * Map of {@link IWebWorker} where key is appropriate path
     */

    private final Map<String, IWebWorker> workersMap = new HashMap<>();

    /**
     * Constructor for our server
     *
     * @param configFileName name of our configuration file
     * @throws Exception
     */
    public SmartHttpServer(String configFileName) throws Exception {
        Properties propertyReader = new Properties();

        propertyReader.load(Files.newBufferedReader(Path.of(configFileName).toAbsolutePath().normalize()));

        this.address = propertyReader.getProperty("server.address");
        this.domainName = propertyReader.getProperty("server.domainName");
        this.port = Integer.parseInt(propertyReader.getProperty("server.port"));
        this.workerThreads = Integer.parseInt(propertyReader.getProperty("server.workerThreads"));
        this.documentRoot = Path.of(propertyReader.getProperty("server.documentRoot"));
        Path mimeConfigPath = Path.of(propertyReader.getProperty("server.mimeConfig"));
        Path workersPath = Path.of(propertyReader.getProperty("server.workers"));
        this.sessionTimeout = Integer.parseInt(propertyReader.getProperty("session.timeout"));

        propertyReader = new Properties();
        propertyReader.load(Files.newBufferedReader(mimeConfigPath.toAbsolutePath().normalize()));

        for (var entry : propertyReader.entrySet()) {
            mimeTypes.put((String) entry.getKey(), (String) entry.getValue());
        }

        setWorkers(workersPath);
        this.serverThread = new ServerThread();
        start();

    }

    public static void main(String[] args) throws Exception {
        if(args.length != 1){
            throw new IllegalArgumentException("Expected only path for config file");
        }

        new SmartHttpServer(args[0]);
    }

    /**
     * Reads workers and stores them in map
     * @param workersPath path to all workers
     * @throws Exception
     */

    private void setWorkers(Path workersPath) throws Exception {
        List<String> workerLines = Files.readAllLines(workersPath);

        for (String worker : workerLines) {
            String[] split = worker.split(" = ");
            if (split.length != 2)
                throw new InvalidPropertiesFormatException("Invalid property: " + worker);

            String path = split[0].trim();
            String fqcn = split[1].trim();

            if (workersMap.containsKey(path))
                throw new InvalidPropertiesFormatException("Same path occurs more than once. Path: " + path);


            IWebWorker iWebWorker = getWorker(fqcn);
            if (Objects.isNull(iWebWorker))
                throw new InvalidPropertiesFormatException("Invalid fqcn.");

            workersMap.put(path, iWebWorker);
        }
    }

    /**
     * Starts our server by initializing thread pool and starting our thread for listening requests
     */

    protected synchronized void start() {
        threadPool = Executors.newFixedThreadPool(workerThreads);
        serverThread.start();
    }

    /**
     * For shutting down server
     */

    protected synchronized void stop() {
        serverThread.interrupt();
        threadPool.shutdown();
    }

    /**
     * Gets {@link IWebWorker} from fcgn, fully qualified name
     * @param fcqn fully qualified name of class
     * @return instance of {@link IWebWorker}
     */

    private IWebWorker getWorker(String fcqn) {

        try {
            Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fcqn);
            Constructor<?> constructor = referenceToClass.getConstructor();
            return (IWebWorker) constructor.newInstance();
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * This class implements {@link Thread}.
     * Main job is to listen for incoming request and sends it processing
     *
     * @author Dean Trkulja
     * @version 1.0
     */

    protected class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                Runnable cleanupJob = () -> {
                    sessions.entrySet().removeIf(entry -> entry.getValue().validUntil < System.currentTimeMillis());
                    try {
                        Thread.sleep(5000*60);
                    } catch (InterruptedException ignored) {}
                };
                Thread cleanup = new Thread(cleanupJob);
                cleanup.setDaemon(true);
                cleanup.start();

                @SuppressWarnings("resource")
                ServerSocket serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress((InetAddress) null, port));
                while (true) {
                    Socket client = serverSocket.accept();
                    ClientWorker clientWorker = new ClientWorker(client);
                    threadPool.submit(clientWorker);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This class implements {@link Runnable} and {@link IDispatcher}
     * Process request and sends response
     *
     * @author Dean Trkulja
     * @version 1.0
     */

    private class ClientWorker implements Runnable, IDispatcher {
        /**
         * Socket from which requst came
         */
        private final Socket csocket;
        /**
         * Stream from which we read data
         */
        private InputStream istream;
        /**
         * Stream for writing data
         */
        private OutputStream ostream;
        /**
         * HTTP version
         */
        private String version;
        /**
         * HTTP method
         */
        private String method;
        /**
         *
         */
        private String host;
        /**
         * Parameters
         */
        private final Map<String, String> params = new HashMap<>();
        /**
         * Temporary parameters
         */
        private final Map<String, String> tempParams = new HashMap<>();
        /**
         * Permanent parameters
         */
        private Map<String, String> permPrams = new HashMap<>();

        /**
         * List of cookies for session
         */
        private final List<RequestContext.RCCookie> outputCookies = new ArrayList<>();
        /**
         * Session id
         */
        private String SID;
        /**
         * Left boundary for generating SID
         */
        private final int LEFT_LIMIT = 97;
        /**
         * Right boundary for generating SID
         */
        private final int RIGHT_LIMIT = 122;
        /**
         * Lenght of SID
         */
        private final int SID_LENGTH = 20;

        /**
         * Base constructor for our Client worker
         */
        public ClientWorker(Socket csocket) {
            this.csocket = csocket;
        }

        @Override
        public void run() {
            try {

                istream = new BufferedInputStream(csocket.getInputStream());
                ostream = new BufferedOutputStream(csocket.getOutputStream());

                String request = Util.readRequest(istream);
                if (Objects.isNull(request)) {
                    sendEmptyResponse(400, "Bad request");
                    return;
                }
                List<String> header = Util.extractHeader(request);
                System.out.println(header);
                String[] firstLine = header.isEmpty() ? null : header.get(0).split(" ");
                if (Objects.isNull(firstLine) || firstLine.length != 3) {
                    sendEmptyResponse(400, "Bad request");
                    return;
                }

                method = firstLine[0];
                if (!method.equals("GET")) {
                    sendEmptyResponse(400, "Bad request");
                    return;
                }

                version = firstLine[2];
                if (!(version.equals("HTTP/1.0") || version.equals("HTTP/1.1"))) {
                    sendEmptyResponse(400, "Bad request");
                    return;
                }

                getHost(header);
                checkSession(header);

                String requestedPath = firstLine[1];
                String path, params;
                if (requestedPath.contains("?")) {
                    path = requestedPath.split("\\?")[0];
                    params = requestedPath.split("\\?")[1];
                    boolean success = parseParameters(params);

                    if (!success) return;
                } else path = requestedPath;

                if (path.contains(documentRoot.toString())){
                    sendEmptyResponse(403,"Access forbidden");
                    return;
                }

                internalDispatchRequest(path, true);
            } catch (Exception e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        }

        /**
         * Checks if session exists and if it is valid. If it doesn't exist, or it isn't valid crates new session
         * @param header request header
         */
        private synchronized void checkSession(List<String> header) {
            String sidCandidate = null;
            for (String line : header) {
                if (!line.startsWith("Cookie:")) continue;
                String[] cookies = line.replace("Cookie:", "").split(";");

                for (String cookie : cookies) {
                    if (cookie.trim().startsWith("sid")) {
                        sidCandidate = cookie.split("=")[1];
                        sidCandidate = sidCandidate.replaceAll("\"", "");
                    }
                }
            }
            if (Objects.isNull(sidCandidate) || !sessions.containsKey(sidCandidate)) {
                SessionMapEntry session = createSession();
                sessions.put(SID, session);
                return;
            }
            SessionMapEntry session = sessions.get(sidCandidate);


            if (!session.host.equals(host)) {
                SessionMapEntry newSession = createSession();
                sessions.put(SID, newSession);
                return;
            }

            if (System.currentTimeMillis() > session.validUntil) {
                SessionMapEntry newSession = createSession();
                sessions.remove(session.sid);
                sessions.put(SID, newSession);
                return;
            }

            session.validUntil = System.currentTimeMillis() + sessionTimeout * 1000L;
            permPrams = session.map;

        }

        /**
         * Creates new session
         * @return instance of {@link SessionMapEntry}
         */
        private SessionMapEntry createSession() {
            SID = sessionRandom
                    .ints(LEFT_LIMIT, RIGHT_LIMIT + 1).limit(SID_LENGTH)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString().toUpperCase();

            RequestContext.RCCookie cookie = new RequestContext.RCCookie(SID, "sid", null, host, "/");
            outputCookies.add(cookie);

            return new SessionMapEntry(
                    SID,
                    host,
                    System.currentTimeMillis() + sessionTimeout * 1000L,
                    new ConcurrentHashMap<>()
            );
        }

        /**
         * Fills {@link RequestContext} if it is null and then calls processRequest
         *
         * @param iWebWorker instance of {@link IWebWorker}
         * @throws Exception
         */

        private void handleWorker(IWebWorker iWebWorker) throws Exception {
            if (Objects.isNull(context))
                context = new RequestContext(ostream, params, permPrams, outputCookies.size() == 0 ? null : outputCookies);


            if (!params.isEmpty()) {
                params.forEach((key, value) -> context.setParameter(key, value));
            }

            iWebWorker.processRequest(context);
        }

        /**
         * Processes request
         *
         * @param urlPath path that was called
         * @param directCall checks if this call was by {@link IWebWorker} or basic direct call
         * @throws Exception
         */

        private void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
            if (Objects.isNull(context))
                context = new RequestContext(ostream, params, permPrams, outputCookies.size() == 0 ? null : outputCookies, null, this);

            if (urlPath.contains("private") && directCall) {
                sendEmptyResponse(403, "Forbidden");
                return;
            }

            if (urlPath.startsWith("/ext")) {
                urlPath = urlPath.substring("/ext/".length());
                IWebWorker worker = getWorker("hr.fer.zemris.java.webserver.workers." + urlPath);

                if (Objects.isNull(worker)) {
                    sendEmptyResponse(404, "Page not found");
                    return;
                }

                handleWorker(worker);

                csocket.close();
                context = null;
                return;
            }
            if (workersMap.containsKey(urlPath)) {
                handleWorker(workersMap.get(urlPath));
                csocket.close();
                context = null;
                return;
            }

            Path pathToFile = Path.of("./webroot" + urlPath);

            if (!Files.exists(pathToFile)) {
                sendEmptyResponse(404, "File doesn't exist");
                return;
            }

            if (!Files.isRegularFile(pathToFile)) {
                sendEmptyResponse(404, "File doesn't exist");
                return;
            }

            if (!Files.isReadable(pathToFile)) {
                sendEmptyResponse(404, "File is not readable");
                return;
            }
            String extension = urlPath.substring(urlPath.lastIndexOf(".") + 1);
            String mimeType = mimeTypes.getOrDefault(extension, "application/octet-stream");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream fileInputStream = Files.newInputStream(pathToFile);
            byte[] buffer = new byte[4096];
            while (true) {
                int read = fileInputStream.read(buffer);
                if (read == -1) {
                    if (extension.equals("smscr")) handleSmsrcFile(mimeType, bos.toString());
                    else sendResponseWithData(null, null, mimeType, bos.toByteArray());
                    fileInputStream.close();
                    csocket.close();
                    break;
                }
                bos.write(buffer, 0, read);
            }

        }

        /**
         * If client request smsrc file this method will generate output and handle smsrc file
         * @param mimeType response type
         * @param documentBody body of smsrc file
         */
        private void handleSmsrcFile(String mimeType, String documentBody) {
            context.setMimeType(mimeType);

            new SmartScriptEngine(
                    new SmartScriptParser(documentBody).getDocumentNode(),
                    context
            ).execute();

            Set<String> pparamSet = context.getPersistentParameterNames();
            if(!Objects.isNull(pparamSet)){
                pparamSet.forEach((pparam) -> permPrams.put(pparam,context.getPersistentParameter(pparam)));
            }
            context = null;
        }

        @Override
        public void dispatchRequest(String urlPath) throws Exception {
            internalDispatchRequest(urlPath, false);
        }

        /**
         * Parses URL parameters
         *
         * @param params URL parameters
         * @return true if parameters are valid, else it returns false
         * @throws IOException
         */
        private boolean parseParameters(String params) throws IOException {
            String[] paramsSplit = params.split("&");
            if (paramsSplit.length == 0) {
                sendEmptyResponse(400, "Bad request");
                return false;
            }
            for (String param : paramsSplit) {
                String[] split = param.split("=");
                if (split.length != 2) {
                    sendEmptyResponse(400, "Bad request");
                    return false;
                }
                this.params.put(split[0], split[1]);

            }
            return true;
        }

        /**
         * Sets {@code host}
         *
         * @param header request header
         */
        private void getHost(List<String> header) {
            boolean set = false;
            for (String line : header) {
                if (line.startsWith("Host:")) {
                    this.host = line.split(":")[1];
                    set = true;
                    break;
                }
            }

            if (!set)
                this.host = domainName;

        }

        /**
         * Sends a response without body
         * @param statusCode status code for response
         * @param message status message
         * @throws IOException
         */

        private void sendEmptyResponse(Integer statusCode, String message) throws IOException {
            sendResponseWithData(statusCode, message, mimeTypes.get("txt"), new byte[0]);
        }

        /**
         * Sends response with data
         * @param statusCode status code for response
         * @param message status message
         * @param mimeType type of content for response
         * @param data to send
         * @throws IOException
         */
        private void sendResponseWithData(Integer statusCode, String message, String mimeType, byte[] data) throws IOException {
            if(Objects.isNull(context))
                context = new RequestContext(ostream, params,permPrams,outputCookies,null,this);

            if (!Objects.isNull(statusCode)) {
                context.setStatusCode(statusCode);
                context.setStatusText(message);
            }

            context.setMimeType(mimeType);
            context.setContentLength((long) data.length);

            context.write(data);
            context = null;
        }
    }

    /**
     * Represent session
     */

    private static class SessionMapEntry {
        /**
         * Session id
         */
        private final String sid;
        /**
         * Who is host of session
         */
        private final String host;
        /**
         * How long is session valid
         */
        private long validUntil;

        /**
         * Session parameters
         */
        private final Map<String, String> map;

        public SessionMapEntry(String sid, String host, long validUntil, Map<String, String> map) {
            this.sid = sid;
            this.host = host;
            this.validUntil = validUntil;
            this.map = map;
        }

        @Override
        public String toString() {
            return "SessionMapEntry{" +
                    "sid='" + sid + '\'' +
                    ", host='" + host + '\'' +
                    ", validUntil=" + validUntil +
                    ", map=" + map +
                    '}';
        }
    }

}
