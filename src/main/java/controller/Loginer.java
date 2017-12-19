package controller;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class Loginer {

    public Loginer() {
    }

    public void logMeIn(String username, String password) throws AWTException {
        Robot robot = new Robot();
        robot.setAutoDelay(40);
        robot.setAutoWaitForIdle(true);

        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();

        StringSelection accountName = new StringSelection(username);
        StringSelection accountPassword= new StringSelection(password);
        clipBoard.setContents(accountName, accountName);

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




    }
}
