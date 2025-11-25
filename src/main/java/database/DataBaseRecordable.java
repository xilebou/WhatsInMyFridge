package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface DataBaseRecordable {
    void setValues(ResultSet rs) throws SQLException;
    void saveToDatabase(DataBaseLink conn) throws SQLException;

    DataBaseRecordable assignedFromDatabase(DataBaseLink conn, Map<String, String> params);
}
