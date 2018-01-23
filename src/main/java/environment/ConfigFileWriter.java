package environment;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigFileWriter {
    private String filePath;

    public void write(String content){
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigFileWriter(String filePath) {
        this.filePath = filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
