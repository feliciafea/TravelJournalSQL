package com.example.traveljournal;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;

public class CreateJEController extends Application {

    public String user;
    @FXML
    public ChoiceBox privacyCB;
    @FXML
    public ChoiceBox ratingCB;
    @FXML
    public TextField cityText;
    @FXML
    public TextField countryText;
    @FXML
    public Text incorrect;
    @FXML
    public DatePicker dateText;
    @FXML
    public TextField noteText;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }


    public void onSubmit(ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        int privacy = 0;
        if (privacyCB != null) {
            if (privacyCB.getValue().toString().equals("Private")) {
                privacy = 0;
            } else {
                privacy = 1;
            }
        }

        String connectQuery;

//        System.out.println(ratingCB.getValue().toString());
//        System.out.println(privacyCB.getValue().toString());
//        System.out.println(privacy);
//        System.out.println(dateText.getValue().toString());
//        System.out.println(countryText.getText());
//        System.out.println(cityText.getText());
//        System.out.println(user);

        try {
            int result = 0;
            FXMLLoader fxmlLoader = null;
            if (!(((this.ratingCB.getValue() == null) && this.noteText.getText().equals("")) || this.cityText.getText().equals("") || this.countryText.getText().equals("") || this.dateText.getValue().toString().equals(""))) {
                if (noteText == null) {
                    connectQuery = "INSERT INTO JOURNAL_ENTRY(Username, EntryDate, Note, Rating, PrivacyLevel, CityName, Country) VALUES('"
                            + this.user + "', '" + this.dateText.getValue().toString() + "', NULL, '" + this.ratingCB.getValue().toString() + "', '" + privacy + "', '" + cityText.getText()
                            + "', '" + countryText.getText() + "' );";
                } else if (ratingCB.getValue() == null) {
                    connectQuery = "INSERT INTO JOURNAL_ENTRY(Username, EntryDate, Note, Rating, PrivacyLevel, CityName, Country) VALUES('"
                            + this.user + "', '" + this.dateText.getValue().toString() + "', '" + this.noteText.getText()
                            + "', NULL, '" + privacy + "', '" + cityText.getText()
                            + "', '" + countryText.getText() + "' );";
                } else {
                    connectQuery = "INSERT INTO JOURNAL_ENTRY(Username, EntryDate, Note, Rating, PrivacyLevel, CityName, Country) VALUES('" + this.user + "', '" + this.dateText.getValue().toString() + "', '" + this.noteText.getText() + "', '" + this.ratingCB.getValue().toString() + "', '" + privacy + "', '" + cityText.getText() + "', '" + countryText.getText() + "' );";
                }
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(connectQuery);
                Stage stage;
                Scene scene;
                fxmlLoader = new FXMLLoader(TJApp.class.getResource("Home.fxml"));
                scene = new Scene((Parent)fxmlLoader.load());
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                HomeController controller = (HomeController)fxmlLoader.getController();
                controller.setInfo(user);

            } else {
                incorrect.setText("Please make sure all required fields are filled out");
            }

        } catch (NullPointerException var12) {
            incorrect.setText("Please make sure all required fields are filled out");
        } catch (SQLIntegrityConstraintViolationException var12) {
            System.out.println(var12);
            incorrect.setText("Cannot have two Journal Entries about same place on same date");
        } catch (Exception e) {
            e.printStackTrace();
        }

        connectDB.close();
    }

    public void back (ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("Home.fxml")));
            Scene scene = new Scene(fxmlLoader.load());
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
        privacyCB.getItems().addAll("Public", "Private");
        ratingCB.getItems().addAll("1", "2", "3", "4", "5");

        //get default account privacy
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT IsPublic FROM ACCOUNT WHERE Username = '" + user + "';";
        ResultSet result = null;
        try {
            Statement statement = connectDB.createStatement();
            result = statement.executeQuery(connectQuery);
            while (result.next()) {
                if (result.getString(1).equals("1")) {
                    privacyCB.setValue("Public");
                } else {
                    privacyCB.setValue("Private");
                }
                ratingCB.setValue(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
