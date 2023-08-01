package com.example.traveljournal;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class SettingsController extends Application {

    @FXML
    public TextField fn;

    @FXML
    public Label msg;
    @FXML
    public TextField ln;
    @FXML
    public TextField email;
    @FXML
    public TextField password;
    @FXML
    public CheckBox ispublic;
    public String user;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }

    public void updateSelected (ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String publicity = ispublic.isSelected() ? "1" : "0";
        if (fn.getText().equals("") || ln.getText().equals("") || email.getText().equals("") || password.getText().equals("")) {
            msg.setText("No entries can be empty");
        } else {
            String connectQuery = "UPDATE ACCOUNT SET Firstname = '" + fn.getText() + "', LastName = '" + ln.getText() + "', Email = '" + email.getText() + "', UserPassword = '" + password.getText() + "', IsPublic = " + publicity + " WHERE Username= '" + user + "';";
            try {
                Statement statement = connectDB.createStatement();
                int queryOutput = statement.executeUpdate((connectQuery));
                msg.setText("Updates saved!");
            } catch (SQLIntegrityConstraintViolationException e) {
                msg.setText("Please choose a different email");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void back (ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("Home.fxml")));
            Scene scene = new Scene(fxmlLoader.load());
            //Stage stage = getCurrentStage(event);
            Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            stage.setScene(scene);
            HomeController controller = fxmlLoader.getController();
            controller.setInfo(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void delete (ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "DELETE FROM ACCOUNT WHERE Username= '" + user + "';";
        user = null;
        try {
            Statement statement = connectDB.createStatement();
            int queryOutput = statement.executeUpdate((connectQuery));
            FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("Login.fxml")));
            Scene scene = new Scene(fxmlLoader.load());
            //Stage stage = getCurrentStage(event);
            Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            stage.setScene(scene);
            LoginController controller = fxmlLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populate() {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT FirstName, LastName, Email, UserPassword, isPublic FROM ACCOUNT WHERE Username = '" + user +"';";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery((connectQuery));
            while (queryOutput.next()) {
                fn.setText(queryOutput.getString(1));
                ln.setText(queryOutput.getString(2));
                email.setText(queryOutput.getString(3));
                password.setText(queryOutput.getString(4));
                if(queryOutput.getString(5).equals("1")) {
                    ispublic.setSelected(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setInfo(String user) {
        this.user = user;
        populate();
    }
}
