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

public class MyCityEntryController extends Application {

    public String user;
    public String city;
    public String date;

    public String trip;

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
            FXMLLoader fxmlLoader = new FXMLLoader(TJApp.class.getResource("MyTripReport.fxml"));
            Scene scene = new Scene((Parent)fxmlLoader.load());
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            MyTripReportController controller = fxmlLoader.getController();
            controller.setTrip(this.trip);
            controller.setInfo(this.user);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public void delete(ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "DELETE FROM JOURNAL_ENTRY " +
                " WHERE Username = '" + user + "' AND CityName = '" + city + "' AND EntryDate = '" + date + "';";

        try {
            Statement statement = connectDB.createStatement();
            int queryOutput = statement.executeUpdate(connectQuery);
            FXMLLoader fxmlLoader = new FXMLLoader(TJApp.class.getResource("MyTripReport.fxml"));
            Scene scene = new Scene((Parent) fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            MyTripReportController controller = fxmlLoader.getController();
            controller.setTrip(this.trip);
            controller.setInfo(this.user);
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public void setInfo(String user, String city, String date, String trip) {
        this.user = user;
        this.city = city;
        this.date = date;
        this.trip = trip;
        populate();
    }

    private void populate() {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT EntryDate, CityName, Rating, Note FROM JOURNAL_ENTRY " +
        " WHERE Username = '" + user + "' AND CityName = '" + city + "' AND EntryDate = '" + date + "';";

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
