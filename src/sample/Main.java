package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBQuery;
import utils.requests;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {
    /** creates connection object */
    public static Connection conn;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View/login.fxml"));
        primaryStage.setTitle("Scheduling Application");
        primaryStage.setScene(new Scene(root, 455, 303));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {

        /**Starts connection to database */
        conn = DBConnection.startConnection();

        /**Gets statement reference */
        Statement SQL = DBQuery.getStatement();


        launch(args);

        /** Program does not close until the application is closed by the user, after all the components of launch(args) has already run */
        DBConnection.closeConnection();
    }
}
