package com.example.traveljournal;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MyTripReportController extends Application {

    public String user;
    public String trip = null;
    @FXML
    public TableView tableView;

    @FXML
    public TableColumn dateCol;
    @FXML
    public TableColumn cityCol;
    @FXML
    public TableColumn countryCol;
    @FXML
    public TableColumn ratingCol;
    @FXML
    public TableColumn noteCol;
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

    public void cityEntry (Event event) throws SQLException {
        try {
            String userlist = tableView.getSelectionModel().getSelectedItem().toString();
            if (userlist != null) {
                String city = userlist.substring(1, userlist.length() - 1).split(", ")[1];
                String date = userlist.substring(1, userlist.length() - 1).split(", ")[0];
                FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("MyCityEntryScene.fxml")));
                Scene scene = new Scene(fxmlLoader.load());
                //Stage stage = getCurrentStage(event);
                Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                stage.setScene(scene);
                MyCityEntryController controller = fxmlLoader.getController();
                controller.setInfo(user, city, date, trip);
            }
        } catch (NullPointerException e) {
//            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void build() {
        data = FXCollections.observableArrayList();
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String tableQuery = "";

        if (trip == null) {
            tableQuery = "SELECT EntryDate, CityName, Country, Rating, Note FROM JOURNAL_ENTRY WHERE Username = '" + user + "' ORDER BY EntryDate";
        } else {
            tableQuery = "SELECT EntryDate, CityName, Country, Rating, Note FROM JOURNAL_ENTRY WHERE Username = '" + user + "' AND EntryDate BETWEEN (SELECT StartDate" +
                    " FROM TRIP WHERE TripName = '" + trip + "' AND Username = '" + user + "') AND " +
                    " (SELECT EndDate FROM TRIP WHERE TripName = '"+ trip +"' AND Username = '"+user+"') " +
                    " ORDER BY EntryDate";
        }

        try {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery((tableQuery));
            for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col;
                if (i == 0) {
                    col = new TableColumn("Date");
                } else if (i == 1) {
                    col = new TableColumn("City");
                } else {
                    col = new TableColumn(result.getMetaData().getColumnName(i + 1));
                }
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                        new SimpleStringProperty(param.getValue().get(j).toString()));
                tableView.getColumns().addAll(col);
            }

            while (result.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                    String fixed = result.getString(i) == null ? "NONE" : result.getString(i);
                    row.add(fixed);
                }
                data.add(row);
            }
            tableView.setItems(data);
            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setInfo(String user) {
        this.user = user;
        build();
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }
}
