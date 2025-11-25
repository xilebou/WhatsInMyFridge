package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import database.SqlLiteDataBaseLink;
import recipes.Ingredient;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class IngredientHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) {
        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                try {
                    handleGet(exchange);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "POST" -> {
                try {
                    handlePost(exchange);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private void handleGet(HttpExchange exchange) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            try {
                Ingredient ingredientToReturn = Ingredient.createIngredientFromDatabase(
                        new SqlLiteDataBaseLink("jdbc:sqlite:database/devData.sqlite"),
                        getParams(String.valueOf(exchange.getRequestURI()))
                );
                byte[] response = mapper.writeValueAsBytes(ingredientToReturn);
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

    private void handlePost(HttpExchange exchange) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            int responseCode = 201;
            byte[] response = new byte[0];
            try {
                Ingredient ingredient = null;
                try {
                    String json = new String(exchange.getRequestBody().readAllBytes());
                    ingredient = mapper.readValue(json, Ingredient.class);
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                    System.out.println("Invalid object format");
                } finally {
                    responseCode = 400;
                    mapper.writeValueAsBytes(false);
                }

                if (ingredient != null) {
                    ingredient.saveToDatabase(new SqlLiteDataBaseLink("jdbc:sqlite:database/devData.sqlite"));
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
