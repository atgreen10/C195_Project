package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBQuery {

    static PreparedStatement stmt;

    /**Creates statement object */
    private static Statement statement;

    /**Create a prepared statement */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        stmt = conn.prepareStatement(sqlStatement);
    }

    public static PreparedStatement getPreparedStatement(){
        return stmt;
    }

    /** creates a statement object */
//    public static void setStatement(Connection conn) throws SQLException {
//        statement = conn.createStatement();
//    }

    public static Statement getStatement() {
        return statement;
    }
}
