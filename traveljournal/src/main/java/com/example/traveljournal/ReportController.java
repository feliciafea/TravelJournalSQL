package com.example.traveljournal;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportController extends Application {

    public String user;
    public String entryUser;
    public String entryID;
    public String entryCountry;


    public String entryCity;

    public String entryDate;

    @FXML
    public CheckBox harassButton;

    @FXML
    public CheckBox elButton;

    @FXML
    public CheckBox otButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }

    public void setInfo(String user, String entryID, String entryCity, String entryDate, String entryCountry) {
        this.user = user;
        this.entryCity = entryCity;
        this.entryID = entryID;
        this.entryCountry = entryCountry;
        this.entryDate = entryDate;
    }

    public void back (ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TJApp.class.getResource("ViewCityEntryScene.fxml"));
            Scene scene = new Scene((Parent) fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            ViewCityEntryController controller = fxmlLoader.getController();
            controller.setEntryID(entryID, entryCity, entryCountry, user);
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public void report (ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
//        String entryIDQuery = "(SELECT EntryID FROM JOURNAL_ENTRY WHERE " +
//        " Username = '" + entryUser + "' AND EntryDate = '" + entryDate + "' AND CityName = '" + entryCity + "');";
//        String entryID = "";
//        try {
//            Statement statement = connectDB.createStatement();
//            ResultSet queryOutput = statement.executeQuery(entryIDQuery);
//            while (queryOutput.next()) {
//                entryID = queryOutput.getString(1);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



        String connectQuery = "INSERT INTO USER_FLAGS VALUES ('" + user + "','" + entryID + "' );";
        String harassQuery = null;
        String elQuery = null;
        String otQuery = null;

        if (harassButton.isSelected()) {
            harassQuery = "INSERT INTO FLAG_REASON VALUES ('" + user + "', '" + entryID + "', 'Harassment');";
        }

        if (elButton.isSelected()) {
            elQuery = "INSERT INTO FLAG_REASON VALUES ('" + user + "', '" + entryID + "', 'Explicit Language');";
        }

        if (otButton.isSelected()) {
            otQuery = "INSERT INTO FLAG_REASON VALUES ('" + user + "', '" + entryID + "', 'Off Topic');";
        }

        try {
            Statement statement = connectDB.createStatement();
            int queryOutput = statement.executeUpdate(connectQuery);
            if(harassQuery != null) {
                int hqueryOutput = statement.executeUpdate(harassQuery);
            }
            if(elQuery != null) {
                int elqueryOutput = statement.executeUpdate(elQuery);
            }
            if(otQuery != null) {
                int otqueryOutput = statement.executeUpdate(otQuery);
            }
            FXMLLoader fxmlLoader = new FXMLLoader(TJApp.class.getResource("ViewCityEntryScene.fxml"));
            Scene scene = new Scene((Parent) fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            ViewCityEntryController controller = fxmlLoader.getController();
            controller.setEntryID(entryID, entryCity, entryCountry, entryDate);
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

}
