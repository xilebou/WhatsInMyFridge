import database.DataBaseLink;
import database.SqlLiteDataBaseLink;
import recipes.Ingredient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DataBaseLink dbl = new SqlLiteDataBaseLink("jdbc:sqlite:database/devData.sqlite");
            Ingredient ingredient = new Ingredient();
            PreparedStatement statement = dbl.openConection().prepareStatement(
                    "SELECT * from ingredients;"
            );
            for (ResultSet rs = statement.executeQuery(); rs.next();) {
                ingredient.fromDataBaseRecord(rs);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
