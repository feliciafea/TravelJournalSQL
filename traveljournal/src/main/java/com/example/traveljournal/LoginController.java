package com.example.traveljournal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {
    @FXML
    public TextField user;

    @FXML
    public TextField pass;

    @FXML
    public Label incorrect;

    private String currUN;
    private String currPass;


    public void loginAction (ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT IsUser, IsBanned FROM ACCOUNT WHERE Username = '" + user.getText() + "' AND UserPassword = '" + pass.getText() + "';";

        currUN = user.getText();

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery((connectQuery));
            boolean executed = false;
            while (queryOutput.next()) {
                FXMLLoader fxmlLoader = null;

                if (queryOutput.getString(1).equals("1") && queryOutput.getString(2).equals("0")) {
                    executed = true;
                    fxmlLoader = new FXMLLoader((TJApp.class.getResource("Home.fxml")));
                    Scene scene = new Scene(fxmlLoader.load());
                    //Stage stage = getCurrentStage(event);
                    Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                    stage.setScene(scene);
                    HomeController controller = fxmlLoader.getController();
                    controller.setInfo(currUN);
                } else if (queryOutput.getString(1).equals("0")) {
                    executed = true;
                    fxmlLoader = new FXMLLoader((TJApp.class.getResource("Admin_Flags_Home_Page.fxml")));
                    Scene scene = new Scene(fxmlLoader.load());
                    //Stage stage = getCurrentStage(event);
                    Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                    stage.setScene(scene);
                    AdminHomeController controller = fxmlLoader.getController();
                    controller.setInfo(currUN);
                } else if(queryOutput.getString(2).equals("1")){
                    executed = true;
                    incorrect.setText("This user has been banned");
                }
            }

            if(!executed) {
                incorrect.setText("Incorrect Login Info");
            }
            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAccButton (ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("CreateAccount.fxml")));
            Scene scene = new Scene(fxmlLoader.load());
            //Stage stage = getCurrentStage(event);
            Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}














