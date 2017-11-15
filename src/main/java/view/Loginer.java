package view;

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

import java.io.IOException;

public class Loginer extends javafx.application.Application {
    //public static String boosterName = "Barney";

    //private LolGameLogger lolGameLogger = LolGameLogger.getInstance();
    //private LolClientLogger lolClientLogger = LolClientLogger.getInstance();

    //private AccessWindow accessWindow = new AccessWindow();

    //private boolean isLolClientRunning = true;
    //private boolean isLolGameRunning = false;

    private static String token;

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

        primaryStage.setTitle("Booster view.Loginer");
        Parent loginXML = FXMLLoader.load(getClass().getResource("/templates/Login.fxml"));
        Scene scene = new Scene(loginXML);
        primaryStage.setScene(scene);
        primaryStage.show();

        //lolGameLogger.turnOn();
        //lolClientLogger.turnOn();
    }

    /*private void uploadLog() {
        try {
            AWSWebService webService =  new AWSWebService(lolClientLogger.getFilePath(), boosterName);
            webService.WebService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void setToken(String token){
        Loginer.token = token;
    }
}
