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
import model.Customer;
import utils.DBQuery;
import utils.requests;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javafx.collections.FXCollections.observableArrayList;
import static jdk.javadoc.internal.tool.Main.execute;

public class customerController {

    Stage stage;
    Parent scene;
    private final ResultSet customerRS = new requests().getCustomerData();
    private ResultSet statesRS = new requests().getStates();
    private final ResultSet countryRS = new requests().getCountry();
    private final ResultSet customerIDRS = requests.getCustomerID();
    ObservableList<String> country = observableArrayList();
    ObservableList<String> firstDivision = observableArrayList();
    ObservableList<Customer> customerList = observableArrayList();
    String customerID;
    String customerSelectedState;
    String customerSelectedCountry;

    int countryID;

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
    private ComboBox<String> customerState;

    @FXML
    private ComboBox<String> customerCountry;




    public customerController() throws SQLException {
    }

    @FXML
    void customerCountryHandler(ActionEvent event) throws SQLException {
        getSelectedCountryID();

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
        customerTable.setItems(getCustomer());
    }

    /**
     * puts the data in observable list to be able to be displayed in GUI
     */
    private ObservableList<Customer> getCustomer() {
        ObservableList<Customer> customers = observableArrayList();
        int index = 0;
        while (true) {
            try {
                if (customerRS.next()) {
                    customers.add(new Customer(customerRS.getInt("customer_ID"), customerRS.getString("customer_Name"), customerRS.getString(
                            "address"), customerRS.getString("Postal_Code"), customerRS.getString("phone"), customerRS.getDate(
                            "Create_Date"), customerRS.getString("Created_By"), customerRS.getTimestamp("Last_Update"), customerRS.getString("Last_Updated_By"), customerRS.getInt("Division_ID")));
                    index++;
                } else {
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customers;
    }


    /** Gets all divisions from the database so they can be assigned to the combobox */
    public ObservableList<String> getStates() throws SQLException{
        ObservableList<String> states = observableArrayList();
        int index = 0;
        while(true){
            if (statesRS.next()){
                states.add(statesRS.getString("Division" ));
                index++;
            } else{
                break;
            }
        }
        return states;
    }

    /** Assigns the states retrieved from the DB to the comboBox on the GUI */
    public String stateComboBox() throws SQLException {
       customerState.setItems(getStates());
       customerSelectedState = customerState.getSelectionModel().getSelectedItem();
       return customerSelectedState;
    }


    /** Gets all countries from the database so they can be assigned to the combobox */
    public ObservableList<String> getCountries()  throws SQLException{
        int index = 0;
        while(true){
            if(countryRS.next()){
                country.add(countryRS.getString("country"));
                index++;
            } else{
                break;
            }
        }
        return country;
    }

    /** Sets up the combo box for listed countries */
    public void countryComboBox() throws SQLException {
        customerCountry.setItems(getCountries());
    }

    /** Gets the customers country information and changes the states that are visible in the state combo box. */
    public int getSelectedCountryID() throws SQLException {
        String selected = customerCountry.getValue();
            int index = 0;
            if (selected.contains("U.S")) {
                firstDivision.clear();
                ResultSet states = requests.getUSLocations();
                countryID = 1;
                customerSelectedCountry = "U.S";
                System.out.println(customerSelectedCountry);
                while (states.next()) {
                    firstDivision.add(states.getString("Division"));
                    index++;
                }
                customerState.setItems(firstDivision);
            }
            else if (selected.contains("UK")) {
                firstDivision.clear();
                ResultSet divisions = requests.getUKLocations();
                countryID = 2;
                customerSelectedCountry = "UK";
                System.out.println(customerSelectedCountry);
                while (divisions.next()) {
                    firstDivision.add(divisions.getString("Division"));
                    index++;
                }
                customerState.setItems(firstDivision);
            }
            else if (selected.contains("Canada")) {
                firstDivision.clear();
                ResultSet province = requests.getCanadaLocations();
                countryID = 3;
                customerSelectedCountry = "Canada";
                System.out.println(customerSelectedCountry);
                while (province.next()) {
                    firstDivision.add(province.getString("Division"));
                    index++;
                }
                customerState.setItems(firstDivision);
            }
            return countryID;
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

    /**Gets the new customer address from the GUI menu for the new customer object. */
    public String customerAddress(){
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

    /** Gets the customer post code text field info from the user and assigns it to a variable to be included in the
     * Customer object uploaded to the database.
     */

    public String postalCode(){
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

    /** Gets the customer phone text field info from the user and assigns it to a variable to be included in the Customer
     * object uploaded to the database.
     * @return
     */
    public String customerPhone(){
        String customerPhone = customerPhoneText.getText();

        if (customerPhone.isEmpty()|| customerPhone.length() > 50) {
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
        ResultSet customerIDRSA = requests.getCustomerID();
        int index = 0;
        while (customerIDRSA.next()) {
            {
                customerID = customerIDRSA.getString("Customer_ID");
                return customerID;
            }
        }
        return customerID;
    }


    public void createCustomer() throws SQLException {
       // Customer customer = new Customer(getCustomerID(), customerName(), customerAddress(), postalCode(),
         //       getCustomerID(),  );
        //customer.setCustomer_ID(Integer.parseInt(getCustomerID()));
        Statement statement = DBQuery.getStatement();
        String addCustomer =
                "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES('" + customerName() + "', " +
                        "'" + customerAddress() + "' , '" + postalCode() + "' , '" + customerPhone() + "', '" + getSelectedCountryID() +  "' );";
        statement.execute(addCustomer);
    }

    @FXML
    void editClickedCustomer(MouseEvent event) throws SQLException {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();



        String customerID = String.valueOf(customer.getCustomer_ID());
        String customerName = customer.getCustomer_Name();
        String customerAddress = customer.getAddress();
        String customerPost = customer.getPostal_Code();
        String customerPhone = customer.getPhone();
        customerCountry.setValue(customerSelectedCountry);
//        customerState.setValue(stateComboBox());
        customerNameText.setText(customerName);
        customerIDText.setText(customerID);
        customerAddressText.setText(customerAddress);
        customerPostCodeText.setText(customerPost);
        customerPhoneText.setText(customerPhone);
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
