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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class appointmentsController {

    @FXML
    /** Connection to Database should actually be closed once the SQL query is completed, this is not good practice.*/
    private final ResultSet appointmentsRS = requests.getAppointmentList();
    @FXML
    private final ResultSetMetaData metaData = appointmentsRS.getMetaData();

    private final ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();
    ObservableList<Integer> startHours = FXCollections.observableArrayList();
    ObservableList<Integer> startMinutes = FXCollections.observableArrayList();
    ObservableList<Integer> endHours = FXCollections.observableArrayList();
    ObservableList<Integer> endMinutes = FXCollections.observableArrayList();
    ObservableList<Contact> contacts = requests.contactComboBoxInfo();
    Map<Integer, String> contactIDtoNames = new HashMap<>();


    Stage stage;
    Parent scene;
    LocalDate startDate;
    LocalDate endDate;
    int selectedEndMinute;
    int selectedEndHour;
    int selectedStartHour;
    int selectedStartMinute;
    LocalTime totalEndTime;
    LocalTime totalStartTime;
    LocalDateTime finalStartTime;
    LocalDateTime finalEndTime;
    Contact selectedContact;



//    LocalTime timeEST  = LocalTime.of(selectedStartHour, selectedStartMinute);
//    LocalTime local = LocalTime.now();
//    ZoneId zoneEST = ZoneId.of("America/New York");
//    ZonedDateTime estTime = ZonedDateTime.of(startDate, timeEST, zoneEST);
//    ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());


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
    private ComboBox<Contact> apptContactComboBox;

    @FXML
    private TextField apptTypeText;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<Integer> startHourComboBox;

    @FXML
    private ComboBox<Integer> startMinuteComboBox;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<Integer> endHourComboBox;

    @FXML
    private ComboBox<Integer> endMinuteComboBox;

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


    /**
     * submits a new appointment to the table and database to be saved and have the GUI reflect the changes.
     */
    @FXML
    void bookAptBtnHandler(ActionEvent event) {
        finalStartTime();
        finalEndTime();
        createAppointment();
        apptTableView.refresh();
    }

    /**
     * fills the contact combo box with contact ID's
     */
    @FXML
    void contactHandler(MouseEvent event) {
//        for(Contact contact : requests.contactComboBoxInfo()) {
//            ObservableList<Contact> contacts = requests.contactComboBoxInfo();
//            apptContactComboBox.setItems(contacts);
//            System.out.println();
//        }

        for (Contact contact : contacts) {
            apptContactComboBox.setItems(contacts);
            contactMap(contact);
            System.out.println("This is the contact Map:" + getNameFromMap(Integer.parseInt(String.valueOf(contact.getContactID()))));
        }
    }

    String getSelectedContact(){
        selectedContact = apptContactComboBox.getSelectionModel().getSelectedItem();
        System.out.println(selectedContact);
        return String.valueOf(selectedContact);
    }

    /**
     * takes the selected Appointment and deletes it from the tableview and observable list.
     */
    @FXML
    void deleteApptBtnHandler(ActionEvent event) {
        Appointment selectedAppointment = apptTableView.getSelectionModel().getSelectedItem();
        if (requests.getAppointments().remove(selectedAppointment)) {
            String confirmation = "Are you sure you want to delete this appointment?";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText(confirmation);
            alert.showAndWait();
        }
        apptTableView.setItems(requests.getAppointments());
    }

    /**
     * fills the end time minute combobox with values spaced 15 mins apart
     */
    public void setUpEndMinuteCombo() {
        String minutes;
        for (int i = 0; i < 60; i += 15) {
            if (i < 10) {
                minutes = "0" + i;
                endMinutes.add(Integer.valueOf(minutes));
            }
            else {
                endMinutes.add(i);
            }
            endMinuteComboBox.setItems(endMinutes);
        }
    }

    /**
     * fills the end time hour combobox with values 1 thry 24 to be able to tell AM times from PM times
     */
    public void setUpEndHourCombo() {
        String hours;
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hours = "0" + i;
                endHours.add(Integer.valueOf(hours));
            }
            else {
                endHours.add(i);
            }
            endHourComboBox.setItems(endHours);
        }
    }
    /**
     * Gets the value of the selection of the end time hour combo box
     *
     * @return
     */
    @FXML
    int endHourHandler(ActionEvent event) {
        selectedEndHour = endHourComboBox.getSelectionModel().getSelectedItem();
        System.out.println(selectedEndHour);
        return selectedEndHour;
    }

    /**
     * Gets the value of the selection of the end time minute combo box
     */
    @FXML
    int endMinuteHandler(ActionEvent event) {
        selectedEndMinute = endMinuteComboBox.getSelectionModel().getSelectedItem();
        System.out.println(selectedEndMinute);
        return selectedEndMinute;
    }
/** combines the selected hour and minutes to give a localTime object for the ending time of the appointment*/
    LocalTime combineEndTime() {
        String combinedEndTime = selectedEndHour + ":" + selectedEndMinute;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
       totalEndTime = LocalTime.parse(combinedEndTime, formatter);
        System.out.println(totalEndTime);;
        return totalEndTime;
    }
