package com.example.traveljournal;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewTripsController extends Application {

    public String user;
    @FXML
    public TableView tableView;
    @FXML
    public TableColumn col;
    List<String> columnNameList = new ArrayList<>();
    List<String> tableNameList = new ArrayList<>();
    ObservableList<ObservableList> data;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

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
        build();
    }

//    public void clickRow() {
//        TableCell tc = (TableCell) tableView.getSelectionModel().getSelectedCells();
//        System.out.println(tc.getText());
//    }
    public void build() {
        data = FXCollections.observableArrayList();
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();

        String tableQuery = "SELECT TripName FROM TRIP WHERE Username = '" + user + "'ORDER BY TripName";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery((tableQuery));
                for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                    final int j = i;
                    col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                            new SimpleStringProperty(param.getValue().get(j).toString()));
                }

                while (result.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                        row.add(result.getString(i));
                    }
                    data.add(row);
                }
                tableView.setItems(data);
            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void handleRowClick(Event event) throws SQLException {
        try {
            String userlist = tableView.getSelectionModel().getSelectedItem().toString();
            if (userlist != null) {
                FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("MyTripReport.fxml")));
                Scene scene = new Scene(fxmlLoader.load());
                //Stage stage = getCurrentStage(event);
                Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                stage.setScene(scene);
                MyTripReportController controller = fxmlLoader.getController();
                controller.setTrip(userlist.substring(1, userlist.length() - 1));
                controller.setInfo(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void movePage(ActionEvent event) throws SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("MyTripReport.fxml")));
            Scene scene = new Scene(fxmlLoader.load());
            //Stage stage = getCurrentStage(event);
            Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            stage.setScene(scene);
            MyTripReportController controller = fxmlLoader.getController();
            controller.setInfo(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
