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
import view.AlertBox;
import webService.HttpHandler;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    @FXML
    TextField password, username;

    @FXML
    Button login;

    @FXML
    public void handleLoginButton(javafx.event.ActionEvent event) throws IOException {
        HttpHandler httpHandler = new HttpHandler();
        LinkedHashMap<String,String> urlParameters = new LinkedHashMap();

        String username = this.username.getText();
        String password = this.password.getText();

        urlParameters.put("email", username);
        urlParameters.put("password", password);

        String token;
        try {
            token = httpHandler.sendingPostRequest("http://boostroyal.fhesfjrizw.eu-west-2.elasticbeanstalk.com/auth/login", urlParameters);
            System.out.println(token);
        } catch (Exception e1) {
            token = null;
        }


        if(token != null) {
            Gson gson = new Gson();
            User user = gson.fromJson(token, User.class);
            BoosterPageController boosterPageController = new BoosterPageController(user);
            FXMLLoader loginXML = new FXMLLoader(getClass().getResource("/templates/BoosterPage.fxml"));
            loginXML.setController(boosterPageController);
            Parent root = loginXML.load();
            Scene scene = new Scene(root);
            Node node=(Node) event.getSource();
            Stage stage=(Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }else{
            AlertBox.display("Alert", "Invalid username, or password!");
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
}
