package controller;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import view.Loginer;
import view.Order;
import webService.HttpHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;

public class BoosterPageController implements Initializable {
    @FXML
    TableView<Order> table;
    @FXML
    private TableColumn<Order, String> id_column;
    @FXML
    private TableColumn<Order, String> purchase_column;
    @FXML
    private TableColumn<Order, String> status_column;


    @FXML
    public void launchButtonClicked() {
        Order orderSelected;
        List<String> accountData = new ArrayList<>();

        orderSelected = table.getSelectionModel().getSelectedItem();

        String username = orderSelected.getLoginname();
        String password = orderSelected.getLoginpassword();
        String purchase = orderSelected.getPurchase();

        System.out.println(purchase);
        System.out.println(username);
        System.out.println(password);

        accountData.add(username);
        accountData.add(password);
    }

    @FXML
    public void signoutButtonHandler(ActionEvent event) throws IOException {
        Loginer.setToken("");
        Parent loginXML = FXMLLoader.load(getClass().getResource("/templates/Login.fxml"));
        Scene scene = new Scene(loginXML);
        Node node=(Node) event.getSource();
        Stage stage=(Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public ObservableList<Order> initData() {
        HttpHandler httpHandler = new HttpHandler();
        Gson gson = new Gson();
        LinkedHashMap<String, String> urlParameters = new LinkedHashMap();
        urlParameters.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoxLCJpYXQiOjE1MTA0MTgyMDl9.xqOb4YCQXIKLrovARyifb9KiKUkAJnRtyeS3bVofnqQ");
        ObservableList<Order> products = FXCollections.observableArrayList();
        String response = "";
        try {
            response = httpHandler.sendingPostRequest("http://localhost:9999/", urlParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Order[] postResponse = gson.fromJson(response, Order[].class);
        for (int i = 0; i < postResponse.length; i++) {
            products.add(postResponse[i]);
        }
        return products;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id_column.setCellValueFactory(new PropertyValueFactory<Order, String>("id"));
        purchase_column.setCellValueFactory(new PropertyValueFactory<Order, String>("purchase"));
        status_column.setCellValueFactory(new PropertyValueFactory<Order, String>("status"));

        table.getItems().setAll(initData());
    }
}
