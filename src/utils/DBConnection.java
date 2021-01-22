package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    /** Database object references */
    private static Connection conn = null;
    private Statement stmt = null;

    /** database credentials: */
    static final String userName = "U05SoC";
    static final String password = "53688592846";
    static final String jdbcURL = "jdbc:mysql://wgudb.ucertify.com/WJ05SoC";


    public static void main(String[] args) {

        /** Driver interface reference*/
        final String JDBCDriver = "com.mysql.cj.jdbc.Driver";
    }


    /** Creates connection with database */
    public static Connection startConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection successful");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    /** Closes connection with database */
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection to database closed");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
