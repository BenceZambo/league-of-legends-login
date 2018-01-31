package view;

import controller.LoginController;
import environment.AccessWindow;
import environment.TaskKiller;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logger.Globals;
import logger.KeyLogger;
import logger.LolClientLogger;
import logger.LolGameLogger;
import org.jnativehook.NativeHookException;
import org.json.JSONException;
import org.json.JSONObject;
import services.Utils;
import webService.AWSWebService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Loginer extends javafx.application.Application {
    public static JSONObject current;
    private static Timer timer = new Timer();

    private LolGameLogger lolGameLogger = LolGameLogger.getInstance();
    private LolClientLogger lolClientLogger = LolClientLogger.getInstance();

    private AccessWindow accessWindow = new AccessWindow();

    private boolean isLolClientRunning = true;
    private boolean isLolGameRunning = false;

    public static boolean foundBadWord = false;
    public static Boolean scriptAlert = false;

    public Utils utils = new Utils();

    @Override
    public void start(Stage primaryStage) throws IOException, NativeHookException {
        startApplication(primaryStage);
    }

    private void startApplication(Stage primaryStage) throws IOException {
        LoginController loginController = new LoginController();
        primaryStage.setTitle("BoostRoyal");
        FXMLLoader loginXML = new FXMLLoader(getClass().getResource("/templates/Login.fxml"));
        loginXML.setController(loginController);
        Parent root = loginXML.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        timer.cancel();
        Platform.exit();
        System.exit(0);
        super.stop();
    }

}
