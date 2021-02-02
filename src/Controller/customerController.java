package Controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.First_Level_Division;
import utils.DBQuery;
import utils.requests;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static javafx.collections.FXCollections.observableArrayList;

public class customerController {

    Stage stage;
    Parent scene;
    private final ResultSet customerRS = new requests().getCustomerData();
    private final ResultSet customerIDNum = requests.getCustomerID();
    Country selectedCountry;

    ObservableList <First_Level_Division> states = null;
    ObservableList<Country> country = null;
    ObservableList<String> customerID = observableArrayList();
    Map<String, ObservableList<First_Level_Division>> countryStateMap = new HashMap<>();
    Map<String, Integer> countryIDMap = new HashMap<>();


    @FXML
    private final ResultSetMetaData metaData = customerRS.getMetaData();

    @FXML
    private Button backBtn;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> customerIDColumn;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableColumn<Customer, String> customerAddressColumn;

    @FXML
    private TableColumn<Customer, String> customerPostalCodeColumn;

    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;

    @FXML
    private TableColumn<Customer, LocalDate> customerCreateDateColumn;

    @FXML
    private TableColumn<Customer, String> customerCreatedByColumn;

    @FXML
    private TableColumn<Customer, Timestamp> customerLastUpdatedColumn;

    @FXML
    private TableColumn<Customer, String> customerLastUpdatedByColumn;

    @FXML
    private TableColumn<Customer, Integer> CustomerDivisionIDColumn;

    @FXML
    private Button submitBtn;

    @FXML
    private TextField customerIDText;

    @FXML
    private TextField customerNameText;

    @FXML
    private TextField customerAddressText;

    @FXML
    private TextField customerPostCodeText;

    @FXML
    private TextField customerPhoneText;

    @FXML
    private ComboBox<First_Level_Division> customerState;

    @FXML
    private ComboBox<Country> customerCountry;


    public customerController() throws SQLException {
    }

    @FXML
    void customerCountryHandler(ActionEvent event) {
        getSelectedCountryStates();
    }


    @FXML
    void customerStateHandler(MouseEvent event) throws SQLException {
    }

