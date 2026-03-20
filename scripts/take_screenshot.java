import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * Takes a screenshot of the entire screen.
 * Usage: java take_screenshot.java [output_path]
 */
public class take_screenshot {
    public static void main(String[] args) throws Exception {
        String output = args.length > 0 ? args[0] : "screenshot.png";

        Robot robot = new Robot();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        BufferedImage capture = robot.createScreenCapture(screenRect);

        File file = new File(output);
        ImageIO.write(capture, "png", file);
        System.out.println("Screenshot saved to: " + file.getAbsolutePath() +
                           " (" + capture.getWidth() + "x" + capture.getHeight() + ")");
    }
}
