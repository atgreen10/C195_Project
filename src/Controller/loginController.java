package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Main;
import utils.DBQuery;
import utils.requests;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class loginController {
    private String userName;
    private String password;
    private boolean userNameAccepted = false;
    private boolean passwordAccepted = false;

    Stage stage;

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginUsernameInput;

    @FXML
    private PasswordField loginPasswordInput;





 /** Once the login button is pressed it checks the input info and if it clears, the user is taken to the main application page */
    @FXML
    void onClickLoginButton(MouseEvent event) throws SQLException, IOException {
        if(usernameCheck() && passwordCheck()){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/mainMenu.fxml"));
            loader.load();

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Login Error");
            alert.setContentText("There was an error with your username or password");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                alert.close();
            }
        }
    }

    /** Gets the characters entered by the user for the userName field*/
    public String getLoginUsernameInput() {
        if (loginUsernameInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Login Error");
            alert.setContentText("Please enter a username to login.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                userName = null;
                alert.close();
            }
        }
        else {
            userName = loginUsernameInput.getText();
            //System.out.println(userName);

        }
        return userName;
    }

    /** Gets the characters entered by the user for the password field*/
    private String getLoginPasswordInput() {
        if (loginPasswordInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Login Error");
            alert.setContentText("Please enter a password.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                password = null;
                alert.close();
            }
        }
        else {
            password = loginPasswordInput.getText();
            // System.out.println(password);
        }
        return String.valueOf(password);
    }

    /** Checks to make sure the entered userName is in the database */
    public boolean usernameCheck() throws SQLException {
        ResultSet rs = requests.getUserName();
        while (rs.next()) {
            String user_Name = rs.getString("User_Name");
            if ((getLoginUsernameInput().contains(user_Name))) {
                userNameAccepted = true;;
                break;
            } else {
               userNameAccepted = false;
            }
        }
        return userNameAccepted;
    }

    /** Checks to make sure the entered password is in the database */
    public boolean passwordCheck() throws SQLException{
        ResultSet rs = requests.getPassword();

        while(rs.next()){
            String passwordNeeded = rs.getString("Password");
            if(getLoginPasswordInput().contains(passwordNeeded)){
                passwordAccepted = true;
                break;
            }
            else{
                passwordAccepted = false;

            }
        }
        return passwordAccepted;
    }

}
