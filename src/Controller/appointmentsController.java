package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.time.LocalDateTime;

public class appointmentsController {

    Stage stage;

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
    private ComboBox<LocalDateTime> startTimeComboBox;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<LocalDateTime> endTimeComboBox;

    @FXML
    private TextField customerIDText;

    @FXML
    private TextField userIDText;

    @FXML
    private Button bookAptBtn;

    @FXML
    private Button deleteApptBtn;

    @FXML
    void bookAptBtnHandler(MouseEvent event) {

    }

    @FXML
    void contactHandler(ActionEvent event) {

    }

    @FXML
    void deleteApptBtnHandler(MouseEvent event) {

    }

    @FXML
    void endTimeHandler(ActionEvent event) {

    }

    @FXML
    void startTimeHandler(ActionEvent event) {

    }

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

}