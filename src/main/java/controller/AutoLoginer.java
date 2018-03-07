package controller;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import environment.AccessWindow;
import environment.ConfigFileReader;
import environment.ConfigFileWriter;
import logger.Globals;
import logger.KeyLogger;
import model.User;
import model.orders.Order;
import model.orders.Server;
import view.AlertBox;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AutoLoginer {

    public AutoLoginer() {
    }

    public void logMeIn(String username, String password) throws AWTException {
        Robot robot = new Robot();
        robot.setAutoDelay(30);
        robot.setAutoWaitForIdle(true);
        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();

        StringSelection accountName = new StringSelection(username);
        StringSelection accountPassword= new StringSelection(password);
        StringSelection clear = new StringSelection("");
        clipBoard.setContents(accountName, accountName);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        KeyLogger.log.add("Screen width: " + width);
        double height = screenSize.getHeight();
        KeyLogger.log.add("Screen height: " + height);

        WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null, Globals.lolClient);
        WinDef.HWND hwnd1 = User32.INSTANCE.FindWindow("Shell_TrayWnd", null);
        WinDef.RECT rect = new WinDef.RECT();
        User32.INSTANCE.SetForegroundWindow(hwnd);
        User32.INSTANCE.GetWindowRect(hwnd, rect);

        KeyLogger.log.add("Rectangle height: " + rect.toRectangle().getHeight());
        KeyLogger.log.add("Rectangle width: " + rect.toRectangle().getWidth());

        if (rect.toRectangle().getHeight() > 29 || rect.toRectangle().getWidth() > 161) {
            int windowLeftCoordinate = rect.left;
            int windowTopCoordinate = rect.top;
            int windowHeight = rect.toRectangle().height;
            int x, y;
            switch (windowHeight) {
                case 576:
                    x = windowLeftCoordinate + 1024 - 100;
                    y = windowTopCoordinate + 140;
                    KeyLogger.log.add("case 576: x: " + x);
                    KeyLogger.log.add("case 576: y: " + y);
                    break;
                case 720:
                    x = windowLeftCoordinate + 1280 - 115;
                    y = windowTopCoordinate + 180;
                    KeyLogger.log.add("case 720: x: " + x);
                    KeyLogger.log.add("case 720: y: " + y);
                    break;
                case 900:
                    x = windowLeftCoordinate + 1600 - 160;
                    y = windowTopCoordinate + 230;
                    KeyLogger.log.add("windowLeftCoordinate: " + windowLeftCoordinate);
                    KeyLogger.log.add("windowTopCoordinate: " + windowTopCoordinate);
                    KeyLogger.log.add("case 900: x: " + x);
                    KeyLogger.log.add("case 900: y: " + y);
                    break;
                default:
                    x = 0;
                    y = 0;
            }

            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            robot.delay(5);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            robot.delay(5);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);

            clipBoard.setContents(accountPassword, accountPassword);

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            clipBoard.setContents(clear, clear);
        }else{
            AlertBox.display("LoL clien is on tray", "Please click on LoL client, and than you could auto login int the program!");
        }
    }

    public boolean checkIfConfigFileValid(Order order) throws FileNotFoundException{
        if (Globals.LoLSettingsFilePath.equals("")){
            throw new FileNotFoundException();
        }
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
            if (fileContent.indexOf(server) != -1){
                return true;
            }
        }
        if (fileContent.indexOf(order.getServer().toString()) != -1){
            return true;
        }else{
            return false;
        }
    }
}
