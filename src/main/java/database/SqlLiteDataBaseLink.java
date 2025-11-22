package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlLiteDataBaseLink extends DataBaseLink {
    public SqlLiteDataBaseLink(String url) throws ClassNotFoundException {
        super(url);
    }


    public Connection openConection() throws SQLException {
        return DriverManager.getConnection(getUrl());
    }
}
