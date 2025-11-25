package recipes;

import database.DataBaseLink;
import database.DataBaseRecordable;
import org.sqlite.SQLiteException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public class Ingredient implements DataBaseRecordable {
    private String ingredientName;
    private String barcode;
    private LocalDate expiryDate;
    private int quantity;
    private QuantityType quantityType;


    public DataBaseRecordable assignedFromDatabase(DataBaseLink dataBaseLink, Map<String, String> params) {
        try (PreparedStatement statement = dataBaseLink.openConection()
                .prepareStatement(
                        "SELECT PI.barcode," +
                                " PI.expiry_date," +
                                " I.full_ingredient_name AS 'ingredient_name'," +
                                " GI.ingredient_unit_type," +
                                " PI.ingredient_quantity" +
                                " FROM pantry_ingredients PI" +
                                " NATURAL JOIN ingredients I" +
                                " JOIN generic_ingredients GI ON I.generic_ingredient_name = GI.generic_name" +
                                " WHERE barcode = ?"
                )) {
            statement.setString(1, params.get("barcode"));
            setValues(dataBaseLink.request(statement));
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return this;
    }
    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public QuantityType getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(QuantityType quantityType) {
        this.quantityType = quantityType;
    }

    @Override
    public void setValues(ResultSet rs) throws SQLException {
        this.ingredientName = rs.getString("ingredient_name");
        this.barcode = rs.getString("barcode");
        if (rs.getString("ingredient_unit_type") != null) {
            this.quantityType = QuantityType.valueOf(rs.getString("ingredient_unit_type"));
        } else this.quantityType = null;
        this.quantity = rs.getInt("ingredient_quantity");

        if (rs.getString("expiry_date") != null) {
            this.expiryDate = rs.getObject("expiry_date", LocalDate.class);

        } else this.expiryDate = null;
    }

    @Override
    public void saveToDatabase(DataBaseLink link) throws SQLException {
        PreparedStatement p = null;
        p = link.openConection().prepareStatement("INSERT INTO pantry_ingredients (" +
                "barcode," +
                " expiry_date," +
                " ingredient_quantity )" +
                " VALUES (?, ?, ?)"
        );

        p.setString(1, this.barcode);
        p.setObject(2, this.expiryDate);
        p.setInt(3, this.quantity);

        PreparedStatement potentialUpdate = null;
        try {
            link.insert(p);
        } catch (SQLiteException e) {
            // if already exists
            try {
                potentialUpdate = link.openConection()
                        .prepareStatement("UPDATE pantry_ingredients SET ingredient_quantity = ingredient_quantity + ? WHERE barcode = ?");
                potentialUpdate.setInt(1, this.quantity);
                potentialUpdate.setString(2, this.barcode);
                potentialUpdate.executeUpdate();
            } catch (SQLException ex) {
                System.out.println("Item barcode doesn't exist");
            }
        } finally{
            p.close();
            if(potentialUpdate != null){
                potentialUpdate.close();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return quantity == that.quantity && Objects.equals(ingredientName, that.ingredientName) && Objects.equals(barcode, that.barcode) && Objects.equals(expiryDate, that.expiryDate) && quantityType == that.quantityType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientName, barcode, expiryDate, quantity, quantityType);
    }
}
