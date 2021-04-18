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

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;

public class appointmentsController {

    @FXML
    /** Connection to Database should actually be closed once the SQL query is completed, this is not good practice. */
    private final ResultSet appointmentsRS = requests.getAppointmentList();
    @FXML
    private final ResultSetMetaData metaData = appointmentsRS.getMetaData();


    private final ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();
    ObservableList<String> startHours = FXCollections.observableArrayList();
    ObservableList<String> startMinutes = FXCollections.observableArrayList();
    ObservableList<String> endHours = FXCollections.observableArrayList();
    ObservableList<String> endMinutes = FXCollections.observableArrayList();
    ObservableList<Contact> contacts = requests.contactComboBoxInfo();
    Map<Integer, String> contactIDtoNames = new HashMap<>();


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
    LocalTime startTime;
    LocalTime endTime;
    String combinedEndTime;
    String combinedStartTime;
    boolean withinBusinessHours;

    LocalTime local = LocalTime.now();
    ZoneId zoneEST = ZoneId.of("America/New_York");

    LocalTime businessOpening = LocalTime.of(8, 00, 00);
    LocalTime businessClosing = LocalTime.of(22, 00, 00);
    ZonedDateTime finalStartEST;
    ZonedDateTime finalEndEST;
    ZonedDateTime finalStartLocal;

    DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
    ZonedDateTime finalStart;
    ZonedDateTime finalEnd;

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

    @FXML
    private RadioButton weekView;

    @FXML
    private ToggleGroup viewReport;

    @FXML
    private RadioButton monthView;

    private boolean isNewAppointment;
    public appointmentsController() throws SQLException {
    }


    /**
     * submits a new appointment to the table and database to be saved and have the GUI reflect the changes.
     */
    @FXML
    void bookAptBtnHandler(ActionEvent event) throws IOException {
    if(isNewAppointment) {
        createAppointment();
    }
    else{

    }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/mainMenu.fxml"));
        loader.load();

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
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

    /**
     * takes the selected Appointment and deletes it from the tableview and observable list.
     * @param event
     */
    @FXML
    void deleteApptBtnHandler(MouseEvent event) {
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
                endMinutes.add(minutes);
            } else {
                endMinutes.add(String.valueOf(i));
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
                endHours.add(hours);
            } else {
                endHours.add(String.valueOf(i));
            }
            endHourComboBox.setItems(endHours);
        }
    }
    /**
     * fills the start time hour combobox with values 1 thry 24 to be able to tell AM times from PM times
     */
    public void setUpStartHourCombo() {
        String hours;
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hours = "0" + i;
                startHours.add(hours);
            } else {
                startHours.add(String.valueOf(i));
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
                startMinutes.add(minutes);
            } else {
                startMinutes.add(String.valueOf(i));
            }
            startMinuteComboBox.setItems(startMinutes);
        }
    }

    /** gets the start date selection from the combo box */
    public LocalDate getStartDate(){
        startDate = startDatePicker.getValue();
        System.out.println("startDate returns: " + startDate);
        return startDate;
    }

//    /** Gets the hour selection from the combo box */
//    public String getStartHour(){
//        String startHour = startHourComboBox.getValue();
//        System.out.println("This is the startHour: " + startHour);
//        return startHour;
//    }
//
//    /** Gets the minute selection from the combo box */
//    public String getStartMin(){
//        String startMin = startMinuteComboBox.getValue();
//        System.out.println("This is the startMin: " + startMin);
//        return startMin;
//    }

    /** Converts the time selection into a LocalTime variable */
    public LocalTime getStartTime(){
        String combinedStartTime = startHourComboBox.getValue() + ":" + startMinuteComboBox.getValue();
        startTime = LocalTime.parse(combinedStartTime, formatTime);
        System.out.println("getApptStartTime function returns: " + startTime);
        return startTime;
    }

    /** Converts the time and date selections into a LocalDateTime variable */
    public LocalDateTime getStartDateTime(){
        LocalDateTime startDateTime = LocalDateTime.of(getStartDate(), getStartTime());
        System.out.println("getStartDateTime returns: " + startDateTime);
        return startDateTime;
    }

    ZonedDateTime finalLocalStartTime() {
        finalStartLocal = ZonedDateTime.of(getStartDateTime(), localZoneId);
        System.out.println("finalStartLocal variable value: " + finalStartLocal);
        return finalStartLocal;
    }

    public ZonedDateTime finalStartTimeUTC() {
        finalStart = finalLocalStartTime().withZoneSameInstant(UTC);
        System.out.println("startTime IN UTC: " + finalStart);
        return finalStart;
    }

    public ZonedDateTime finalStartTimeEST() {
        finalStartEST = finalStartTimeUTC().withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println("final start time in EST: " + finalStartEST);
        return finalStartEST;
    }

    /** gets the end date selection from the combo box */
    public LocalDate getEndDate() {
        if (endDatePicker.getValue().isAfter(startDatePicker.getValue())) {
            endDate = endDatePicker.getValue();
        } else {
            String error = "Invalid date. The end date must be after the start date.";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Date Selection");
            alert.setHeaderText("Invalid Date");
            alert.setContentText(error);
            alert.showAndWait();
            endDate = null;
        }
        return endDate;
    }

