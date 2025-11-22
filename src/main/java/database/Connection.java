package database;

public class Connection {

    public Connection() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }
}
