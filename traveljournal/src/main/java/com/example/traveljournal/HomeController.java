package com.example.traveljournal;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class HomeController extends Application {

    public String user;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }

    public void clickSettings (ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("Settings.fxml")));
            Scene scene = new Scene(fxmlLoader.load());
            //Stage stage = getCurrentStage(event);
            Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            stage.setScene(scene);
            SettingsController controller = fxmlLoader.getController();
            controller.setInfo(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveToCreateTrip (ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("CreateTrip.fxml")));
            Scene scene = new Scene(fxmlLoader.load());
            //Stage stage = getCurrentStage(event);
            Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            stage.setScene(scene);
            CreateTripController controller = fxmlLoader.getController();
            controller.setInfo(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewTrips (ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("ViewMyTrips.fxml")));
            Scene scene = new Scene(fxmlLoader.load());
            //Stage stage = getCurrentStage(event);
            Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            stage.setScene(scene);
            ViewTripsController controller = fxmlLoader.getController();
            controller.setInfo(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchCities (ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("SearchForCity.fxml")));
            Scene scene = new Scene(fxmlLoader.load());
            //Stage stage = getCurrentStage(event);
            Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            stage.setScene(scene);
            SearchCityController controller = fxmlLoader.getController();
            controller.setInfo(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createJE (ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("CreateJEScene.fxml")));
            Scene scene = new Scene(fxmlLoader.load());
            //Stage stage = getCurrentStage(event);
            Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            stage.setScene(scene);
            CreateJEController controller = fxmlLoader.getController();
            controller.setInfo(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logOut (ActionEvent event) throws SQLException {
        try {
            user = null;
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

    public void setInfo(String user) {
        this.user = user;
    }
}
