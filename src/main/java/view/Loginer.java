package view;

import controller.LoginController;
import environment.TaskKiller;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import environment.AccessWindow;
import logger.Globals;
import logger.LolClientLogger;
import logger.LolGameLogger;
import org.jnativehook.NativeHookException;
import webService.AWSWebService;
import webService.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Loginer extends javafx.application.Application {
    //public static String boosterName = "Barney";

    //private LolGameLogger lolGameLogger = LolGameLogger.getInstance();
    //private LolClientLogger lolClientLogger = LolClientLogger.getInstance();

    //private AccessWindow accessWindow = new AccessWindow();

    //private boolean isLolClientRunning = true;
    //private boolean isLolGameRunning = false;

    @Override
    public void start(Stage primaryStage) throws IOException, NativeHookException {
        /*TaskKiller.requestRunningProccesses();
        Platform.setImplicitExit(false);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                isLolClientRunning = accessWindow.checkIfRunning(Globals.lolClient);
                isLolGameRunning = accessWindow.checkIfRunning(Globals.lolGame);

                if(isLolGameRunning || isLolClientRunning) {
                    event.consume();
                    AlertBox.display("You can't close me", "Sorry dude, you can not close me if lol is running.");
                } else {
                    uploadLog();
                    lolGameLogger.turnOff();
                    lolClientLogger.turnOff();
                }
            }
        });*/

        LoginController loginController = new LoginController();
        primaryStage.setTitle("BoostRoyal");
        FXMLLoader loginXML = new FXMLLoader(getClass().getResource("/templates/Login.fxml"));
        loginXML.setController(loginController);
        Parent root = loginXML.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        //lolGameLogger.turnOn();
        //lolClientLogger.turnOn();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Ez a loginerből volt");
        Platform.exit();
        System.exit(0);
        super.stop();
    }


    /*private void uploadLog() {
        try {
            AWSWebService webService =  new AWSWebService(lolClientLogger.getFilePath(), boosterName);
            webService.WebService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
