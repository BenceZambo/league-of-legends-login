import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TaskKiller {
    private static String requestProccessess = "WMIC PROCESS get caption";
    private static String LEAUGE_OF_LEGENDS = "LeagueofLegends.exe";
    private static String LOL_CLIENT = "LeagueClient.exe";
    private static String taskkill = "taskkill /F /IM ";

    private static Runtime runtime = Runtime.getRuntime();

    public static void checkRunningGame(String processName) {
        if(processName.equals(LEAUGE_OF_LEGENDS)) {
            System.out.println("Please finish your game and then restart the application");
            System.exit(1);
        }
    }

    public static void closeRunningClient(String processName) throws IOException {
        if (processName.equals(LOL_CLIENT))
            runtime.exec(taskkill + LOL_CLIENT);
    }

    public static ArrayList<String> readProccesses(BufferedReader input) throws IOException {
        String processName;
        ArrayList<String> arrayList = new ArrayList<>();

        while((processName = input.readLine()) != null) {
            processName = processName.replaceAll("\\s", "");
            checkRunningGame(processName);
            arrayList.add(processName);
        }

        return arrayList;
    }

    public static void requestRunningProccesses() {
        try {
            final Process process = runtime.exec(requestProccessess);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

            ArrayList<String> arrayList = readProccesses(input);
            process.destroy();

            for(String processName: arrayList){
                closeRunningClient(processName);
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }
}
