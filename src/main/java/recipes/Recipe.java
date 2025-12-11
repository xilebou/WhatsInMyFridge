package recipes;

import database.DataBaseLink;
import database.DataBaseRecordable;

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

    public DataBaseRecordable assignedFromDatabase(DataBaseLink dataBaseLink, Map<String, String> params) {
        try (PreparedStatement statement = dataBaseLink.openConection()
                .prepareStatement(
                        "SELECT r.recipe_name," +
                                " r.instructions," +
                                " r.recipe_description," +
                                " s.source_name," +
                                " r.portions" +
                                " FROM recipes r" +
                                " LEFT JOIN sources s ON r.source_id = s.id" +
                                " WHERE r.id = ?"
                )) {
            statement.setString(1, params.get("recipe_id"));
            setValues(dataBaseLink.request(statement));
            PreparedStatement newStatement = dataBaseLink.openConection()
                    .prepareStatement("SELECT tag_name FROM recipe_categories WHERE recipe_id = ?");
            newStatement.setString(1, params.get("recipe_id"));
            ResultSet rs = newStatement.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("tag_name"));
            }


        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return this;
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
