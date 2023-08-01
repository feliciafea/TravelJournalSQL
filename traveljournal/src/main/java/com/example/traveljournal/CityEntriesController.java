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

public class CityEntriesController extends Application {

    public String city;
    public String country;

    public String user;

    @FXML
    public TableView tableView;

    ObservableList<ObservableList> data;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }

    public void back (ActionEvent event) throws SQLException {
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

    public void setCity(String city, String country, String user) {
        this.user = user;
        this.country = country;
        this.city = city;
        build();
    }

    public void build() {
        tableView.getColumns().clear();
        data = FXCollections.observableArrayList();
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();

        String tableQuery =
        "SELECT EntryDate, Rating, Note, EntryID " +
        "FROM JOURNAL_ENTRY " +
        "WHERE CityName = '"+ city + "' AND Country = '"+ country +"' AND PrivacyLevel = True;";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery((tableQuery));
            tableView.getItems().clear();
            for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col;
                if (i == 0) {
                    col = new TableColumn("Date");
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

    public void oneEntry (Event event) throws SQLException {
        try {
            String userlist = tableView.getSelectionModel().getSelectedItem().toString();
            if (userlist != null) {
                String entryID = userlist.substring(1, userlist.length() - 1).split(", ")[3];
                FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("ViewCityEntryScene.fxml")));
                Scene scene = new Scene(fxmlLoader.load());
                //Stage stage = getCurrentStage(event);
                Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                stage.setScene(scene);
                ViewCityEntryController controller = fxmlLoader.getController();
                controller.setEntryID(entryID, city, country, user);
            }
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
}
