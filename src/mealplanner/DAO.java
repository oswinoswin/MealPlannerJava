package mealplanner;

import java.sql.*;

public class DAO {
    static final String DB_URL = "jdbc:postgresql:meals_db";
    static final String USER = "postgres";
    static final String PASS = "1111";
    Connection connection;

    public DAO() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        connection.setAutoCommit(true);
    }

    public void run(String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    public void close() throws SQLException {
        connection.close();
    }
    
}
