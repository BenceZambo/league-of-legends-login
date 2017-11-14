package view;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import webService.HttpHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BoosterPage {

    TableView<Order> table;

    public void createBoosterPage(Stage window) {
        //Id column
        TableColumn<Order, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        //Price column
        /*TableColumn<view.Order, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));*/

        //Purchase column
        TableColumn<Order, String> purchaseColumn = new TableColumn<>("Purchase");
        purchaseColumn.setMinWidth(200);
        purchaseColumn.setCellValueFactory(new PropertyValueFactory<>("purchase"));

        //Status column
        TableColumn<Order, Integer> statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        table = new TableView<>();
        table.setItems(initData());
        table.getColumns().addAll(idColumn, purchaseColumn, statusColumn);

        Button launchButton = new Button("Launch");
        launchButton.setOnAction(e -> launchButtonClicked());
        HBox hBox = new HBox();
        hBox.getChildren().addAll(launchButton);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, hBox);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }

    public ObservableList<Order> initData(){
        HttpHandler httpHandler = new HttpHandler();
        Gson gson = new Gson();
        LinkedHashMap<String,String> urlParameters = new LinkedHashMap();
        urlParameters.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoxLCJpYXQiOjE1MTA0MTgyMDl9.xqOb4YCQXIKLrovARyifb9KiKUkAJnRtyeS3bVofnqQ");
        ObservableList<Order> products = FXCollections.observableArrayList();
        String response = "";
        try {
            response = httpHandler.sendingPostRequest("http://localhost:9999/", urlParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Order[] postResponse = gson.fromJson(response, Order[].class);
        for (int i=0; i<postResponse.length; i++){
            products.add(postResponse[i]);
        }

        return products;
    }

    public void launchButtonClicked(){
        Order orderSelected;
        List<String> accountData = new ArrayList<>();

        orderSelected = table.getSelectionModel().getSelectedItem();

        String username = orderSelected.getLoginname();
        String password = orderSelected.getLoginpassword();

        System.out.println(username);
        System.out.println(password);

        accountData.add(username);
        accountData.add(password);
    }
}
