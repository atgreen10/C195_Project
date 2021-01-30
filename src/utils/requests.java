package utils;

import sample.Main;


import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static utils.DBQuery.getStatement;

public class requests {

    /** gets the usernames that are in the database. */
   public static ResultSet getUserName() throws SQLException {
       DBQuery.setStatement(Main.conn);
       Statement statement = getStatement();

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
        return DBConnection.startConnection().createStatement().executeQuery("SELECT * FROM first_level_divisions");
    }
    public static ResultSet getUSLocations() throws SQLException {
        return DBConnection.startConnection().createStatement().executeQuery("SELECT Division FROM first_level_divisions WHERE COUNTRY_ID = 1");

}

    public static ResultSet getUKLocations() throws SQLException{
        return DBConnection.startConnection().createStatement().executeQuery("SELECT Division FROM first_level_divisions WHERE COUNTRY_ID = 2");
}

    public static ResultSet getCanadaLocations() throws SQLException{
        return DBConnection.startConnection().createStatement().executeQuery("SELECT Division FROM first_level_divisions WHERE COUNTRY_ID = 3");
    }

    public ResultSet getCountry() throws SQLException {
        return DBConnection.startConnection().createStatement().executeQuery("SELECT * FROM countries");
    }

        public static ResultSet getCustomerID () throws SQLException {
            DBQuery.setStatement(Main.conn);
            Statement statement = getStatement();

            String customerID = "SELECT customer_ID FROM customers";
            statement.execute(customerID);
            ResultSet custID = statement.getResultSet();
            return custID;
        }

        public static ResultSet reloadCustomerTable () throws SQLException {
            Statement statement = getStatement();
            String allCustomers = "SELECT * FROM customers";
            statement.execute(allCustomers);
            ResultSet reload = statement.getResultSet();
            return reload;
        }
    }