//    /** Gets the end hour selection from the combo box */
//    public int getEndHour(){
//        int endHour = Integer.parseInt(endHourComboBox.getValue());
//        System.out.println("This is the endHour: " + endHour);
//        return endHour;
//    }
//
//    /** Gets the end minute selection from the combo box */
//    public int getEndMin(){
//        int endMin = Integer.parseInt(endMinuteComboBox.getValue());
//        System.out.println("This is the endMin: " + endMin);
//        return endMin;
//    }

    /** Converts the time selection into a LocalTime variable */
    public LocalTime getEndTime(){
        String combinedEndTime = (endHourComboBox.getValue() + ":" + endMinuteComboBox.getValue());
        endTime = LocalTime.parse(combinedEndTime, formatTime);
        System.out.println("getApptEndTime function returns: " + endTime);
        return endTime;
    }

    /** Converts the time and date selections into a LocalDateTime variable */
    public LocalDateTime endDateTime(){
        LocalDateTime endDateTime = LocalDateTime.of(getEndDate(), getEndTime());
        System.out.println("endDateTime returns: " + endDateTime);
        return endDateTime;
    }

    ZonedDateTime finalLocalEndTime() {
        ZonedDateTime finalLocalEnd = ZonedDateTime.of(endDateTime(), localZoneId);
        System.out.println("finalLocalEndTime variable: " + finalLocalEnd);
        return finalLocalEnd;
    }

    public ZonedDateTime finalEndTimeUTC() {
        finalEnd = finalLocalEndTime().withZoneSameInstant(UTC);
        System.out.println("EndTime IN UTC: " + finalEnd);
        return finalEnd;
    }

    public ZonedDateTime finalEndTimeEST() {
        finalEndEST = finalEndTimeUTC().withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println("final end time in EST: " + finalEndEST);
        return finalEndEST;
    }

    public boolean withinBusinessHours() {
        withinBusinessHours = !finalStartTimeEST().toLocalTime().isBefore(businessOpening) && !finalEndTimeEST().toLocalTime().isAfter(businessClosing);
        return withinBusinessHours;
    }



    //beginning of edits to the variables dealing with time.

//    /**
//     * Gets the value of the selection of the end time hour combo box
//     *
//     * @return
//     */
//    @FXML
//    String endHourHandler(ActionEvent event) {
//        selectedEndHour = endHourComboBox.getSelectionModel().getSelectedItem();
//        return selectedEndHour;
//    }
//
//    /**
//     * Gets the value of the selection of the end time minute combo box
//     */
//    @FXML
//    String endMinuteHandler(ActionEvent event) {
//        selectedEndMinute = endMinuteComboBox.getSelectionModel().getSelectedItem();
//        return selectedEndMinute;
//    }
//
//    /**
//     * combines the selected hour and minutes to give a localTime object for the ending time of the appointment
//     */
//    LocalTime combineEndTime() {
//        combinedEndTime = selectedEndHour + ":" + selectedEndMinute;
//        totalEndTime = parseTime(combinedEndTime);
//        System.out.println("TotalEndTime: " + totalEndTime);
//        return totalEndTime;
//    }
//
//    /**
//     * Combines the selected hour and minute to give a localTime object for the starting time of the appointment
//     */
//    LocalTime combineStartTime() {
//        combinedStartTime = selectedStartHour + ":" + selectedStartMinute;
//        totalStartTime = parseTime(combinedStartTime);
//        System.out.println("TotalStartTime: " + totalStartTime);
//        return totalStartTime;
//    }
//
//    LocalTime parseTime(String inputTime){
//        return LocalTime.parse(inputTime, formatTime);
//    }

