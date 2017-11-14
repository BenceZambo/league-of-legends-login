import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Login {

    public void createLoginWindow(Stage window){


    GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

    Text scenetitle = new Text("BoostRoyal");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

    Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

    TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

    Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

    PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

    Button btn = new Button("Sign in");
    HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

    GridPane grid2 = new GridPane();
    Scene scene2 = new Scene(grid2, 1000, 750);

    btn.setOnAction(e -> {
        HttpHandler httpHandler = new HttpHandler();
        LinkedHashMap<String,String> urlParameters = new LinkedHashMap();

        String username = userTextField.getText();
        String password = pwBox.getText();

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
            Controller.setToken(ifAccountValid);
            BoosterPage boosterPage = new BoosterPage();
            boosterPage.createBoosterPage(window);
        }
    });

    Scene scene = new Scene(grid, 500, 350);
    window.setScene(scene);
    window.show();
}
}
