package recipes;

import database.DataBaseLink;
import database.DataBaseRecordable;
import org.sqlite.SQLiteException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Recipe implements DataBaseRecordable {
    private String name;
    private Map<Ingredient, Integer> ingredients;
    private String instructionsASHtml;
    private String description;
    private String source;
    private int portion;

    public static Recipe createRecipeFromDatabase(DataBaseLink dataBaseLink, Map<String,String> params) {
        Recipe recipe = new Recipe();
        try (PreparedStatement statement = dataBaseLink.openConection()
                .prepareStatement(
                        "SELECT r.name," +
                                " r.instructions," +
                                " r.description," +
                                " s.source_name," +
                                " r.portion" +
                                " FROM recipe r" +
                                " JOIN source s ON r.source_id = s.id" +
                                " WHERE r.recipe_id = ?"
                )) {
            statement.setString(1, params.get("recipe_id"));
            recipe.fromDataBaseRecord(dataBaseLink.request(statement));
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return recipe;
    }
    @Override
    public void fromDataBaseRecord(ResultSet rs) throws SQLException {
        this.name = rs.getString("name");
        this.instructionsASHtml = rs.getString("instructions");
        this.description = rs.getString("description");
        this.source = rs.getString("source_name");
        this.portion = rs.getInt("portion");
    }

    @Override
    public void saveToDatabase(DataBaseLink link) throws SQLException {
        PreparedStatement p = null;
        p = link.openConection().prepareStatement("INSERT INTO recipes (" +
                " recipe_name," +
                " instructions," +
                " recipe_description, " +
                " source_id," +
                " portions" +
                ")" +
                " VALUES (?, ?, ?, ?, ?)"
        );

        p.setString(1, this.name);
        p.setBytes(2, this.instructionsASHtml.getBytes(StandardCharsets.UTF_8));
        p.setString(3, this.description);
        p.setString(4, null);
        p.setInt(5, this.portion);
    }
}