//    /**
//     * Gets the value of the selection of the start time hour combo box
//     */
//    @FXML
//    String startHourHandler(ActionEvent event) {
//        selectedStartHour = startHourComboBox.getSelectionModel().getSelectedItem();
//        return selectedStartHour;
//    }
//
//    /**
//     * Gets the value of the selection of the start time minute combo box
//     */
//    @FXML
//    String startMinuteHandler(ActionEvent event) {
//        selectedStartMinute = startMinuteComboBox.getSelectionModel().getSelectedItem();
//        return selectedStartMinute;
//    }
//
//    /**
//     * gets the value of the start date from the datepicker on the GUI
//     */
//    @FXML
//    void startDatePickerHandler(ActionEvent event) {
//        startDate = startDatePicker.getValue();
//    }
//
//    /**
//     * gets the value of the end date from the datepicker on the GUI
//     */
//    @FXML
//    void endDatePickerHandler(ActionEvent event) {
//      public ZonedDateTime finalStartTimeEST() {
////        finalStartEST = finalStartTimeUTC().withZoneSameInstant(ZoneId.of("America/New_York"));
////        System.out.println("final start time in EST: " + finalStartEST);
////        return finalStartEST;
////    }
//
//    /**
//     * Creates the LocalDateTime startTime value needed for conversions to other timezones or to input info to database
//     */
//    LocalDateTime finalStartTime() {
//        startTime = LocalDateTime.of(startDate, combineStartTime());
//        System.out.println("in localDateTime format the startTime is: " + startTime);
//        return startTime;
//    }
//
//    ZonedDateTime finalLocalStartTime() {
//        finalStartLocal = ZonedDateTime.of(startTime, localZoneId);
//        System.out.println("finalStartLocal variable value: " + finalStartLocal);
//        return finalStartLocal;
//    }
//
//    /**
//     * Converts startingTime to UTC
//     */
//    public ZonedDateTime finalStartTimeUTC() {
//        finalStart = finalLocalStartTime().withZoneSameInstant(UTC);
//        System.out.println("startTime IN UTC: " + finalStart);
//        return finalStart;
//    }
//
//    /**
//     * Converts startingTime to EST
//     */
//    public ZonedDateTime finalStartTimeEST() {
//        finalStartEST = finalStartTimeUTC().withZoneSameInstant(ZoneId.of("America/New_York"));
//        System.out.println("final start time in EST: " + finalStartEST);
//        return finalStartEST;
//    }
//
//    public boolean withinBusinessHours() {
//        withinBusinessHours = !finalStartTimeEST().toLocalTime().isBefore(businessOpening) && !finalEndTimeEST().toLocalTime().isAfter(businessClosing);
//        return withinBusinessHours;
//    }
//
//    /**
//     * Creates the LocalDateTime endTime value needed for conversions to other timezones or to input info to database
//     */
//    LocalDateTime endTime() {
//      endTime = LocalDateTime.of(endDate, combineEndTime());
//        return endTime;
//    }
//
//    ZonedDateTime finalLocalEndTime() {
//        ZonedDateTime finalLocalEnd = ZonedDateTime.of(endTime, localZoneId);
//        System.out.println("finalLocalEndTime variable: " + finalLocalEnd);
//        return finalLocalEnd;
//    }
//
//    public ZonedDateTime finalEndTimeUTC() {
//        finalEnd = finalLocalEndTime().withZoneSameInstant(UTC);
//        System.out.println("EndTime IN UTC: " + finalEnd);
//        return finalEnd;
//    }
//
//    public ZonedDateTime finalEndTimeEST() {
//        finalEndEST = finalEndTimeUTC().withZoneSameInstant(ZoneId.of("America/New_York"));
//        System.out.println("final end time in EST: " + finalEndEST);
//        return finalEndEST;
//    }

    /**
     * when back button is clicked, program takes user back to the main page and cancels any changes
     * @param event
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
        if (type == 1) {
            return new TableColumn<Appointment, Integer>(name);
        }
        return new TableColumn(name);
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
    public void createAppointment() {
        if (withinBusinessHours()) {
            Appointment newAppointment = new Appointment();
            newAppointment.setTitle(apptTitleText.getText());
            newAppointment.setDescription(apptDescriptionText.getText());
            newAppointment.setLocation(apptLocationText.getText());
            newAppointment.setApptType(apptTypeText.getText());
//            newAppointment.setApptStartDate(getStartDate());
//            newAppointment.setApptStartTime(getStartTime());
//            newAppointment.setApptEndDate(getEndDate());
//            newAppointment.setApptEndTime(getEndTime());
            newAppointment.setStartDateTime(LocalDateTime.from(finalStartTimeUTC()));
            newAppointment.setEndDateTime(LocalDateTime.from(finalEndTimeUTC()));
            newAppointment.setCustomerID(Integer.parseInt(customerIDText.getText()));
            newAppointment.setUserID(Integer.parseInt(userIDText.getText()));
            newAppointment.setContactID(Integer.parseInt(apptContactComboBox.getSelectionModel().getSelectedItem().getContactID()));

            if (requests.getStartTime().contains(newAppointment.getStartDateTime().toLocalTime()) || requests.getEndTime().contains(newAppointment.getEndDateTime().toLocalTime())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Appointment overlap");
                alert.setHeaderText("Appointment overlap");
                alert.setContentText("Appointment times cannot overlap");
                alert.showAndWait();
            }
            else{
                appointmentObservableList.add(newAppointment);
                apptTableView.setItems(appointmentObservableList);
                requests.createNewAppt(newAppointment);
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please select a time that is within business hours.");
            alert.setHeaderText("Please select a time that is within business hours.");
            alert.setContentText("Please select a time that is within business hours.");
            alert.showAndWait();
        }
    }


    /**
     * edits an existing appointment object from the table
     */
    @FXML
    void setSelectedAppointment() throws IOException {
        Appointment appointment = apptTableView.getSelectionModel().getSelectedItem();
        if (appointment == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Appointment Selected");
            alert.setHeaderText("Please select an appointment to edit.");
            alert.setContentText("Please select an appointment to edit.");
            alert.showAndWait();
            } else {
            appointment = requests.getSelectedAppointments(appointment);

            apptTitleText.setText(appointment.getTitle());
            apptDescriptionText.setText(appointment.getDescription());
            apptLocationText.setText(appointment.getLocation());

            apptTypeText.setText(appointment.getApptType());
            apptContactComboBox.setValue((Contact) getNameFromMap(appointment.getContactID()));
            startDatePicker.setValue(appointment.getStartDateTime().toLocalDate());
            startHourComboBox.setValue(String.valueOf(appointment.getStartDateTime().getHour()));
            startMinuteComboBox.setValue(String.valueOf(appointment.getStartDateTime().getMinute()));
            endDatePicker.setValue(appointment.getEndDateTime().toLocalDate());
            endHourComboBox.setValue(String.valueOf(appointment.getEndDateTime().getHour()));
            endMinuteComboBox.setValue(String.valueOf(appointment.getEndDateTime().getMinute()));
            customerIDText.setText(String.valueOf(appointment.getCustomerID()));
            userIDText.setText(String.valueOf(appointment.getUserID()));

        }
            requests.updateAppointment(appointment);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/mainMenu.fxml"));
            loader.load();

            Parent scene = loader.getRoot();
            stage.setScene((new Scene(scene)));
            stage.show();
    }

    /**
     * creates map of contact names and contact Id's
     */
    public void contactMap(Contact c) {
        contactIDtoNames.put(Integer.valueOf((c.getContactID())), c.getContactName());
    }

    /**
     * allows you to select a contact Name when using the contact ID to refer to the contact.
     */
    public Object getNameFromMap(int contactID) {
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