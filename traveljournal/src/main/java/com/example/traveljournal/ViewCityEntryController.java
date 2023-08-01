package com.example.traveljournal;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewCityEntryController extends Application {

    public String entryUser;
    public String user;
    public String country;

    public String entryID = null;


    public String city;
    public String date;

//    public String trip;

    @FXML
    public Text cityText;
    @FXML
    public Text ratingText;
    @FXML
    public Text dateText;
    @FXML
    public Text noteText;


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
    }

    public void back(ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TJApp.class.getResource("CityEntriesScene.fxml"));
            Scene scene = new Scene((Parent)fxmlLoader.load());
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            CityEntriesController controller = fxmlLoader.getController();
            controller.setCity(city, country, user);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public void report(ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TJApp.class.getResource("ReportScene.fxml"));
            Scene scene = new Scene((Parent) fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            ReportController controller = fxmlLoader.getController();
            controller.setInfo(user, entryID, city, date, country);
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public void setEntryID(String entryID, String city, String country, String user) {
        this.entryID = entryID;
        this.city = city;
        this.country = country;
        this.user = user;
        populate();
    }

//    public void setInfo(String user, String city, String date, String entryUser) {
//        this.user = user;
//        this.city = city;
//        this.date = date;
//        this.entryUser = entryUser;
//        populate();
//    }

    private void populate() {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "";
        if (entryID == null) {
             connectQuery = "SELECT EntryDate, CityName, Rating, Note FROM JOURNAL_ENTRY " +
                    " WHERE Username = '" + user + "' AND CityName = '" + city + "' AND EntryDate = '" + date + "';";
        } else {
             connectQuery = "SELECT EntryDate, CityName, Rating, Note FROM JOURNAL_ENTRY " +
                    " WHERE EntryID = '" + entryID + "';";
        }

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while(queryOutput.next()) {
                this.cityText.setText(queryOutput.getString(2));
                this.dateText.setText(queryOutput.getString(1));
                this.noteText.setText(queryOutput.getString(4) == null ? "NO NOTE" : queryOutput.getString(4));
                this.ratingText.setText(queryOutput.getString(3) == null ? "-- / --" : queryOutput.getString(3) + " / 5");
//                if (queryOutput.getString(5).equals("1")) {
//                    this.ispublic.setSelected(true);
//                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }
}