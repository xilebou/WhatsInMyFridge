package recipes;

import database.DataBaseLink;
import database.DataBaseRecordable;
import database.factories.DataBaseRecordableFactory;

import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Recipe implements DataBaseRecordable {
    private String name;
    private Map<Ingredient, Integer> ingredients;
    private String instructionsASHtml;
    private String description;
    private String source;
    private int portion;
    private Set<String> categories = new HashSet<>();

    public static DataBaseRecordableFactory createFactory() {
        return new DataBaseRecordableFactory() {
            @Override
            protected Class<? extends DataBaseRecordable> getRecordableClass() {
                return Recipe.class;
            }

            @Override
            protected String createSQLStatement(Map<String, String> params) {
                return "SELECT " +
                        " r.recipe_name," +
                        " r.instructions," +
                        " r.recipe_description," +
                        " r.portions," +
                        " s.source_name," +
                        " s.source_url " +
                        " FROM recipes r " +
                        " JOIN sources s ON r.source_id = s.id " +
                        "WHERE recipe_name = ? ";
            }

            @Override
            protected void setStatementParameters(PreparedStatement statement, Map<String, String> params) throws SQLException {
                if (params.containsKey("recipe_name")) statement.setString(1, params.get("recipe_name"));
            }
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<Ingredient, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructionsASHtml() {
        return instructionsASHtml;
    }

    public void setInstructionsASHtml(String instructionsASHtml) {
        this.instructionsASHtml = instructionsASHtml;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    @Override
    public void setValues(ResultSet rs) throws SQLException {
        this.name = rs.getString("recipe_name");
        this.instructionsASHtml = rs.getString("instructions");
        this.description = rs.getString("recipe_description");
        this.source = rs.getString("source_name");
        this.portion = rs.getInt("portions");
    }

    @Override
    public void saveToDatabase(DataBaseLink link) throws SQLException {
        PreparedStatement p = null;
        p = link.openConection().prepareStatement("INSERT INTO recipes (" +
                        "SELECT r.recipe_name," +
                        " r.instructions," +
                        " r.recipe_description," +
                        " s.source_name," +
                        " r.portions" +
                        " FROM recipes r" +
                        " LEFT JOIN sources s ON r.source_id = s.id" +
                        " WHERE r.id = ?" +
                " VALUES (?, ?, ?, ?, ?)"
        );

        p.setString(1, this.name);
        p.setBytes(2, this.instructionsASHtml.getBytes(StandardCharsets.UTF_8));
        p.setString(3, this.description);
        p.setString(4, null);
        p.setInt(5, this.portion);

        for (String category : this.categories) {
            PreparedStatement p2 = link.openConection().prepareStatement("INSERT INTO recipe_categories (" +
                    " recipe_id," +
                    " tag_name" +
                    ") VALUES ((SELECT id FROM recipes WHERE recipe_name = ?), ?)");
            p2.setString(1, this.name);
            p2.setString(2, category);
        }
    }
}
