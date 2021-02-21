package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import utils.requests;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class loginController {

    Map<String, User> usersToUserNames = new HashMap<>();
//    List<String> userNames =
    Stage stage;

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginUsernameInput;

    @FXML
    private PasswordField loginPasswordInput;

    public List<User> getAllUsers(){
        List<User> allUsers = requests.getUser();
        return allUsers;
    }


    /**
     * Once the login button is pressed it checks the input info and if it clears, the user is taken to the main application page
     */
    @FXML
    void onClickLoginButton(MouseEvent event) {
        pairUserWithUserName();
 //       System.out.print
    }

    //        if(usernameCheck() && passwordCheck()){
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("/View/mainMenu.fxml"));
//            loader.load();
//
//            loader.getController();
//            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
//            Parent scene = loader.getRoot();
//            stage.setScene(new Scene(scene));
//            stage.show();
//        }
//        else{
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Login Error");
//            alert.setHeaderText("Login Error");
//            alert.setContentText("There was an error with your username or password");
//            Optional<ButtonType> result = alert.showAndWait();
//            if(result.get() == ButtonType.OK){
//                alert.close();
//            }
//        }
//    }
    public void pairUserWithUserName() {
        for(getAllUsers() : 

    }


    /**
     * Gets the characters entered by the user for the userName field
     */
    public String getLoginUsernameInput() {
        String userName = null;
        if (loginUsernameInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Login Error");
            alert.setContentText("Please enter a username to login.");
            Optional<ButtonType> result = alert.showAndWait();
            //
            alert.close();
        } else {
            userName = loginUsernameInput.getText();
  //          User.setUserName(userName);
            //System.out.println(userName);

        }
        return userName;
    }
}

    /**
     * Gets the characters entered by the user for the password field
Dring password = requests.getPassword();
    }
Z} */