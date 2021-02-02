package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class mainMenuController {

    Stage stage;

    @FXML
    private Button customerBtn;

    @FXML
    private Button appointmentBtn;

    @FXML
    private Button reportBtn;

    @FXML
    private Button exitBtn;

    /** Takes user to the appointment page when selected */
    @FXML
    void appointmentBtnHandler(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/appointments.fxml"));
        loader.load();

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();


    }

    /** Takes you to the customer page when the button is clicked */
    @FXML
    void customerBtnHandler(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/customers.fxml"));
        loader.load();

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();


                                                                    }

    /** Exits the application when clicked */
    @FXML
    void exitBtnHandler(MouseEvent event) {
        System.exit(1);

    }

    /** Takes user to the reports page when clicked */
    @FXML
    void reportBtnHandler(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/reports.fxml"));
        loader.load();

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();


    }

}