/**Combines the selected hour and minute to give a localTime object for the starting time of the appointment */
    LocalTime combineStartTime(){
        String combinedStartTime = selectedStartHour + ":" + selectedStartMinute;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        totalStartTime = LocalTime.parse(combinedStartTime, formatter);
        System.out.println(totalStartTime);
        return totalStartTime;
    }

    /**
     * fills the start time hour combobox with values 1 thry 24 to be able to tell AM times from PM times
     */
    public void setUpStartHourCombo() {
        String hours;
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hours = "0" + i;
                startHours.add(Integer.valueOf(hours));
            }
            else {
                startHours.add(i);
            }
            startHourComboBox.setItems(startHours);
        }
    }

    /**
     * fills the start time minute combobox with values spaced 15 mins apart
     */
    public void setUpStartMinuteCombo() {
        String minutes;
        for (int i = 0; i < 60; i += 15) {
            if (i < 10) {
                minutes = "0" + i;
                startMinutes.add(Integer.valueOf(minutes));
            }
            else {
                startMinutes.add(i);
            }
            startMinuteComboBox.setItems(startMinutes);
        }
    }

    /**
     * Gets the value of the selection of the start time hour combo box
     */
    @FXML
    int startHourHandler(ActionEvent event) {
        selectedStartHour = startHourComboBox.getSelectionModel().getSelectedItem();
        System.out.println(selectedStartHour);
        return selectedStartHour;
    }

    /**
     * Gets the value of the selection of the start time minute combo box
     */
    @FXML
    int startMinuteHandler(ActionEvent event) {
        selectedStartMinute = startMinuteComboBox.getSelectionModel().getSelectedItem();
        System.out.println(selectedStartMinute);
        return selectedStartMinute;
    }

    /**
     * gets the value of the start date from the datepicker on the GUI
     */
    @FXML
    void startDatePickerHandler(ActionEvent event) {
        startDate = startDatePicker.getValue();
    }

    /**
     * gets the value of the end date from the datepicker on the GUI
     */
    @FXML
    void endDatePickerHandler(ActionEvent event) {
        endDate = endDatePicker.getValue();
    }

    /**
     * Creates the LocalDateTime startTime value needed for conversions to other timezones or to input info to database
     */
    LocalDateTime finalStartTime() {
        finalStartTime = LocalDateTime.of(startDate, combineStartTime());
        System.out.println("finalStartTime: " + finalStartTime);
        return finalStartTime;
    }

    /**
     * Creates the LocalDateTime endTime value needed for conversions to other timezones or to input info to database
     */
    LocalDateTime finalEndTime() {
        finalEndTime = LocalDateTime.of(endDate, combineEndTime());
        System.out.println("finalEndTime: " + finalEndTime);
        return finalEndTime;
    }

    /**
     * when back button is clicked, program takes user back to the main page and cancels any changes
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
     * Creates columns spaces in the GUI for the appointment table
     */
    private void createColumns() {
        for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
            apptTableView.getColumns().add(columnIndex, setTableColumn(columnIndex + 1));

        }
    }

    /**
     * gets the column names from the database columns
     */
    private ResultSetMetaData getMetaData() {
        try {
            return appointmentsRS.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Count the columns from the info in the database
     */
    private int getColumnCount() {
        try {
            return getMetaData().getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Sets up the column names for the appointment tableview
     */
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
                return new TableColumn<Appointment, LocalDateTime>(name);
            case 92:
                return new TableColumn<Appointment, LocalDateTime>(name);
            case 93:
                return new TableColumn<Appointment, Integer>(name);
            case 94:
                return new TableColumn<Appointment, Integer>(name);
            case 98:
                return new TableColumn<Appointment, Integer>(name);
            default:
                return new TableColumn(name);
        }
    }

    /**
     * sets up the appointment table data in the tableview
     */
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
                    col.setCellValueFactory(new PropertyValueFactory<>("contactID"));
                    break;
            }
        }
        apptTableView.setItems(requests.getAppointments());
    }

    /**
     * creates a mew Appointment object
     */
    public Appointment createAppointment() {
        Appointment appointment = new Appointment();;
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
        appointment.setContactID(Integer.parseInt(apptContactComboBox.getSelectionModel().getSelectedItem().getContactID()));

        appointmentObservableList.add(appointment);
        apptTableView.setItems(appointmentObservableList);
        requests.createNewAppt(appointment);
        return appointment;
    }


    /**
     * edits an existing appointment object from the table
     */
    @FXML
    void editAppointment(MouseEvent event) {
        Appointment appointment = apptTableView.getSelectionModel().getSelectedItem();
        isNewAppointment = false;

        apptTitleText.setText(appointment.getTitle());
        apptDescriptionText.setText(appointment.getDescription());
        apptLocationText.setText(appointment.getLocation());
        apptTypeText.setText(appointment.getApptType());

        apptContactComboBox.setValue((Contact) getNameFromMap(appointment.getContactID()));
        startDatePicker.setValue(LocalDate.from(appointment.getStartDateTime()));
        startHourComboBox.setValue(appointment.getStartDateTime().getHour());
        startMinuteComboBox.setValue(appointment.getStartDateTime().getMinute());
        endDatePicker.setValue(LocalDate.from(appointment.getEndDateTime()));
        endHourComboBox.setValue(appointment.getEndDateTime().getHour());
        endMinuteComboBox.setValue(appointment.getEndDateTime().getMinute());

        customerIDText.setText(String.valueOf(appointment.getCustomerID()));
        userIDText.setText(String.valueOf(appointment.getUserID()));

        requests.updateAppointment(appointment);
    }

    public void contactMap(Contact c){
        contactIDtoNames.put(Integer.valueOf((c.getContactID())), c.getContactName());
    }

    public Object getNameFromMap(int contactID){
        return contactIDtoNames.get(contactID);
    }


    /**
     * first function run to set up the page for viewing
     */
    public void initialize() {
        createColumns();
        setTableData();
        setUpStartHourCombo();
        setUpEndHourCombo();
        setUpStartMinuteCombo();
        setUpEndMinuteCombo();
    }

}