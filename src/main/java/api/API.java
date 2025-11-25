package api;


import api.handlers.IngredientHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import recipes.Ingredient;

import java.io.IOException;
import java.io.OutputStream;
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
        server.createContext("/api/ingredients", new IngredientHandler());
    }
}
