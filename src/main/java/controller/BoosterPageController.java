package controller;

import com.google.gson.Gson;
import environment.AccessWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logger.Globals;
import model.Order;
import model.Status;
import model.User;
import org.json.JSONException;
import org.json.JSONObject;
import services.OrderService;
import view.AlertBox;
import webService.HttpHandler;
import webService.WebSocketClient;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

public class BoosterPageController implements Initializable {
    User user;
    WebSocketClient webSocketClient;
    OrderService orderService;
    @FXML
    Button signOut;
    @FXML
    Button launchButton;
    @FXML
    TableView<Order> table;
    @FXML
    private TableColumn<Order, String> id_column;
    @FXML
    private TableColumn<Order, String> purchase_column;
    @FXML
    private TableColumn<Order, String> server_column;
    @FXML
    private TableColumn<Order, String> status_column;

    public BoosterPageController(User user, WebSocketClient webSocketClient,
                                 OrderService orderService) {
        this.orderService = orderService;
        this.user = user;
        this.webSocketClient = webSocketClient;
    }

    @FXML
    public void launchButtonHandler() {
        Order orderSelected;
        AccessWindow accessWindow = new AccessWindow();

        orderSelected = table.getSelectionModel().getSelectedItem();

        String username = orderSelected.getLoginname();
        String password = orderSelected.getLoginpassword();

        System.out.println(username);
        System.out.println(password);

        /*orderNotification, { type: 'login VAGY logout', id: 'order id itt', to: 'customer_id (ezt is elküldöm orderekkel együtt)' }
        ja és a küldésnél a customer_id-t azt arrayba küld
        Áron Liptai*/


        if (accessWindow.checkIfRunning(Globals.lolClient) && orderSelected.getStatus() == Status.PROCESSING){
            AutoLoginer autoLoginer = new AutoLoginer();
            try {
                //TODO websocket send order login to server
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "login");
                jsonObject.put("id", orderSelected.getId());
                jsonObject.put("customer_id", orderSelected.getCustomer_id());
                webSocketClient.send("orderNotification", jsonObject);
                autoLoginer.logMeIn(username, password);
            } catch (AWTException e) {
                AlertBox.display("Login fail", "Can't login");
            }catch (JSONException e){
                System.out.println(e);
            }
        }
        if (!accessWindow.checkIfRunning(Globals.lolClient)){
            AlertBox.display("Client does not run", "Please run the League of Legends client");
        }
        if (orderSelected.getStatus() == Status.PAUSED){
            AlertBox.display("Order paused", "This order is paused, you cannot boost on this");
        }
    }

    @FXML
    public void signoutButtonHandler(ActionEvent event) throws IOException {
        LoginController loginController = new LoginController();
        FXMLLoader loginXML = new FXMLLoader(getClass().getResource("/templates/Login.fxml"));
        loginXML.setController(loginController);
        Parent root = loginXML.load();
        Scene scene = new Scene(root);
        Node node=(Node) event.getSource();
        Stage stage=(Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public ObservableList<Order> initData() {
        HttpHandler httpHandler = new HttpHandler();
        Gson gson = new Gson();
        LinkedHashMap<String, String> urlParameters = new LinkedHashMap();
        urlParameters.put("access_token", user.getToken());
        ObservableList<Order> products = FXCollections.observableArrayList();
        String response = "";
        try {
            response = httpHandler.sendingPostRequest("http://boostroyal.fhesfjrizw.eu-west-2.elasticbeanstalk.com/order/getOrdersApp", urlParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(response);
        Order[] postResponse = gson.fromJson(response, Order[].class);
        orderService.setArrayOfOrders(postResponse);
        for (int i = 0; i < postResponse.length; i++) {
            products.add(postResponse[i]);
        }
        return products;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        purchase_column.setCellValueFactory(new PropertyValueFactory<>("purchase"));
        server_column.setCellValueFactory(new PropertyValueFactory<>("server"));
        status_column.setCellValueFactory(new PropertyValueFactory<>("status"));

        launchButton.setOnAction(event -> launchButtonHandler());

        signOut.setOnAction(event -> {
            try {
                signoutButtonHandler(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        table.getItems().setAll(initData());
    }

}