    @FXML
    void submitBtnHandler(MouseEvent event) throws SQLException, IOException {
        createCustomer();

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/mainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     * When the back button is pressed, the application takes the user back to the main application screen
     */
    @FXML
    void backBtnHandler(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/mainMenu.fxml"));
        loader.load();

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     * Creates columns spaces in the GUI for the Customer table
     */
    private void createColumns() {
        for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
            customerTable.getColumns().add(columnIndex, setTableColumn(columnIndex + 1));
        }
    }

    /**
     * Gets information about the table from the Database
     */
    private ResultSetMetaData getMetaData() {
        try {
            return customerRS.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getColumnCount() {
        try {
            return getMetaData().getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * sets up the column names in GUI
     */
    private TableColumn setTableColumn(int columnIndex) {
        int type = 0;
        String name = null;
        try {
            type = customerRS.getType();
            name = getMetaData().getColumnName(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        switch (type) {
            case 1:
                return new TableColumn<Customer, Integer>(name);
            case 4:
                return new TableColumn<Customer, String>(name);
            case 6:
                return new TableColumn<Customer, String>(name);
            case 8:
                return new TableColumn<Customer, String>(name);
            case 12:
                return new TableColumn<Customer, String>(name);
            case 91:
                return new TableColumn<Customer, LocalDateTime>(name);
            case 92:
                return new TableColumn<Customer, String>(name);
            case 93:
                return new TableColumn<Customer, Timestamp>(name);
            case 94:
                return new TableColumn<Customer, String>(name);
            case 95:
                return new TableColumn<Customer, Integer>(name);
            default:
                return new TableColumn(name);
        }
    }

    /**
     * assigns the data from the database to the table in the GUI
     */
    private void setTableData() throws SQLException {
        for (int i = 0; i < customerTable.getColumns().size(); i++) {
            TableColumn col = customerTable.getColumns().get(i);
            switch (i) {
                case 0:
                    col.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
                    break;
                case 1:
                    col.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
                    break;
                case 2:
                    col.setCellValueFactory(new PropertyValueFactory<>("address"));
                    break;
                case 3:
                    col.setCellValueFactory(new PropertyValueFactory<>("Postal_Code"));
                    break;
                case 4:
                    col.setCellValueFactory(new PropertyValueFactory<>("phone"));
                    break;
                case 5:
                    col.setCellValueFactory(new PropertyValueFactory<>("Create_Date"));
                    break;
                case 6:
                    col.setCellValueFactory(new PropertyValueFactory<>("Created_By"));
                    break;
                case 7:
                    col.setCellValueFactory(new PropertyValueFactory<>("Last_Update"));
                    break;
                case 8:
                    col.setCellValueFactory(new PropertyValueFactory<>("Last_Updated_By"));
                    break;
                case 9:
                    col.setCellValueFactory(new PropertyValueFactory<>("Division_ID"));
                    break;
            }
        }
        customerTable.setItems(getCustomer());
    }

    /**
     * puts the data in observable list to be able to be displayed in GUI
     */
    private ObservableList<Customer> getCustomer() {
        ObservableList<Customer> customers = observableArrayList();
        int index = 0;
        //  while (true) {
        try {
            while (customerRS.next()) {
                customers.add(new Customer(customerRS.getInt("customer_ID"), customerRS.getString("customer_Name"), customerRS.getString(
                        "address"), customerRS.getString("Postal_Code"), customerRS.getString("phone"), customerRS.getDate(
                        "Create_Date"), customerRS.getString("Created_By"), customerRS.getTimestamp("Last_Update"), customerRS.getString("Last_Updated_By"), customerRS.getInt("Division_ID")));
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }


    /**
     * Assigns the states retrieved from the DB to the combo box on the GUI
     */
    public void stateComboBox() {
        states = requests.getAllStates();
        customerState.setItems(states);
    }



    /**
     * Sets up the combo box for listed countries
     */
    public void countryComboBox(){
        country = requests.getCountry();
        for (Country country1 : country) {
            countryIDMap.put(country1.getCountry(), country1.getCountry_ID());
        }
        customerCountry.setItems(country);

    }

    /**
     * Maps the state options to their respective countries and returns the correct states as available options
     * depending on the country selected.
     */
    public ObservableList<First_Level_Division> getSelectedCountryStates() {
        selectedCountry = customerCountry.getSelectionModel().getSelectedItem();
        ObservableList<First_Level_Division> states = null;
        ObservableList <First_Level_Division> state = null;
        if(selectedCountry == null){
            state = observableArrayList();
            return state;
        }
        if (countryStateMap.containsKey(selectedCountry.getCountry())) {
            states = countryStateMap.get(selectedCountry.getCountry());
            return (states);
        } else {
            states = requests.getStates(selectedCountry.getCountry_ID());
            countryStateMap.put(selectedCountry.getCountry(), states);
        }
        System.out.println("This is states: " + states);
        customerState.setItems(states);
        return states;
    }


    public String customerName() throws SQLException {
        String customerName = customerNameText.getText();

        if (customerName.isEmpty() || customerName.length() > 50) {
            String error = "Customer name should be between 1-50 characters";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Customer name is invalid");
            alert.setContentText(error);

            alert.showAndWait();
        }
        return customerName;
    }

    /**
     * Gets the new customer address from the GUI menu for the new customer object.
     */
    public String customerAddress() {
        String customerAddress = customerAddressText.getText();

        if (customerAddress.isEmpty() || customerAddress.length() > 100) {
            String error = "Invalid Customer Address";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Customer name is invalid");
            alert.setContentText(error);

            alert.showAndWait();
        }
        return customerAddress;
    }

    /**
     * Gets the customer post code text field info from the user and assigns it to a variable to be included in the
     * Customer object uploaded to the database.
     */

    public String postalCode() {
        String postalCode = customerPostCodeText.getText();

        if (postalCode.isEmpty() || postalCode.length() > 50) {
            String error = "Postal Code is invalid.";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Customer name is invalid");
            alert.setContentText(error);

            alert.showAndWait();
        }
        return postalCode;
    }

    /**
     * Gets the customer phone text field info from the user and assigns it to a variable to be included in the Customer
     * object uploaded to the database.
     *
     * @return
     */
    public String customerPhone() {
        String customerPhone = customerPhoneText.getText();

        if (customerPhone.isEmpty() || customerPhone.length() > 50) {
            String error = "Postal Code is invalid.";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Customer name is invalid");
            alert.setContentText(error);

            alert.showAndWait();
        }
        return customerPhone;
    }

//    public String createdBy() throws SQLException {
//        String createdBy =

    public String getCustomerID() throws SQLException {
        while (customerIDNum.next()) {
            {
                customerID.add(String.valueOf(customerIDNum));
            }
        }
        return String.valueOf(customerID);
    }

    public void createCustomer() throws SQLException {
        // Customer customer = new Customer(getCustomerID(), customerName(), customerAddress(), postalCode(),
        //customer.setCustomer_ID(Integer.parseInt(getCustomerID()));
        Statement statement = DBQuery.getStatement();
        String addCustomer =
                "INSERT INTO customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES" +
                        "('" + getCustomerID() + ",'" + customerName() + "', " +
                        "'" + customerAddress() + "' , '" + postalCode() + "' , '" + customerPhone() + "', '" + getSelectedCountryStates() + "' );";
        statement.execute(addCustomer);
    }

    public void getDivisionID() {

    }


    @FXML
    void editClickedCustomer(MouseEvent mouseEvent) throws SQLException {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
       Country selectCountry = selectedCountry;

        String customerID = String.valueOf(customer.getCustomer_ID());
        String customerName = customer.getCustomer_Name();
        String customerAddress = customer.getAddress();
        String customerPost = customer.getPostal_Code();
        String customerPhone = customer.getPhone();
        customerCountry.setValue(selectedCountry);
//      customerState.setValue();
        customerNameText.setText(customerName);
        customerIDText.setText(customerID);
        customerAddressText.setText(customerAddress);
        customerPostCodeText.setText(customerPost);
        customerPhoneText.setText(customerPhone);
    }

    @FXML
    void clearBtnHandler(MouseEvent event) {
        customerNameText.clear();
        customerPhoneText.clear();
        customerPostCodeText.clear();
        customerAddressText.clear();
        customerIDText.clear();
        customerState.setValue(null);
        customerCountry.setValue(null);
    }



        /**
         * First method run on the page
         */
    public void initialize() throws SQLException {
        createColumns();
        setTableData();
        countryComboBox();
        stateComboBox();

    }


}