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

public class AdminHomeController extends Application {
    public String user;
    @FXML
    public TableView tableView;
    @FXML
    public TableColumn userCol;
    List<String> columnNameList = new ArrayList<>();
    List<String> tableNameList = new ArrayList<>();
    ObservableList<ObservableList> data;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }

    public void reviewReport(Event event) throws SQLException {
        try {
            String userlist = tableView.getSelectionModel().getSelectedItem().toString();
            if (userlist != null) {
//                System.out.println(userlist);
                String city = userlist.substring(1, userlist.length() - 1).split(", ")[1];
                String date = userlist.substring(1, userlist.length() - 1).split(", ")[5];
                String flaggedUser = userlist.substring(1, userlist.length() - 1).split(", ")[0];
//                System.out.println(city);
//                System.out.println(date);
                FXMLLoader fxmlLoader = new FXMLLoader((TJApp.class.getResource("Review_Flagged_Entry.fxml")));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                stage.setScene(scene);
                ReviewReport controller = fxmlLoader.getController();
                controller.setInfo(user, city, date, flaggedUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void build() {
        data = FXCollections.observableArrayList();
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();

        String tableQuery = "SELECT je.Username, je.CityName, je.Note, GROUP_CONCAT(fr.Reason SEPARATOR \"; \"), uf.USERNAME AS Flagger, je.EntryDate " +
                "FROM JOURNAL_ENTRY je JOIN USER_FLAGS uf ON je.EntryID = uf.EntryID LEFT OUTER JOIN FLAG_REASON fr " +
                "ON uf.Username = fr.Username AND uf.EntryID and fr.EntryID GROUP BY je.Username, je.CityName, je.Note, je.EntryDate, Flagger;";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery((tableQuery));
            for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col;
                if (i == 4) {
                    col = new TableColumn("Flagger");
                } else if (i == 3) {
                    col = new TableColumn("Reasons");
                } else if (i == 1) {
                    col = new TableColumn("City");
                } else {
                    col = new TableColumn(result.getMetaData().getColumnName(i + 1));
                }
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
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

    public void setInfo(String user) {
        this.user = user;
        build();
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
}
