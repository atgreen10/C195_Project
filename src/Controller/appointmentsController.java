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
import model.Appointment;
import model.Contact;
import utils.requests;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class appointmentsController {

    @FXML
    /** Connection to Database should actually be closed once the SQL query is completed, this is not good practice.*/
    private final ResultSet appointmentsRS = requests.getAppointmentList();
    @FXML
    private final ResultSetMetaData metaData = appointmentsRS.getMetaData();

    private final ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();
    ObservableList<String> startHours = FXCollections.observableArrayList();
    ObservableList<String> startMinutes = FXCollections.observableArrayList();

    Stage stage;
    Parent scene;
    LocalDate startDate;
    LocalDate endDate;
    String selectedEndMinute;
    String selectedEndHour;
    String selectedStartHour;
    String selectedStartMinute;
    LocalTime totalEndTime;
    LocalTime totalStartTime;
    LocalDateTime finalStartTime;
    LocalDateTime finalEndTime;



    @FXML
    private Button backBtn;

    @FXML
    private TableView<Appointment> apptTableView;

    @FXML
    private TextField apptIDText;

    @FXML
    private TextField apptTitleText;

    @FXML
    private TextField apptDescriptionText;

    @FXML
    private TextField apptLocationText;

    @FXML
    private ComboBox<String> apptContactComboBox;

    @FXML
    private TextField apptTypeText;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<String> startHourComboBox;

    @FXML
    private ComboBox<String> startMinuteComboBox;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<String> endHourComboBox;

    @FXML
    private ComboBox<String> endMinuteComboBox;

    @FXML
    private TextField customerIDText;

    @FXML
    private TextField userIDText;

    @FXML
    private Button bookAptBtn;

    @FXML
    private Button deleteApptBtn;

    private boolean isNewAppointment;

    public appointmentsController() throws SQLException {
    }

    /** submits a new appointment to the table and database to be saved and have the GUI reflect the changes.*/
    @FXML
    void bookAptBtnHandler(ActionEvent event) {
        finalStartTime();
        finalEndTime();
        createAppointment();
        //need to fix error where the appointment is added in 3 times to the database and the tableview replaces the current entries and only shows 2 of the same appointment.
        //requests.createNewAppt(createAppointment());
    }

    /** fills the contact combo box with contact ID's */
    @FXML
    void contactHandler(MouseEvent event) {
        ObservableList<String> contacts = requests.contactComboBoxInfo();
        apptContactComboBox.setItems(contacts);
    }

    /** takes the selected Appointment and deletes it from the tableview and observable list.*/
    @FXML
    void deleteApptBtnHandler(MouseEvent event) {
        Appointment selectedAppointment = apptTableView.getSelectionModel().getSelectedItem();
        if(requests.getAppointments().remove(selectedAppointment)){
            String confirmation = "Are you sure you want to delete this appointment?";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText(confirmation);
            alert.showAndWait();
        }
        apptTableView.setItems(requests.getAppointments());
    }

    /** fills the end time minute combobox with values spaced 15 mins apart */
    public void setUpEndMinuteCombo(){
        ObservableList<String> endMinutes = FXCollections.observableArrayList();
        for(int i = 00; i < 60; i+=15){
            endMinutes.add(String.valueOf(i));
            endMinuteComboBox.setItems(endMinutes);
        }
    }

    /** fills the end time hour combobox with values 1 thry 24 to be able to tell AM times from PM times */
    public void setUpEndHourCombo(){
        ObservableList<String> endHours = FXCollections.observableArrayList();
        for (int i = 01; i < 25; i++) {
            endHours.add(String.valueOf(i));
            endHourComboBox.setItems(endHours);
        }
    }

    /** Gets the value of the selection of the end time hour combo box
     * @return*/
    @FXML
    void endHourHandler(ActionEvent event) {
        selectedEndHour = endHourComboBox.getSelectionModel().getSelectedItem();
     }

    /** Gets the value of the selection of the end time minute combo box */
    @FXML
    void endMinuteHandler(ActionEvent event) {
        selectedEndMinute = endMinuteComboBox.getSelectionModel().getSelectedItem();
    }

    LocalTime combineEndTime(){
        String combinedEndTime = selectedEndHour + ":" + selectedEndMinute;
        if(selectedEndHour.length() < 2){
            selectedEndHour = "0"+selectedEndHour;
            combinedEndTime = selectedEndHour + ":" + selectedEndMinute;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        totalEndTime = LocalTime.parse(combinedEndTime, formatter);
        System.out.println(totalEndTime);
        return totalEndTime;
    }

    LocalTime combineStartTime(){
        String combinedStartTime = selectedStartHour + ":" + selectedStartMinute;
        if(selectedStartHour.length() < 2){
            selectedStartHour = "0"+selectedStartHour;
            combinedStartTime = selectedStartHour + ":" + selectedStartMinute;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        totalStartTime = LocalTime.parse(combinedStartTime, formatter);
        System.out.println(totalStartTime);
        return totalStartTime;
    }

    /** fills the start time hour combobox with values 1 thry 24 to be able to tell AM times from PM times */
    public void setUpStartHourCombo(){
        for (int i = 1; i < 25; i++) {
            startHours.add(String.valueOf(i));
            startHourComboBox.setItems(startHours);
        }
    }

    /** fills the start time minute combobox with values spaced 15 mins apart */
    public void setUpStartMinuteCombo(){
        for(int i = 00; i < 60; i+=15){
            startMinutes.add(String.valueOf(i));
            startMinuteComboBox.setItems(startMinutes);
        }
    }

    /** Gets the value of the selection of the start time hour combo box */
    @FXML
    String startHourHandler(ActionEvent event) {
        selectedStartHour = startHourComboBox.getSelectionModel().getSelectedItem();
        System.out.println(selectedStartHour);
        return selectedStartHour;
    }

    /** Gets the value of the selection of the start time minute combo box */
    @FXML
    String startMinuteHandler(ActionEvent event) {
        selectedStartMinute = startMinuteComboBox.getSelectionModel().getSelectedItem();
        System.out.println(selectedStartMinute);
        return selectedStartMinute;
    }

    /** gets the value of the start date from the datepicker on the GUI */
    @FXML
    void startDatePickerHandler(ActionEvent event) {
        startDate = startDatePicker.getValue();
    }

    /** gets the value of the end date from the datepicker on the GUI */
    @FXML
    void endDatePickerHandler(ActionEvent event) {
       endDate = endDatePicker.getValue();
    }

    /** Creates the LocalDateTime startTime value needed for conversions to other timezones or to input info to database */
    LocalDateTime finalStartTime (){
        finalStartTime = LocalDateTime.of(startDate, combineStartTime());
        System.out.println("finalStartTime: " + finalStartTime);
        return finalStartTime;
    }

    /** Creates the LocalDateTime endTime value needed for conversions to other timezones or to input info to database */
    LocalDateTime finalEndTime (){
       finalEndTime = LocalDateTime.of(endDate, combineEndTime());
       System.out.println("finalEndTime: " + finalEndTime);
       return finalEndTime;
    }

    /** when back button is clicked, program takes user back to the main page and cancels any changes */
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
     * Creates columns spaces in the GUI for the appointment table
     */
    private void createColumns() {
        for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
            apptTableView.getColumns().add(columnIndex, setTableColumn(columnIndex + 1));

        }
    }

    /** gets the column names from the database columns */
    private ResultSetMetaData getMetaData() {
        try {
            return appointmentsRS.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

/** Count the columns from the info in the database */
    private int getColumnCount() {
        try {
            return getMetaData().getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** Sets up the column names for the appointment tableview */
    private TableColumn setTableColumn(int columnIndex) {
        int type = 0;
        String name = null;
        try {
            type = appointmentsRS.getType();
            name = Objects.requireNonNull(getMetaData()).getColumnName(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        switch (type) {
            case 1:
                return new TableColumn<Appointment, Integer>(name);
            case 4:
                return new TableColumn<Appointment, String>(name);
            case 6:
                return new TableColumn<Appointment, String>(name);
            case 8:
                return new TableColumn<Appointment, String>(name);
            case 12:
                return new TableColumn<Appointment, String>(name);
            case 91:
                return new TableColumn<Appointment,LocalDateTime >(name);
            case 92:
                return new TableColumn<Appointment, LocalDateTime>(name);
            case 93:
                return new TableColumn<Appointment,Integer >(name);
            case 94:
                return new TableColumn<Appointment, Integer>(name);
            case 98:
                return new TableColumn<Appointment, Integer>(name);
            default:
                return new TableColumn(name);
        }
    }
/** sets up the appointment table data in the tableview */
    private void setTableData() {
        for (int i = 0; i < apptTableView.getColumns().size(); i++) {
            TableColumn col = apptTableView.getColumns().get(i);
            switch (i) {
                case 0:
                    col.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                    break;
                case 1:
                    col.setCellValueFactory(new PropertyValueFactory<>("title"));
                    break;
                case 2:
                    col.setCellValueFactory(new PropertyValueFactory<>("description"));
                    break;
                case 3:
                    col.setCellValueFactory(new PropertyValueFactory<>("Location"));
                    break;
                case 4:
                    col.setCellValueFactory(new PropertyValueFactory<>("apptType"));
                    break;
                case 5:
                    col.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
                    break;
                case 6:
                    col.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
                    break;
                case 7:
                    col.setCellValueFactory(new PropertyValueFactory<>("customerID"));
                    break;
                case 8:
                    col.setCellValueFactory(new PropertyValueFactory<>("userID"));
                    break;
                case 9:
                    col.setCellValueFactory(new PropertyValueFactory<>("contact"));
                    break;
            }
        }
        apptTableView.setItems(requests.getAppointments());
    }
/** creates a mew Appointment object */
    public Appointment createAppointment() {
        Appointment appointment = new Appointment();
        appointment.setTitle(apptTitleText.getText());
        appointment.setDescription(apptDescriptionText.getText());
        appointment.setLocation(apptLocationText.getText());
        appointment.setApptType(apptTypeText.getText());
        appointment.setStartDate(startDate);
        appointment.setStartTime(totalStartTime);
        appointment.setEndDate(endDate);
        appointment.setEndTime(totalEndTime);
        appointment.setStartDateTime(finalStartTime());
        appointment.setEndDateTime(finalEndTime());
        appointment.setCustomerID(String.valueOf(customerIDText.getText()));
        appointment.setUserID(String.valueOf(userIDText.getText()));
        appointment.setContact(appointment.getContact());

        appointmentObservableList.add(appointment);
        apptTableView.setItems(appointmentObservableList);
        requests.createNewAppt(appointment);
        apptTableView.refresh();
        return appointment;
    }


    /** edits an existing appointment object from the table */
    @FXML
    public void editAppointment() {
        Appointment appointment = apptTableView.getSelectionModel().getSelectedItem();
//        First_Level_Division state = divisionIDtoStates.get(customer.getDivision_ID());

        isNewAppointment = false;
        apptTitleText.setText(appointment.getTitle());
        apptDescriptionText.setText(appointment.getDescription());
        apptLocationText.setText(appointment.getLocation());
        apptTypeText.setText(appointment.getApptType());
      //  LocalTime hours = LocalTime.from(startHourComboBox.getSelectionModel().getSelectedItem());
        customerIDText.setText(String.valueOf(appointment.getCustomerID()));
        userIDText.setText(String.valueOf(appointment.getUserID()));

    }


/** first function run to set up the page for viewing */
    public void initialize() {
        createColumns();
        setTableData();

       setUpStartHourCombo();
       setUpEndHourCombo();
       setUpStartMinuteCombo();
       setUpEndMinuteCombo();
    }

}