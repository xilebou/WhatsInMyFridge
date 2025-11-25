package api;


import api.handlers.DatabaseRecordableHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
public class API {
    private HttpServer server;

    public API() throws IOException {
        server = HttpServer.create(new InetSocketAddress(3900), 0);
        initAllEndpoints();
        server.setExecutor(null);
        server.start();
    }

    private void initAllEndpoints() throws IOException {
        server.createContext("/api/", new DatabaseRecordableHandler());
    }
}
