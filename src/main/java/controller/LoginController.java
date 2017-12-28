package controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import services.OrderService;
import view.AlertBox;
import webService.HttpHandler;
import webService.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController implements Initializable{
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @FXML
    TextField password, username;

    @FXML
    Button login;

    @FXML
    public void handleLoginButton(javafx.event.ActionEvent event) throws IOException {
        if (validate(username.getText())) {
            HttpHandler httpHandler = new HttpHandler();
            LinkedHashMap<String, String> urlParameters = new LinkedHashMap();

            String username = this.username.getText();
            String password = this.password.getText();

            urlParameters.put("email", username);
            urlParameters.put("password", password);
            urlParameters.put("applogin", "true");

            String token;
            try {
                token = httpHandler.sendingPostRequest("http://boostroyal.fhesfjrizw.eu-west-2.elasticbeanstalk.com/auth/login", urlParameters);
                System.out.println(token);
            } catch (Exception e1) {
                token = null;
            }

            //sRyMAN4/3
            if (token != null) {

                Gson gson = new Gson();
                User user = gson.fromJson(token, User.class);
                WebSocketClient webSocketClient = null;
                OrderService orderService = new OrderService();
                try {
                    webSocketClient = new WebSocketClient(orderService, new URI("https://boostroyal.fhesfjrizw.eu-west-2.elasticbeanstalk.com"));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                BoosterPageController boosterPageController = new BoosterPageController(user, webSocketClient, orderService);
                FXMLLoader loginXML = new FXMLLoader(getClass().getResource("/templates/BoosterPage.fxml"));
                loginXML.setController(boosterPageController);
                Parent root = loginXML.load();
                Scene scene = new Scene(root);
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                AlertBox.display("Alert", "Invalid username, or password!");
            }
        }else{
            AlertBox.display("Alert", "Invalid email address");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login.setOnAction(event -> {
            try {
                handleLoginButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }
}
