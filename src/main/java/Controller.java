import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logger.AccessWindow;
import logger.Globals;
import logger.LolClientLogger;
import logger.LolGameLogger;
import org.jnativehook.NativeHookException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller extends Application{
    private String boosterName = "Barney";

    //private LolGameLogger lolGameLogger = LolGameLogger.getInstance();
    //private LolClientLogger lolClientLogger = LolClientLogger.getInstance();

    //private AccessWindow accessWindow = new AccessWindow();

    private boolean isLolClientRunning = true;
    private boolean isLolGameRunning = false;


    public static void main(String[] args) {
        launch(args);
    }
    private static String token;

    @Override
    public void start(Stage primaryStage) throws IOException, NativeHookException {
        //TaskKiller.requestRunningProccesses();
        primaryStage.setTitle("Booster App");
        //Platform.setImplicitExit(false);

        /*primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                isLolClientRunning = accessWindow.checkIfRunning(Globals.lolClient);
                isLolGameRunning = accessWindow.checkIfRunning(Globals.lolGame);
                System.out.println(isLolClientRunning);
                System.out.println(isLolGameRunning);

                if(isLolGameRunning || isLolClientRunning) {
                    event.consume();
                    AlertBox.display("You can't close me", "Sorry dude, you can not close me if lol is runnig.");
                } else {
                    uploadLog();
                }
            }
        });*/
        Login loginWindow = new Login();

        loginWindow.createLoginWindow(primaryStage);

        //lolGameLogger.turnOn();
        //lolClientLogger.turnOn();
    }

    /*private void uploadLog() {
        try {
            WebService webService =  new WebService(lolClientLogger.getFilePath(), boosterName);
            webService.WebService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void setToken(String token){
        Controller.token = token;
    }
}
