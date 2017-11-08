package keyLogger;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException, AWTException {
        System.out.println("ASDASDSADASDASDASDSA");
        String fileName = "src/main/java/keyLogger/logs/" + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".csv";
        LoLClientLogger loLClientLogger = LoLClientLogger.getInstance();
        LoLGameLogger loLGameLogger = LoLGameLogger.getInstance();
        AccessWindow accessWindow = new AccessWindow();
        while (true) {
            if(!loLClientLogger.isCurrentlyRunning()) {
                loLClientLogger.setFileName(fileName);
                loLClientLogger.turnOn();
            }
            if(!loLGameLogger.isCurrentlyRunning()) {
                loLGameLogger.setFileName(fileName);
                loLGameLogger.turnOn();
            }
            if(accessWindow.getActiveWindowTitle().equals(Globals.lolGame)) {
                BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                ImageIO.write(image, "png", new File("/screenshot.png"));
            }
        }
    }
}
