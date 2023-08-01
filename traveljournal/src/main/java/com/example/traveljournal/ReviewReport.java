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

public class ReviewReport extends Application {

    public String user;
    public String city;
    public String date;
    public String flaggedUser;
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

    private void populate() {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        System.out.println(user);
        System.out.println(city);
        System.out.println(date);
        String connectQuery = "SELECT EntryDate, CityName, Rating, Note FROM JOURNAL_ENTRY " +
                " WHERE Username = '" + flaggedUser + "' AND CityName = '" + city + "' AND EntryDate = '" + date + "';";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while(queryOutput.next()) {
                this.cityText.setText(queryOutput.getString(2));
                this.dateText.setText(queryOutput.getString(1));
                this.noteText.setText(queryOutput.getString(4) == null ? "NO NOTE" : queryOutput.getString(4));
                this.ratingText.setText(queryOutput.getString(3) == null ? "-- / --" : queryOutput.getString(3) + " / 5");
            }
            connectDB.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public void back(ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TJApp.class.getResource("Admin_Flags_Home_Page.fxml"));
            Scene scene = new Scene((Parent)fxmlLoader.load());
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            AdminHomeController controller = fxmlLoader.getController();
            controller.setInfo(this.user);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public void clearFlag(ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();

        String idQuery = "SELECT EntryID FROM JOURNAL_ENTRY " +
                " WHERE Username = '" + flaggedUser + "' AND CityName = '" + city + "' AND EntryDate = '" + date + "';";

        String id = null;
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(idQuery);
            while (queryOutput.next()) {
                id = queryOutput.getString(1);
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        String connectQuery = "DELETE FROM USER_FLAGS WHERE EntryID = " + id + ";";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);
            FXMLLoader fxmlLoader = new FXMLLoader(TJApp.class.getResource("Admin_Flags_Home_Page.fxml"));
            Scene scene = new Scene((Parent)fxmlLoader.load());
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            AdminHomeController controller = fxmlLoader.getController();
            controller.setInfo(this.user);
            connectDB.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public void deleteEntry(ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();

        String idQuery = "SELECT EntryID FROM JOURNAL_ENTRY " +
                " WHERE Username = '" + flaggedUser + "' AND CityName = '" + city + "' AND EntryDate = '" + date + "';";

        String id = null;
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(idQuery);
            while (queryOutput.next()) {
                id = queryOutput.getString(1);
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        String deleteQuery = "DELETE FROM JOURNAL_ENTRY WHERE EntryID = " + id + ";";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(deleteQuery);

            //clear flag
            String connectQuery = "DELETE FROM USER_FLAGS WHERE EntryID = " + id + ";";

            try {
                statement.executeUpdate(connectQuery);
                FXMLLoader fxmlLoader = new FXMLLoader(TJApp.class.getResource("Admin_Flags_Home_Page.fxml"));
                Scene scene = new Scene((Parent)fxmlLoader.load());
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                AdminHomeController controller = fxmlLoader.getController();
                controller.setInfo(this.user);
                connectDB.close();
            } catch (Exception var6) {
                var6.printStackTrace();
            }

        } catch (Exception var6) {
            var6.printStackTrace();
        }
        connectDB.close();

    }

    public void banUser(ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();

        String banQuery = "UPDATE ACCOUNT SET IsBanned = TRUE WHERE Username = '" + flaggedUser + "';";
        String deleteJEQuery = "DELETE FROM JOURNAL_ENTRY WHERE Username = '" + flaggedUser + "';";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(banQuery);

            //clear flag
            String idQuery = "SELECT EntryID FROM JOURNAL_ENTRY " +
                    " WHERE Username = '" + flaggedUser + "' AND CityName = '" + city + "' AND EntryDate = '" + date + "';";

            String id = null;
            try {
                ResultSet queryOutput = statement.executeQuery(idQuery);
                while (queryOutput.next()) {
                    id = queryOutput.getString(1);
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            String removeFlagQuery = "DELETE FROM USER_FLAGS WHERE EntryID = " + id + ";";

            try {
                statement.executeUpdate(removeFlagQuery);
                statement.executeUpdate(deleteJEQuery);
                FXMLLoader fxmlLoader = new FXMLLoader(TJApp.class.getResource("Admin_Flags_Home_Page.fxml"));
                Scene scene = new Scene((Parent)fxmlLoader.load());
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                AdminHomeController controller = fxmlLoader.getController();
                controller.setInfo(this.user);
                connectDB.close();
            } catch (Exception var6) {
                var6.printStackTrace();
            }

        } catch (Exception var6) {
            var6.printStackTrace();
        }
        connectDB.close();

    }

    public void setInfo(String user, String city, String date, String flaggedUser) {
        this.user = user;
        this.city = city;
        this.date = date;
        this.flaggedUser = flaggedUser;
        populate();
    }
}
