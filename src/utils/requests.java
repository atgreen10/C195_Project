package utils;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import model.Country;
import model.Customer;
import model.First_Level_Division;
import model.User;
import sample.Main;

import java.sql.*;

import static javafx.collections.FXCollections.observableArrayList;
import static utils.DBQuery.getStatement;

public class requests {

    /**
     * gets the usernames that are in the database.
     */
    public static ObservableList<User> getUser(){
        ObservableList<User> userList = null;
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.startConnection();
            String selectUsers = "SELECT * from USER";
            DBQuery.setPreparedStatement(conn, selectUsers);
            statement = DBQuery.getPreparedStatement();
            statement.execute(selectUsers);
            rs = statement.getResultSet();
            userList = observableArrayList();
            while (rs.next()) {
                User user = new User(rs.getString("User_Name"), rs.getString("Password"));
                userList.add(user);
            }
            return userList;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            DBConnection.closeAll(statement, rs, conn);
        }
        return userList;
    }

    /**
     * gets the users password from the database.
     */
    public static String getPassword() throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection conn = null;
        String error;
        try {
            conn = DBConnection.startConnection();
            String acceptedPasswords = "SELECT Password FROM users";
            DBQuery.setPreparedStatement(Main.conn, acceptedPasswords);
            statement = DBQuery.getPreparedStatement();
            statement.execute(acceptedPasswords);
            rs = statement.getResultSet();
            if (rs == null) {
                error = "Password does not match!";
                return error;
            } else {
                return rs.getString("Password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(statement, rs, conn);
        }
        return rs.getString("Password");
    }

    /**
     * runs the query to the database for customer data
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> allCustomers = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            conn = DBConnection.startConnection();
            allCustomers = observableArrayList();
            String getResult = "SELECT * FROM customers";
            DBQuery.setPreparedStatement(conn, getResult);
            ps = DBQuery.getPreparedStatement();
            result = ps.executeQuery();
            while (result.next()) {
                Customer customer = new Customer(result.getInt("customer_ID"), result.getString("customer_Name"),
                        result.getString("address"), result.getString("Postal_Code"), result.getString("phone"), result.getDate(
                        "Create_Date"), result.getString("Created_By"), result.getTimestamp("Last_Update"),
                        result.getString("Last_Updated_By"), result.getInt("Division_ID"));
                allCustomers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, result, conn);
        }
        return allCustomers;
    }

    /**
     * This doesn't close the connection to the Database like it should
     */
    public ResultSet getCustomerData() throws SQLException {
        return DBConnection.startConnection().createStatement().executeQuery("SELECT * FROM customers");
    }

//    public static ObservableList<First_Level_Division> getAllStatdwwwwwwwwwwes() {
//        ObservableList<First_Level_Division> allStates = null;
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet result = null;
//        try {
//            conn = DBConnection.startConnection();
//            allStates = observableArrayList();
//            String getResult = "SELECT Division FROM first_level_divisions";
//            DBQuery.setPreparedStatement(conn, getResult);
//            ps = DBQuery.getPreparedStatement();
//            result = ps.executeQuery();
//            while (result.next()) {
//                First_Level_Division state = new First_Level_Division();
//                state.setDivision(result.getString("Division"));
//                allStates.add(state);
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } finally {
//            DBConnection.closeAll(ps, result, conn);
//        }
//        return allStates;
//    }

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


//    public static ObservableList<String> mapCountryID() throws SQLException {
//        ObservableList<String> countryIDs = null;
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet result = null;
//        try {
//            conn = DBConnection.startConnection();
//            countryIDs = observableArrayList();
//            String sqlQuery = "SELECT Country_ID FROM countries";
//            DBQuery.setPreparedStatement(conn, sqlQuery);
//            ps = DBQuery.getPreparedStatement();
//            result = ps.executeQuery();
//            while (result.next()) {
//                countryIDs.add(result.getString("Country_ID"));
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } finally {
//            DBConnection.closeAll(ps, result, conn);
//        }
//        return countryIDs;
//    }

//    public static ResultSet getCustomerID() {
//        ResultSet custID = null;
//        try {
//            DBQuery.setStatement(Main.conn);
//            Statement statement = getStatement();
//
//            String customerID = "SELECT customer_ID FROM customers";
//            statement.execute(customerID);
//            custID = statement.getResultSet();
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return custID;
//    }


    public static void setNewCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.startConnection();
            String insertCustomer = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Created_By, " +
                    "Last_Updated_By, Division_ID) VALUES( " +
                    "?, ?, ?, ?, ?, ?, ?); ";
            DBQuery.setPreparedStatement(conn, insertCustomer);
            ps = DBQuery.getPreparedStatement();
            ps.setString(1, customer.getCustomer_Name());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostal_Code());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getCreated_By());
            ps.setString(6, customer.getLast_Updated_By());
            ps.setInt(7, customer.getDivision_ID());

            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, null, conn);
        }
        System.out.println(customer);
    }

    public static void updateCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.startConnection();
            String updateCustomer = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?," +
                    " Created_By = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?;";
            DBQuery.setPreparedStatement(conn, updateCustomer);
            ps = DBQuery.getPreparedStatement();
            ps.setString(1, customer.getCustomer_Name());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostal_Code());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getCreated_By());
            ps.setString(6, customer.getLast_Updated_By());
            ps.setInt(7, customer.getDivision_ID());
            ps.setInt(8, customer.getCustomer_ID());
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, null, conn);
        }
    }

}