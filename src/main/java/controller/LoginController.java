package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import view.AlertBox;
import view.Loginer;
import webService.HttpHandler;

import java.io.IOException;
import java.util.LinkedHashMap;

public class LoginController {
    @FXML
    TextField password, username;

    @FXML
    public void handleLoginButton(javafx.event.ActionEvent event) throws IOException {
        HttpHandler httpHandler = new HttpHandler();
        LinkedHashMap<String,String> urlParameters = new LinkedHashMap();

        String username = this.username.getText();
        String password = this.password.getText();

        urlParameters.put("username", username);
        urlParameters.put("password", password);

        String ifAccountValid = "";
        try {
            ifAccountValid = httpHandler.sendingPostRequest("http://localhost:9999/login", urlParameters);
            System.out.println(ifAccountValid);
        } catch (Exception e1) {
            e1.printStackTrace();
        }


        if (ifAccountValid.equals("false")) {
            AlertBox.display("Alert", "Invalid username, or password!");

        } else if(ifAccountValid.equals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoxLCJpYXQiOjE1MTA0MTgyMDl9.xqOb4YCQXIKLrovARyifb9KiKUkAJnRtyeS3bVofnqQ")) {
            Loginer.setToken(ifAccountValid);
            Parent loginXML = FXMLLoader.load(getClass().getResource("/templates/BoosterPage.fxml"));
            Scene scene = new Scene(loginXML);
            Node node=(Node) event.getSource();
            Stage stage=(Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    };
}
