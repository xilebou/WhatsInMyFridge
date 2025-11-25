import api.API;
import com.google.protobuf.Api;
import database.DataBaseLink;
import database.SqlLiteDataBaseLink;
import recipes.Ingredient;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                new API();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        System.out.println("API loaded");
    }
}
