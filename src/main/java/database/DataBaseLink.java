package database;

import com.mysql.cj.PreparedQuery;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.List;

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

    public ResultSet request(PreparedStatement query) {
        ResultSet resultSet = null;

        try {
            resultSet = query.executeQuery();
        } catch (SQLException e) {
            System.out.println("SQL Error! Check output console");
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    // skip
                }
            }
        }
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
