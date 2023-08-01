package com.example.traveljournal;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class CreateTripController extends Application {

    @FXML
    public TextField tn;

    @FXML
    public DatePicker sd;

    @FXML
    public DatePicker ed;

    @FXML
    public Label incorrect;


    public String user;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }
    public void onSubmit(ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "INSERT INTO Trip (TripName, Username, StartDate, EndDate)" +
                "VALUES ('" + tn.getText() + "', '" + user + "', '" + sd.getValue().toString() + "', '" + ed.getValue().toString()+ "' );";
        try {
            int result = 0;
            boolean changed = false;
            FXMLLoader fxmlLoader = null;
            if (!(tn.getText().equals("") || sd.getValue().toString().equals("") || sd.getValue().toString().equals(""))){
                Statement statement = connectDB.createStatement();
                result = statement.executeUpdate((connectQuery));
                if (result != 0) {
                    changed = true;
                    incorrect.setText("Trip saved!");
                    tn.setText("");
                    sd.getEditor().clear();
                    ed.getEditor().clear();
                }
                if (!changed){
                    incorrect.setText("Please choose a different trip name and check dates");
                }
            } else {
                incorrect.setText("No fields can be empty");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            incorrect.setText("Please choose a different trip name");
        } catch (SQLException e) {
            incorrect.setText("Please check the dates of your trip");
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
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

    public void setInfo(String user) {
        this.user = user;
    }
}
