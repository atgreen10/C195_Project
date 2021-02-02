package utils;

import javafx.collections.ObservableList;
import model.Country;
import model.First_Level_Division;
import sample.Main;

import java.sql.*;

import static javafx.collections.FXCollections.observableArrayList;
import static utils.DBQuery.getStatement;

public class requests {

    /**
     * gets the usernames that are in the database.
     */
    public static ResultSet getUserName() throws SQLException {
        DBQuery.setStatement(Main.conn);
        Statement statement = getStatement();

        String acceptedUsernames = "SELECT user_name FROM users";
        statement.execute(acceptedUsernames);
        ResultSet rs = statement.getResultSet();
        return rs;
    }

    /**
     * gets the users password from the database.
     */
    public static ResultSet getPassword() {
        try {
            String acceptedPasswords = "SELECT Password FROM users";
            DBQuery.setPreparedStatement(Main.conn, acceptedPasswords);
            PreparedStatement statement = DBQuery.getPreparedStatement();
            statement.execute(acceptedPasswords);
            ResultSet rs = statement.getResultSet();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * runs the query to the database for customer data
     */
    public ResultSet getCustomerData() throws SQLException {
        return DBConnection.startConnection().createStatement().executeQuery("SELECT * FROM customers");
    }

    public static ObservableList<First_Level_Division> getAllStates() {
        ObservableList<First_Level_Division> allStates = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            conn = DBConnection.startConnection();
            allStates = observableArrayList();
            String getResult = "SELECT Division FROM first_level_divisions";
            DBQuery.setPreparedStatement(conn, getResult);
            ps = DBQuery.getPreparedStatement();
            result = ps.executeQuery();
            while (result.next()) {
                First_Level_Division state = new First_Level_Division();
                state.setDivision(result.getString("Division"));
                allStates.add(state);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, result, conn);
        }
        return allStates;
    }

    /**
     * gets all first_level_divisions for combo box
     */
    public static ObservableList<First_Level_Division> getStates(int countryID) {
        ObservableList<First_Level_Division> states = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            conn = DBConnection.startConnection();
            states = observableArrayList();
            String getResult = "SELECT Division, Division_ID FROM first_level_divisions WHERE COUNTRY_ID = ?";
            DBQuery.setPreparedStatement(conn, getResult);
            ps = DBQuery.getPreparedStatement();
            ps.setInt(1, countryID);
            result = ps.executeQuery();
            while (result.next()) {
                First_Level_Division state = new First_Level_Division();
                state.setDivision_ID(result.getInt("Division_ID"));
                state.setDivision(result.getString("Division"));
                states.add(state);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, result, conn);
        }
        return states;
    }

    public static ObservableList<Country> getCountry() {
        ObservableList<Country> countryList = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            conn = DBConnection.startConnection();
            countryList = observableArrayList();
            String sqlQuery = "SELECT * FROM countries";
            DBQuery.setPreparedStatement(conn, sqlQuery);
            ps = DBQuery.getPreparedStatement();
            result = ps.executeQuery();
            while (result.next()) {
                Country country = new Country();
                country.setCountry_ID(result.getInt("Country_ID"));
                country.setCountry(result.getString("Country"));
                countryList.add(country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, result, conn);
        }
        return countryList;
    }

    public static ObservableList<String> mapCountryID() throws SQLException {
        ObservableList<String> countryIDs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            conn = DBConnection.startConnection();
            countryIDs = observableArrayList();
            String sqlQuery = "SELECT Country_ID FROM countries";
            DBQuery.setPreparedStatement(conn, sqlQuery);
            ps = DBQuery.getPreparedStatement();
            result = ps.executeQuery();
            while (result.next()) {
                countryIDs.add(result.getString("Country_ID"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, result, conn);
        }
        return countryIDs;
    }

    public static ResultSet getCustomerID() {
        ResultSet custID = null;
        try {
            DBQuery.setStatement(Main.conn);
            Statement statement = getStatement();

            String customerID = "SELECT customer_ID FROM customers";
            statement.execute(customerID);
            custID = statement.getResultSet();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return custID;
    }

}