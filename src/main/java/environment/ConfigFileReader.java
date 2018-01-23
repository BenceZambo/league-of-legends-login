package environment;

import java.io.FileReader;
import java.io.IOException;

public class ConfigFileReader {
    private String filePath;

    public String read(){
        String content = "";
        try {
            FileReader fileReader = new FileReader(filePath);
            int i;
            while((i=fileReader.read())!=-1) {
                content += (char) i;
            }
            fileReader.close();
        } catch (IOException e) {
            return null;
        }
        return content;
    }

    public ConfigFileReader(String filePath) {
        this.filePath = filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
