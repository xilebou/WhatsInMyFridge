package api.handlers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import recipes.Ingredient;


import java.util.Map;
import java.util.TreeMap;

public abstract class BaseHandler implements HttpHandler {
    protected ObjectMapper mapper = new ObjectMapper();

    public BaseHandler() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
    }

    @Override
    public abstract void handle(HttpExchange exchange);

    protected Map<String, String> getParams(String uri) {
        Map<String, String> params = new TreeMap<>();


        String[] split = uri.split("/");
        params.put("type", split[2]);



        if (uri.contains("?")) {
            String[] stringParams = uri.split("\\?");
            if (stringParams.length > 1) {
                for (String s : stringParams[1].split("&")) {
                    params.put(s.split("=")[0], s.split("=")[1]);
                }
            }
        }


        return params;
    }


}
