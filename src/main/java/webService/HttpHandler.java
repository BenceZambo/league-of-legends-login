package webService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Arpit Mandliya
 */

public class HttpHandler {

    private final String USER_AGENT = "Mozilla/5.0";

    // HTTP Post request
    public String sendingPostRequest(String url, LinkedHashMap<String,String> parameters) throws Exception {

        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Setting basic post request
        con.setRequestMethod("POST");

        String urlParameters = "";

        Object lastElement = parameters.entrySet().toArray()[parameters.size() -1];
        for (Map.Entry<String,String> pair : parameters.entrySet()){
            urlParameters += pair.getKey() + "=" + pair.getValue();
            if (pair.equals(lastElement)){
                break;
            }
            urlParameters += "&";
        }
        // Send post request
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        //printing result from response
        return response.toString();
    }
}

