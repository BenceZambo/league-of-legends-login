package controller;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import logger.Globals;
import view.AlertBox;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AutoLoginer {

    public AutoLoginer() {
    }

    public void logMeIn(String username, String password) throws AWTException {
        Robot robot = new Robot();
        robot.setAutoDelay(5);
        robot.setAutoWaitForIdle(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();

        StringSelection accountName = new StringSelection(username);
        StringSelection accountPassword= new StringSelection(password);
        StringSelection clear = new StringSelection("");
        clipBoard.setContents(accountName, accountName);

        WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null, Globals.lolClient);
        User32.INSTANCE.SetForegroundWindow(hwnd);
        WinDef.RECT rect = new WinDef.RECT();
        User32.INSTANCE.GetWindowRect(hwnd, rect);
        int windowHeight = rect.toRectangle().height;
        int x, y;
        switch (windowHeight){
            case 576:
                x = (screenSize.width - 1024) / 2 + 1024 - 100;
                y = (screenSize.height - 576) / 2 + 130;
                break;
            case 720:
                x = (screenSize.width - 1280) / 2 + 1280 - 115;
                y = (screenSize.height - 720) / 2 + 160;
                break;
            case 900:
                x = (screenSize.width - 1600) / 2 + 1600 - 160;
                y = (screenSize.height - 900) / 2 + 230;
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

        clipBoard.setContents(clear, clear);
    }
}
