package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RequestContext {

    private final String LINE_SEPERATOR = "\r\n";
    private OutputStream outputStream;
    private Charset standardCharsets;
    private Map<String, String> parameters;
    private Map<String, String> temporaryParameters;
    private Map<String, String> persistentParameters;
    private List<RCCookie> outputCookies;
    private boolean headerGenerated = false;

    private final IDispatcher dispatcher;
    public String encoding = "UTF-8";
    public int statusCode = 200;
    public String statusText = "OK";

    public String mimeType = "text/html";

    public Long contentLength = null;

    public RequestContext(OutputStream outputStream, Map<String,String> parameters, Map<String,String> persistentParameters, List<RCCookie> outputCookies, Map<String,String> temporaryParameters, IDispatcher dispatcher){
        Objects.requireNonNull(outputStream);

        this.outputStream = outputStream;
        this.parameters = Objects.isNull(parameters) ? new HashMap<>() : parameters;
        this.persistentParameters = Objects.isNull(persistentParameters) ? new HashMap<>() : persistentParameters;
        this.outputCookies = Objects.isNull(outputCookies) ? new ArrayList<>() : outputCookies;
        this.temporaryParameters = Objects.isNull(temporaryParameters) ? new HashMap<>() : temporaryParameters;
        this.dispatcher = dispatcher;
    }

    public RequestContext(OutputStream outputStream, Map<String,String> parameters, Map<String,String> persistentParameters, List<RCCookie> outputCookies){
        this(outputStream,parameters,persistentParameters,outputCookies,null,null);
     }

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    public String getParameter(String name){
        return parameters.get(name);
     }

     public void setParameter(String name, String value){
        parameters.put(name,value);
     }

     public Set<String> getParameterNames(){
        return parameters.isEmpty() ? null : parameters.keySet();
     }

     public String getPersistentParameter(String name){
        return persistentParameters.get(name);
     }
    public Set<String> getPersistentParameterNames(){
        return persistentParameters.isEmpty() ? null : persistentParameters.keySet();
    }

    public void setPersistentParameter(String name, String value){
        persistentParameters.put(name, value);
    }

    public void removePersistentParameter(String name){
        persistentParameters.remove(name);
    }

    public String getTemporaryParameter(String name){
        return temporaryParameters.get(name);
    }

    public Set<String> getTemporaryParameterNames(){
        return temporaryParameters.isEmpty() ? null : temporaryParameters.keySet();
    }

    public String getSessionID(){
        return "";
    }

    public void setTemporaryParameters(String name,String value){
        temporaryParameters.put(name,value);
    }

    public void removeTemporaryParameter(String name){
        temporaryParameters.remove(name);
    }

    public RequestContext write(byte[] data) throws IOException{
        return write(data,0,data.length);
    }
    public RequestContext write(byte[] data, int offset,int len) throws IOException{
        if(!headerGenerated) generateHeader();

        outputStream.write(data,offset,len);
        //System.out.println(new String(data));
        outputStream.flush();
        return this;
    }



    public RequestContext write(String text) throws IOException{
        return write(text.getBytes());
    }

    public void setEncoding(String encoding) {
        if(headerGenerated)
            throw new RuntimeException("Can't set properties after header has been generated");

        this.encoding = encoding;
    }

    public void setStatusCode(int statusCode) {
        if(headerGenerated)
            throw new RuntimeException("Can't set properties after header has been generated");

        this.statusCode = statusCode;
    }

    public void setStatusText(String statusText) {
        if(headerGenerated)
            throw new RuntimeException("Can't set properties after header has been generated");

        this.statusText = statusText;
    }

    public void setMimeType(String mimeType) {
        if(headerGenerated)
            throw new RuntimeException("Can't set properties after header has been generated");

        this.mimeType = mimeType;
    }

    public void setContentLength(Long contentLength) {
        if(headerGenerated)
            throw new RuntimeException("Can't set properties after header has been generated");

        this.contentLength = contentLength;
    }

    public void addRCCookie(RCCookie rcCookie) {
        outputCookies.add(rcCookie);
    }

    private void generateHeader() throws IOException {
        StringBuilder header = new StringBuilder();
        StringBuilder sb = new StringBuilder();

        String firstLine = String.format("HTTP/1.1 %d %s", statusCode,statusText);
        header.append(firstLine).append(LINE_SEPERATOR);

        sb.append("Content-Type: ").append(mimeType);
        if(mimeType.startsWith("text/")) sb.append(";charset=").append(encoding);

        header.append(sb).append(LINE_SEPERATOR);
        if(!Objects.isNull(contentLength))
            header.append("Content-Length:").append(contentLength).append(LINE_SEPERATOR);


        if(!outputCookies.isEmpty()){
            for (RCCookie cookie: outputCookies)
                header.append(cookie).append(LINE_SEPERATOR);
        }
        header.append("Connection: close\r\n");
        header.append(LINE_SEPERATOR);

        outputStream.write(header.toString().getBytes(StandardCharsets.ISO_8859_1));
        //System.out.println("Generated header: ");
        //System.out.println(header);

        headerGenerated = true;
    }

    public static class RCCookie{
        private final String name;
        private final String value;
        private final String domain;
        private final String path;
        private final Integer maxAge;

        public RCCookie( String value,String name,Integer maxAge, String domain, String path) {
            this.name = name;
            this.value = value;
            this.domain = domain;
            this.path = path;
            this.maxAge = maxAge;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getDomain() {
            return domain;
        }

        public String getPath() {
            return path;
        }

        public Integer getMaxAge() {
            return maxAge;
        }

        @Override
        public String toString() {
            StringBuilder cookie = new StringBuilder();
            cookie.append("Set-Cookie: ");
            cookie.append(name).append("=").append(String.format("\"%s\"; ",value));

            if(!Objects.isNull(domain))
                cookie.append("Domain=").append(String.format("%s; ",domain.trim()));

            if(!Objects.isNull(path))
                cookie.append("Path=").append(String.format("%s; ",path));

            if(!Objects.isNull(maxAge))
                cookie.append("Max-Age=").append(String.format("%d; ",maxAge));

            cookie.append(" HttpOnly");

            return cookie.toString();
        }
    }
}
