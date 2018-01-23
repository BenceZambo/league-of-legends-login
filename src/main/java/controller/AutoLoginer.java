package controller;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import environment.AccessWindow;
import environment.ConfigFileReader;
import environment.ConfigFileWriter;
import logger.Globals;
import model.orders.Order;
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
        ConfigFileWriter configFileWriter = new ConfigFileWriter("D:\\Riot Games\\League of Legends\\Config\\LeagueClientSettings.yaml");
        String server = order.getServer().toString();
        String username = order.getLoginname();

        String newFileContent =  "install:\n" +
                "    globals:\n" +
                "        locale: \"en_US\"\n" +
                "        region: \"" + server + "\"\n" +
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
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StringSelection accountPassword= new StringSelection(password);
        StringSelection clear = new StringSelection("");
        clipBoard.setContents(accountPassword, accountPassword);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);

        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);

        clipBoard.setContents(clear, clear);

        robot.delay(10);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public boolean checkIfConfigFileValid(Order order){
        ConfigFileReader configFileReader = new ConfigFileReader("D:\\Riot Games\\League of Legends\\Config\\LeagueClientSettings.yaml");
        String fileContent = configFileReader.read();

        if (order == null){
            return false;
        }
        if (fileContent.indexOf(order.getLoginname()) != -1 && fileContent.indexOf(order.getServer().toString()) != -1){
            return true;
        }else{
            return false;
        }
    }

    private void clientExecutor(){
        Runtime runTime = Runtime.getRuntime();
        try {
            runTime.exec("D:\\Riot Games\\League of Legends\\LeagueClient");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
