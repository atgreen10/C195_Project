package utils;

import model.Customer;
import sample.Main;


import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class requests {

    /** gets the usernames that are in the database. */
   public static ResultSet getUserName() throws SQLException {
       DBQuery.setStatement(Main.conn);
       Statement statement = DBQuery.getStatement();

       String acceptedUsernames = "SELECT user_name FROM users";
       statement.execute(acceptedUsernames);
       ResultSet rs = statement.getResultSet();
       return rs;
   }

    /** gets the users password from the database. */
   public static ResultSet getPassword() {
       try {
           String acceptedPasswords = "SELECT Password FROM users";
           DBQuery.setPreparedStatement(Main.conn, acceptedPasswords);
           PreparedStatement statement =DBQuery.getPreparedStatement();
           statement.execute(acceptedPasswords);
           ResultSet rs = statement.getResultSet();
           return rs;
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return null;
   }

   /** runs the query to the database for customer data */
    public ResultSet getCustomerData() throws SQLException {
        return DBConnection.startConnection().createStatement().executeQuery("SELECT * FROM customers");
    }

    /** gets all first_level_divisions for combo box */
    public ResultSet getStates() throws SQLException {
        String states = "SELECT Division FROM first_level_divisions";
        DBQuery.setPreparedStatement(Main.conn, states);
        PreparedStatement st = DBQuery.getPreparedStatement();
        st.execute(states);
        ResultSet rs = st.getResultSet();
        return rs;

    }

}
