package database.factories;

import database.DataBaseLink;
import database.DataBaseRecordable;
import recipes.Ingredient;
import recipes.Recipe;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class DataBaseRecordableFactory {
    private Class<? extends DataBaseRecordable> recordableClass;

    public DataBaseRecordableFactory() {
    }

    public Collection<DataBaseRecordable> createDataBaseRecordable(DataBaseLink dataBaseLink, Map<String, String> params) throws IOException {
        Collection<DataBaseRecordable> records = List.of();
        try (PreparedStatement statement = dataBaseLink.openConection()
                .prepareStatement(
                        createSQLStatement(params) // don't worry mate it's safe
                )) {
            setStatementParameters(statement, params);
            records = createFromValues(dataBaseLink.request(statement), getRecordableClass());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return records;
    }

    protected abstract Class<? extends DataBaseRecordable> getRecordableClass();

    protected abstract String createSQLStatement(Map<String,String> params);
    protected abstract void setStatementParameters(PreparedStatement statement, Map<String,String> params) throws SQLException;

    private Collection<DataBaseRecordable> createFromValues(ResultSet resultSet, Class<? extends DataBaseRecordable> type) throws SQLException {
        Collection<DataBaseRecordable> records = new ArrayList<>();

        while (resultSet.next()) {
            try {
                DataBaseRecordable dbr = type.getConstructor().newInstance();
                dbr.setValues(resultSet);
                records.add(dbr);
            } catch (NoSuchMethodException e) {
                // this doesn't happen if class is implemented properly
                throw new RuntimeException(e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return records;
    }
}
