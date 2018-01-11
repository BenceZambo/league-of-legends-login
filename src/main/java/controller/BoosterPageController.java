package controller;

import com.google.gson.Gson;
import environment.AccessWindow;
import environment.TaskKiller;
import javafx.application.Platform;
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
import logger.KeyLogger;
import logger.LolClientLogger;
import logger.LolGameLogger;
import model.Order;
import model.Status;
import model.User;
import org.json.JSONException;
import org.json.JSONObject;
import services.OrderService;
import services.Utils;
import view.AlertBox;
import view.Loginer;
import webService.HttpHandler;
import webService.WebSocketClient;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BoosterPageController implements Initializable {
    User user;
    WebSocketClient webSocketClient;
    OrderService orderService;
    boolean loggedIn = false;
    boolean closeMethodSet = false;
    Order currentOrder;
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

    private LolGameLogger lolGameLogger = LolGameLogger.getInstance();
    private LolClientLogger lolClientLogger = LolClientLogger.getInstance();
    private Utils utils = new Utils();
    private static Timer timer = new Timer();
    AccessWindow accessWindow = new AccessWindow();

    public BoosterPageController(User user, WebSocketClient webSocketClient,
                                 OrderService orderService) {
        this.orderService = orderService;
        this.user = user;
        this.webSocketClient = webSocketClient;
    }

    @FXML
    public void launchButtonHandler() {
        Order orderSelected;

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

                System.out.println(closeMethodSet);
                if (closeMethodSet == false){
                    setClose();
                    lolGameLogger.turnOn();
                    lolClientLogger.turnOn();
                    closeMethodSet = true;
                }

                autoLoginer.logMeIn(username, password);
                if (loggedIn){
                    JSONObject logoutJsonObject = getLogInJSON(JSONType.LOGOUT, currentOrder);
                    webSocketClient.send("orderNotification", logoutJsonObject);
                    System.out.println("logout websocket message sent" + logoutJsonObject.toString());
                    if(KeyLogger.log.size() != 0) {
                        utils.uploadLog(user, currentOrder);
                    }
                }
                JSONObject loginJsonObject = getLogInJSON(JSONType.LOGIN, orderSelected);
                webSocketClient.send("orderNotification", loginJsonObject);
                System.out.println("login websocket sent" + loginJsonObject.toString());
                currentOrder = orderSelected;
                loggedIn = true;


                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        ArrayList<String> runningProcesses = TaskKiller.requestRunningProccesses();
                        for (String process : runningProcesses) {
                            if(process.contains("BoL Studio.exe") || process.contains("Loader.exe")) {
                                utils.scriptAlert = true;
                            }
                        }
                    }
                }, 1000, 300000);

            } catch (AWTException e) {
                AlertBox.display("Login fail", "Can't login");
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
        if (!accessWindow.checkIfRunning(Globals.lolClient)){
        webSocketClient.disconnect();
        LoginController loginController = new LoginController();
        FXMLLoader loginXML = new FXMLLoader(getClass().getResource("/templates/Login.fxml"));
        loginXML.setController(loginController);
        Parent root = loginXML.load();
        Scene scene = new Scene(root);
        Node node=(Node) event.getSource();
        Stage stage=(Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.show();}
        else {
            AlertBox.display("Error", "Close the lol client.");
        }
        if(KeyLogger.log.size() != 0) {
            utils.uploadLog(user, currentOrder);
        }

    }

    public void initData() {
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
        table.getItems().setAll(products);
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

        initData();
    }

    public JSONObject getLogInJSON(JSONType jsonType, Order order){
        JSONObject loginJsonObject = new JSONObject();
        Integer[] to = new Integer[1];
        to[0] = order.getCustomer_id();
        try {
            loginJsonObject.put("to", to);
            loginJsonObject.put("id", order.getId());
            if (jsonType == JSONType.LOGIN) {
                loginJsonObject.put("type", "login");
            }else {
                loginJsonObject.put("type", "logout");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return loginJsonObject;
    }

    private enum JSONType{
        LOGIN, LOGOUT
    }

    private void setClose(){
        Stage stage = (Stage) signOut.getScene().getWindow();
        stage.setOnCloseRequest(event -> {
        if(accessWindow.checkIfRunning(Globals.lolClient)) {
            AlertBox.display("Error", "Close the lol client.");
            event.consume();
        } else {
                if (loggedIn){
                    JSONObject logoutJsonObject = getLogInJSON(JSONType.LOGOUT, currentOrder);
                    webSocketClient.send("orderNotification", logoutJsonObject);
                    System.out.println("websocket message sent" + "habhahahaha");
                }
                webSocketClient.disconnect();
                if(KeyLogger.log.size() != 0) {
                    utils.uploadLog(user, currentOrder);
                }
                lolGameLogger.turnOff();
                lolClientLogger.turnOff();
                timer.cancel();
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public Order getCurrentOrder(){
        return currentOrder;
    }
}
