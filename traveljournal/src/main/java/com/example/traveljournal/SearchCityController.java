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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SearchCityController extends Application {

    public String user;
    ObservableList<ObservableList> data;

    @FXML
    public TableView tableView;
    @FXML
    public TextField search;

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

    public void build() {
        tableView.getColumns().clear();
        data = FXCollections.observableArrayList();
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();

        String tableQuery = "SELECT CityName AS 'City', Country, AVG(Rating) AS 'Average Rating' FROM JOURNAL_ENTRY WHERE PrivacyLevel = TRUE GROUP BY CityName, Country;";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery((tableQuery));
            tableView.getItems().clear();
            for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(result.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                        new SimpleStringProperty(param.getValue().get(j).toString()));
                tableView.getColumns().addAll(col);
            }

            while (result.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                    if (result.getString(i) == null) {
                        row.add("NONE");
                    } else {
                        row.add(result.getString(i));
                    }
                }
                data.add(row);
            }
            tableView.setItems(data);
            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allEntries (Event event) throws SQLException {
        try {
            String userlist = tableView.getSelectionModel().getSelectedItem().toString();
            if (userlist != null) {
                String city = userlist.substring(1, userlist.length() - 1).split(", ")[0];
                String country = userlist.substring(1, userlist.length() - 1).split(", ")[1];
                FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("CityEntriesScene.fxml")));
                Scene scene = new Scene(fxmlLoader.load());
                //Stage stage = getCurrentStage(event);
                Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                stage.setScene(scene);
                CityEntriesController controller = fxmlLoader.getController();
                controller.setCity(city, country, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchCity (ActionEvent event) throws SQLException {
        tableView.getColumns().clear();
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String tableQuery = "SELECT CityName AS 'City', Country, AVG(Rating) AS 'Average Rating' FROM JOURNAL_ENTRY WHERE PrivacyLevel = TRUE AND CityName = '" + search.getText() + "'GROUP BY CityName, Country;";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery((tableQuery));
            tableView.getItems().clear();
            for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col;
                if (i == 0) {
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
                    if (result.getString(i) == null) {
                        row.add("NONE");
                    } else {
                        row.add(result.getString(i));
                    }
                }
                data.add(row);
            }
            tableView.setItems(data);
            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset (ActionEvent event) {
        try {
            tableView.getColumns().clear();
            search.clear();
            build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
