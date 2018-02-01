package controller;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import environment.AccessWindow;
import environment.ConfigFileReader;
import environment.ConfigFileWriter;
import logger.Globals;
import model.User;
import model.orders.Order;
import model.orders.Server;
import view.AlertBox;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class AutoLoginer {

    public AutoLoginer() {
    }

    public void setUp(Order order){
        ConfigFileWriter configFileWriter = new ConfigFileWriter(Globals.LoLSettingsFilePath);
        String server = order.getServer().toString();
        String serverName;
        String username = order.getLoginname();
        switch (server) {
            case "LAS":
                serverName = "LA2";
                break;
            case "LAN":
                serverName = "LA1";
                break;
            case "OCE":
                serverName = "OC1";
                break;
            default:serverName = server;
        }

        String newFileContent =  "install:\n" +
                "    globals:\n" +
                "        locale: \"en_US\"\n" +
                "        region: \"" + serverName + "\"\n" +
                "    login-remember-me:\n" +
                "        rememberMe: false\n" +
                "        username: \"" + username + "\"";

        configFileWriter.write("");
        configFileWriter.write(newFileContent);
        clientExecutor();
    }

    public void logMeIn(String password) throws AWTException {
        Robot robot = new Robot();
        robot.setAutoDelay(25);
        robot.setAutoWaitForIdle(true);
        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();

        clientExecutor();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StringSelection accountPassword= new StringSelection(password);
        StringSelection clear = new StringSelection("");
        clipBoard.setContents(accountPassword, accountPassword);

        robot.delay(50);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);

        robot.delay(150);

        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);

        clipBoard.setContents(clear, clear);

        robot.delay(25);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public boolean checkIfConfigFileValid(Order order){
        ConfigFileReader configFileReader = new ConfigFileReader(Globals.LoLSettingsFilePath);
        String fileContent = configFileReader.read();
        String server = order.getServer().toString();

        if (order == null){
            return false;
        }
        if (server == Server.LAN.toString() || server == Server.LAS.toString() || server == Server.OCE.toString()){
            switch (server) {
                case "LAS":
                    server = "LA2";
                    break;
                case "LAN":
                    server = "LA1";
                    break;
                case "OCE":
                    server = "OC1";
                    break;
            }
            if (fileContent.indexOf(order.getLoginname()) != -1 && fileContent.indexOf(server) != -1){
                return true;
            }
        }
        if (fileContent.indexOf(order.getLoginname()) != -1 && fileContent.indexOf(order.getServer().toString()) != -1){
            System.out.println("loginname: " + order.getLoginname());
            System.out.println("Server: " + order.getServer().toString());
            System.out.println(fileContent.indexOf(order.getLoginname()) != -1);
            System.out.println(fileContent.indexOf(order.getServer().toString()) != -1);
            return true;
        }else{
            return false;
        }
    }

    private void clientExecutor(){
        Runtime runTime = Runtime.getRuntime();
        try {
            runTime.exec(Globals.LoLClientFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
