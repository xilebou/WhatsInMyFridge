package recipes;

import database.DataBaseLink;
import database.SqlLiteDataBaseLink;
import database.DataBaseRecordable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class Ingredient implements DataBaseRecordable {
    private String ingredientName;
    private String barcode;
    private LocalDate expiryDate;
    private int quantity;
    private QuantityType quantityType;


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
    public void fromDataBaseRecord(ResultSet rs) throws SQLException {
        this.ingredientName = rs.getString("ingredient_name");
        this.barcode = rs.getString("barcode");
        this.quantityType = QuantityType.valueOf(rs.getString("ingredient_quantity_type").toUpperCase());
        this.quantity = rs.getInt("ingredient_quantity");
        this.expiryDate = rs.getObject("expiry_date", LocalDate.class);
    }

    @Override
    public void saveToDatabase(DataBaseLink link) throws SQLException {

    }
}
