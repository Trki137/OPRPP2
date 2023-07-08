package hr.fer.zemris.java.demo;

import hr.fer.zemris.java.webserver.SmartHttpServer;

public class DemoSmartHttpServer {
    public static void main(String[] args) throws Exception {
        new SmartHttpServer("config/server.properties");
    }
}
