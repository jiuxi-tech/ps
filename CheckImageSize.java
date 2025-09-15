import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CheckImageSize {
    public static void main(String[] args) {
        try {
            // Check image size
            File imageFile = new File("ps-be/src/main/resources/META-INF/cut-image/slider/0bg.jpg");
            BufferedImage image = ImageIO.read(imageFile);
            if (image != null) {
                System.out.println("Image size: " + image.getWidth() + "x" + image.getHeight());
            } else {
                System.out.println("Cannot read image");
            }
        } catch (IOException e) {
            System.out.println("Error reading image: " + e.getMessage());
        }
    }
}