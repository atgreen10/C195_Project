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
import utils.requests;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javafx.collections.FXCollections.observableArrayList;

public class customerController {

    Stage stage;
    private final ResultSet customerRS = new requests().getCustomerData();
    private ResultSet statesRS = new requests().getStates();
    private final ResultSet countryRS = new requests().getCountry();
    ObservableList<String> usStates = observableArrayList();
    ObservableList<String> country = observableArrayList();
    ObservableList<String> firstDivision = observableArrayList();
    String selected;

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
    private TextField customerNameText;

    @FXML
    private TextField customerAddressText;

    @FXML
    private TextField customerPostCodeText;

    @FXML
    private ComboBox<String> customerState;

    @FXML
    private ComboBox<String> customerCountry;

    public customerController() throws SQLException {
    }

    @FXML
    void customerCountryHandler(ActionEvent event) throws SQLException {
        selected = customerCountry.getValue();

        getSelectedCountryID();

    }



    @FXML
    void customerStateHandler(MouseEvent event) throws SQLException {


    }

    @FXML
    void submitBtnHandler(MouseEvent event) {
       /* String customerName = customerNameText.getText();
        String customerAddress = customerAddressText.getText();
        String customerPostCode = customerPostCodeText.getText();
        String customer */

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
    public void stateComboBox() throws SQLException {
        customerState.setItems(getSelectedCountryID());
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

    public void countryComboBox() throws SQLException{
        customerCountry.setItems(getCountries());
        selected = customerCountry.getValue();

    }

    public ObservableList<String> getSelectedCountryID() throws SQLException {
            while(statesRS.next()) {
                if (selected.contains("U.S")) {
                    countryID = 1;
                    try {
                        statesRS = requests.getUSLocations();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    String USstates = null;
                    try {
                        USstates = statesRS.getString("division");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    firstDivision.add(USstates);
                    return firstDivision;
                }
                else if(selected.contains("UK")){
                    countryID = 2;
                    try {
                        statesRS = requests.getUKLocations();
                    } catch(SQLException throwables){
                        throwables.printStackTrace();
                    }
                    String UKlocations = null;
                    try{
                        UKlocations = statesRS.getString("division");
                    }
                    catch(SQLException throwables){
                        throwables.printStackTrace();
                    }
                    firstDivision.add(UKlocations);
                    return firstDivision;
                }
                else if(selected.contains("Canada")){
                    countryID = 3;
                    try {
                        statesRS = requests.getCanadaLocations();
                    } catch(SQLException throwables){
                        throwables.printStackTrace();
                    }
                    String canadaLocations = null;
                    try{
                        canadaLocations = statesRS.getString("division");
                    }
                    catch(SQLException throwables){
                        throwables.printStackTrace();
                    }
                    firstDivision.add(canadaLocations);
                    return firstDivision;
                }
            }
            return null;
    }


    /**
     * First method run on the page
     */
    public void initialize() throws SQLException {
        createColumns();
        setTableData();

        stateComboBox();
        countryComboBox();
        getStates();
    }



}
