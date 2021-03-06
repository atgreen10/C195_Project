package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;

import static javafx.collections.FXCollections.observableArrayList;

public class requests {

    /**queries the database for a userName and pulls its password as a result in order to authenticate logging in the application.
     */
    public static String validLogin(String userName){
        String password = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = DBConnection.startConnection();
            String check = "SELECT Password FROM users where User_Name = ?";
            DBQuery.setPreparedStatement(conn, check);
            ps = DBQuery.getPreparedStatement();
            ps.setString(1, userName);
            rs = ps.executeQuery();
            while(rs.next()){
                password = rs.getString("Password");
            }
            if(!rs.next()){
                return password;
            }
        }
        catch(SQLException e){ ;
            e.printStackTrace();
        }
        finally{
            DBConnection.closeAll(ps,rs,conn);
        }
        return password;
    }

    /**
     * gets the usernames that are in the database.
     */
    public static ObservableList<User> getUser() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(statement, rs, conn);
        }
        return userList;
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

    public static LocalDateTime getStartTime() {
        Timestamp startTime = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        LocalDateTime convertedStartTime = null;
        try {
            conn = DBConnection.startConnection();
            String request = "SELECT start FROM appointments";
            DBQuery.setPreparedStatement(conn, request);
            ps = DBQuery.getPreparedStatement();
            result = ps.executeQuery();
            startTime = result.getTimestamp("Start");
            convertedStartTime = startTime.toLocalDateTime();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, result, conn);
        }
        return convertedStartTime;
    }

    public static LocalDateTime getEndTime() {
        Timestamp endTime = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        LocalDateTime convertedEndTime = null;
        try {
            conn = DBConnection.startConnection();
            String request = "SELECT end FROM appointments";
            DBQuery.setPreparedStatement(conn, request);
            ps = DBQuery.getPreparedStatement();
            result = ps.executeQuery();
            endTime = result.getTimestamp("End");
            convertedEndTime = endTime.toLocalDateTime();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, result, conn);
        }
        return convertedEndTime;
    }

    public static ResultSet getAppointmentList() throws SQLException {
        return DBConnection.startConnection().createStatement().executeQuery("SELECT Appointment_ID, Title, " +
                "Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments");
    }

    public static ObservableList<Appointment> getAppointments() {
        ObservableList<Appointment> appointmentList = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            conn = DBConnection.startConnection();
            appointmentList = observableArrayList();
            String request = "SELECT * FROM appointments";
            DBQuery.setPreparedStatement(conn, request);
            ps = DBQuery.getPreparedStatement();
            result = ps.executeQuery();
            while (result.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentID(result.getString("Appointment_ID"));
                appointment.setTitle(result.getString("Title"));
                appointment.setDescription(result.getString("Description"));
                appointment.setLocation(result.getString("Location"));
                appointment.setApptType(result.getString("Type"));
                appointment.setStartDateTime(result.getTimestamp("Start").toLocalDateTime());
                appointment.setStartDateTime(result.getTimestamp("End").toLocalDateTime());
                appointment.setCustomerID(result.getString("Customer_ID"));
                appointment.setUserID(result.getString("User_ID"));
                appointment.setContact(result.getString("Contact_ID"));

                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, result, conn);
        }
        return appointmentList;
    }

    public static ObservableList<String> contactComboBoxInfo() {
        ObservableList<String> contactinfo = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            conn = DBConnection.startConnection();
            contactinfo = FXCollections.observableArrayList();
            String request = "SELECT Contact_ID, Contact_Name FROM contacts";
            DBQuery.setPreparedStatement(conn, request);
            ps = DBQuery.getPreparedStatement();
            result = ps.executeQuery();
            while (result.next()) {
                contactinfo.add(result.getString("Contact_ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, result, conn);
        }
        return contactinfo;
    }

    public static ObservableList<Contact> getContacts (String contactID) {
        ObservableList<Contact> contacts = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            conn = DBConnection.startConnection();
            contacts = observableArrayList();
            String request = "SELECT Contact_Name, Contact_ID FROM contacts WHERE Contact_ID = ?";
            DBQuery.setPreparedStatement(conn, request);
            ps = DBQuery.getPreparedStatement();
            ps.setString(1, contactID);
            result = ps.executeQuery();
            while (result.next()) {
              Contact contact = new Contact();
              contact.setContactName(result.getString("Contact_Name"));
              contact.setContactID(result.getString("Contact_ID"));
              contacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, result, conn);
        }
        return contacts;
    }

    public static ObservableList<Contact> getContacts () {
        ObservableList<Contact> contacts = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            conn = DBConnection.startConnection();
            contacts = observableArrayList();
            String request = "SELECT * FROM contacts";
            DBQuery.setPreparedStatement(conn, request);
            ps = DBQuery.getPreparedStatement();
            result = ps.executeQuery();
            while (result.next()) {
                Contact contact = new Contact();
                contact.setContactName(result.getString("Contact_Name"));
                contact.setContactID(result.getString("Contact_ID"));
                contacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(ps, result, conn);
        }
        return contacts;
    }

    public static void createNewAppt(Appointment appointment){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = DBConnection.startConnection();
            String insertAppt = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?)";
            DBQuery.setPreparedStatement(conn, insertAppt);
            ps = DBQuery.getPreparedStatement();
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getApptType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getStartDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getEndDateTime()));
            ps.setString(7, appointment.getCustomerID());
            ps.setString(8, appointment.getUserID());
            ps.setString(9, appointment.getContact());
            ps.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            DBConnection.closeAll(ps, null, conn);
        }
    }
}
