package controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import environment.ConfigFileReader;
import environment.ConfigFileWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logger.Globals;
import model.User;
import org.json.JSONException;
import org.json.JSONObject;
import services.OrderService;
import view.AlertBox;
import webService.HttpHandler;
import webService.WebSocketClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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
    CheckBox rememberMe;

    public void handleLoginButton(javafx.event.ActionEvent event) throws IOException {
        if (validate(username.getText())) {

            String username = this.username.getText();
            String password = this.password.getText();

            ConfigFileWriter configFileWriter = new ConfigFileWriter("loginData.txt");
            if (rememberMe.isSelected()){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                configFileWriter.write(jsonObject.toString());
            }else{
                configFileWriter.write("");
            }
            HttpHandler httpHandler = new HttpHandler();
            LinkedHashMap<String, String> urlParameters = new LinkedHashMap();

            urlParameters.put("email", username);
            urlParameters.put("password", password);
            urlParameters.put("applogin", "true");
            urlParameters.put("version", Globals.Version);

            String token;
            String JSONToken;
            try {
                JSONToken = httpHandler.sendingPostRequest("http://api.boostroyal.com/auth/login", urlParameters);
                JsonObject jsonObject = (new JsonParser()).parse(JSONToken).getAsJsonObject();
                token = jsonObject.get("token").toString();
            } catch (Exception e1) {
                System.out.println(e1.toString());
                if (e1.toString().equals("java.io.IOException: Server returned HTTP response code: 426 for URL: http://api.boostroyal.com/auth/login")){
                    JSONToken = "426";
                }else{
                    JSONToken = null;
                }
                token = null;
            }

            if (token != null) {

                Gson gson = new Gson();
                User user = gson.fromJson(JSONToken, User.class);

                try {
                    DecodedJWT jwt = JWT.decode(token);
                    user.setId(jwt.getClaims().get("user").asInt());
                } catch (JWTDecodeException exception){
                    //Invalid token
                }

                WebSocketClient webSocketClient = null;
                try {
                    webSocketClient = new WebSocketClient(new URI("http://api.boostroyal.com"));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                OrderService orderService = new OrderService();

                BoosterPageController boosterPageController = new BoosterPageController(user, webSocketClient, orderService);
                webSocketClient.setBoosterPageController(boosterPageController);
                FXMLLoader loginXML = new FXMLLoader(getClass().getResource("/templates/BoosterPage.fxml"));
                loginXML.setController(boosterPageController);
                Parent root = loginXML.load();
                Scene scene = new Scene(root);
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
            if (JSONToken == "426"){
                AlertBox.display("Alert", "Please download the updates");
            }
            if (JSONToken == null){
                AlertBox.display("Alert", "Invalid email, or password!");
            }
        }else{
            AlertBox.display("Alert", "Invalid email, or password!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Map<String, String> loginData = getLoginData();
        if (loginData != null){
            username.setText(loginData.get("username"));
            password.setText(loginData.get("password"));
            rememberMe.setSelected(true);
        }
        login.setOnAction(event -> {
            try {
                handleLoginButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        password.setOnAction(event -> {
            try {
                handleLoginButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        username.setOnAction(event -> {
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

    private Map<String, String> getLoginData(){
        ConfigFileReader configFileReader = new ConfigFileReader("loginData.txt");
        String loginData = configFileReader.read();
        if (loginData == null){
            return null;
        }
        JSONObject dataInJSON = null;
        HashMap<String, String> result = new HashMap<>();
        try {
            dataInJSON = new JSONObject(loginData);
            result.put("username", dataInJSON.getString("username"));
            result.put("password", dataInJSON.getString("password"));
        } catch (JSONException e) {
            return null;
        }
        return result;
    }
}
