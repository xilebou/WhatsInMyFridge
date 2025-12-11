package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import database.DataBaseRecordable;
import database.SqlLiteDataBaseLink;
import recipes.Ingredient;
import recipes.Recipe;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseRecordableHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) {
        Map<String,String> params = getParams(String.valueOf(exchange.getRequestURI()));
        DataBaseRecordable dataBaseRecordable = switch (params.get("type").toLowerCase()) {
            case "ingredients" -> new Ingredient();
            case "recipes" -> new Recipe();
            default -> throw new IllegalStateException("Unexpected value: " + params.get("type").toLowerCase());
        };




        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                try {
                    handleGet(exchange, dataBaseRecordable, params);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "POST" -> {
                try {
                    handlePost(exchange, dataBaseRecordable, params);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private void handleGet(HttpExchange exchange, DataBaseRecordable recordable, Map<String,String> params) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            try {
                recordable.assignedFromDatabase(
                        new SqlLiteDataBaseLink("jdbc:sqlite:database/devData.sqlite"),
                        params
                );
                byte[] response = mapper.writeValueAsBytes(recordable);
                exchange.sendResponseHeaders(200, response.length);
                os.write(response);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            // nothing
        }
    }

    private void handlePost(HttpExchange exchange, DataBaseRecordable recordable, Map<String,String> params) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            int responseCode = 201;
            byte[] response = new byte[0];
            try {
                try {
                    String json = new String(exchange.getRequestBody().readAllBytes());
                    recordable = mapper.readValue(json, recordable.getClass());
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                    System.out.println("Invalid object format");
                } finally {
                    responseCode = 400;
                    mapper.writeValueAsBytes(false);
                }

                if (recordable != null) {
                    recordable.saveToDatabase(new SqlLiteDataBaseLink("jdbc:sqlite:database/devData.sqlite"));
                    response = mapper.writeValueAsBytes(true);
                }

            } catch (IOException | SQLException | ClassNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
                throw new RuntimeException(e);
            } finally {
                exchange.sendResponseHeaders(responseCode, response.length);
                os.write(response);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            // nothing
        }
    }
}
