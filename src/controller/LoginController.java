package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.requests;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    Stage stage;
    String userName;

    @FXML
    private Button loginButton;

    @FXML
    private TextField userNameInput;

    @FXML
    private PasswordField loginPasswordInput;

    /**
     * Once the login button is pressed it checks the input info and if it clears, the user is taken to the main application page
     */
    @FXML
    void onClickLoginButton(ActionEvent event) throws IOException {
        if (validateLogin()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/MainMenu.fxml"));
            loader.getController();
            loader.load();

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Login Error");
            alert.setContentText("There was an error with your username or password");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                alert.close();

            }
        }
    }

    /**
     * Gets the characters entered by the user for the userName field
     */
    public String getUserName(){
        String userName = null;
        if (userNameInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Login Error");
            alert.setContentText("Please enter a username to login.");
            Optional<ButtonType> result = alert.showAndWait();
            //
            alert.close();
        } else {
            userName = userNameInput.getText();
            //User.setUserName(userName);

        }
        return userName;
    }
    
    public String getPasswordInput() {
        String password = null;
        if(loginPasswordInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("login Error");
            alert.setHeaderText("login Error");
            alert.setContentText("Please enter a password to login.");
            Optional<ButtonType> result = alert.showAndWait();
            alert.close();
        } else {
            password = loginPasswordInput.getText();
        }
        return password;
    }


    public boolean validateLogin() {
        boolean validLogin;
        if (requests.validLogin(getUserName()).equals(getPasswordInput())){
            validLogin = true;
        } else {
            validLogin = false;
        }
        return validLogin;
}
}
    /**
     * Gets the characters entered by the user for the password field 
     */
    