package database;

import com.mysql.cj.PreparedQuery;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.List;
import java.util.Map;

public abstract class DataBaseLink {
    private String url;

    public DataBaseLink(String url) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResultSet request(PreparedStatement query) throws SQLException {
        ResultSet resultSet = null;


        resultSet = query.executeQuery();
        return resultSet;
    }

    public boolean insert(PreparedStatement query) throws SQLiteException {
        boolean result = false;

        try {
            result = query.execute();
        } catch (SQLiteException e) {
            throw e;
        } catch (SQLException e) {
            throw new RuntimeException();
        } finally {
            if (result) {
                try {
                    query.close();
                } catch (SQLException e) {
                    // skip
                }
            }
        }

        return result;
    }

    public abstract Connection openConection() throws SQLException;
}
