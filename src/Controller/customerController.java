package Controller;

import javafx.collections.FXCollections;
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
import model.User;
import utils.requests;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static javafx.collections.FXCollections.observableArrayList;

public class customerController {

    Stage stage;
    Parent scene;


    /** Connection to Database should actually be closed once the SQL query is completed, this is not good practice.*/
    private final ResultSet customerRS = new requests().getCustomerData();

    Country selectedCountry;
    Boolean isNewCustomer = true;
    Boolean isSelected = false;

    ObservableList<Customer> customers = observableArrayList();
    ObservableList<First_Level_Division> states = null;
    ObservableList<Country> country = null;
    Map<Country, ObservableList<First_Level_Division>> countryStateMap = new HashMap<>();
    Map<String, Integer> countryIDMap = new HashMap<>();
    Map<Integer, First_Level_Division> divisionIDtoStates = new HashMap<>();
    Map<First_Level_Division, Country> stateToCountry = new HashMap<>();


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
        customerState.setItems(getSelectedCountryStates());
    }


    @FXML
    void customerStateHandler(ActionEvent event) {
    }

    /**
     * checks if the customer is already added to the database and either creates a new customer or edits an
     * existing customer account.
     */
    @FXML
    void submitBtnHandler(MouseEvent event) throws IOException {
        if (isNewCustomer) {
            customers.add(createCustomer());
            requests.setNewCustomer(createCustomer());
        } else {
            requests.updateCustomer(editCustomer());
        }

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
    private void setTableData() {
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
        customerTable.setItems(requests.getAllCustomers());
    }

    /**
     * puts the data in observable list to be able to be displayed in

    /**
     * Sets up the combo box for listed countries
     */
    public void countryComboBox() {
        ObservableList <Country> countries = FXCollections.observableArrayList();
        countries.addAll(countryStateMap.keySet());
        customerCountry.setItems(countries);

        /** lambda takes country objects and gets their country_ID for comparison and returns a specific integer
         * based on whether it is more or less than the country being compared to in order to implement a way to
         * order the values within the list because sets are unordered */
        countries.sort((o1, o2) -> {
            if (o1.getCountry_ID() == o2.getCountry_ID())
                return 0;
            else if (o1.getCountry_ID() < o2.getCountry_ID()) {
                return -1;
            } else {
                return 1;
            }
        });

    }

    /**
     * Maps the state options to their respective countries based on the country ID and returns the correct states as
     * available options
     */
    public ObservableList<First_Level_Division> getSelectedCountryStates() {
        selectedCountry = customerCountry.getSelectionModel().getSelectedItem();
        return countryStateMap.get(selectedCountry);
    }

    public void populatesCountryAndState() {
        for (Country c : requests.getCountry()) {
           ObservableList<First_Level_Division> states = requests.getStates(c.getCountry_ID());
            countryStateMap.put(c, states);
            for(First_Level_Division state : states){
                divisionIDtoStates.put(state.getDivision_ID(), state);
                stateToCountry.put(state, c);
            }
        }
    }

    /**
     * gets customer name and performs checks on input submissions.
     */
    public String customerName() {
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

    /**
     * Creates a new customer object and assigns the information to its respective categories.
     */
    public Customer createCustomer(){
 //       String customerCreatedBy = User.getUserName();
        isNewCustomer = true;
        Customer customer = new Customer();
        customer.setCustomer_Name(customerName());
        customer.setAddress(customerAddress());
        customer.setPostal_Code(postalCode());
        customer.setCustomer_Phone(customerPhone());
        customer.setDivision_ID(customerState.getSelectionModel().getSelectedItem().getDivision_ID());
//        customer.setCreated_By(customerCreatedBy);
 //       customer.setLast_Updated_By(User.getUserName());
        return customer;
    }

    /**
     * Populates the customer information on a previously created customer object.
     */
    @FXML
    public void existingCustomerInfo() {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        First_Level_Division state = divisionIDtoStates.get(customer.getDivision_ID());

            isNewCustomer = false;
            customerState.setValue(state);
            customerNameText.setText(customer.getCustomer_Name());
            customerPhoneText.setText(customer.getPhone());
            customerAddressText.setText(customer.getAddress());
            customerPostCodeText.setText(customer.getPostal_Code());
            customerCountry.getSelectionModel().select(stateToCountry.get(state));
            customerState.getSelectionModel().select(state);
            System.out.println(customerCountry.getItems().size());
        }

    /**
     * makes updates to already existing customer object.
     */
    public Customer editCustomer() {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        customer.setCustomer_Name(customerNameText.getText());
        customer.setCustomer_Phone(customerPhoneText.getText());
        customer.setAddress(customerAddressText.getText());
        customer.setPostal_Code(customerPostCodeText.getText());
      //  customer.setCreated_By(customer.getCreated_By());
 //       customer.setLast_Updated_By(User.getUserName());
        customer.setDivision_ID(customer.getDivision_ID());

        return customer;
    }

    /**
     * Clears selected customer info from textfields
     */
    @FXML
    void clearBtnHandler(MouseEvent event) {
        customerTable.getSelectionModel().clearSelection();
        isNewCustomer = true;
        customerNameText.clear();
        customerPhoneText.clear();
        customerPostCodeText.clear();
        customerAddressText.clear();
        customerIDText.clear();
        customerState.setValue(null);
        customerCountry.setValue(null);
    }


    public void initialize() {
        createColumns();
        setTableData();
        populatesCountryAndState();
        countryComboBox();
    }


}