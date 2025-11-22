package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataBaseRecordable {
    void fromDataBaseRecord(ResultSet rs) throws SQLException;
    void saveToDatabase(DataBaseLink conn) throws SQLException;
}
