package mgsa;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class MGSA {

    private static boolean test(BufferedImage img, int x, int y) {
        int black = -16777216;
        if (img.getRGB(x, y) == black) {
            return false;
        }
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (img.getRGB(x + dx, y + dy) == black) {
                    count++;
                }
            }
        }
        if (count != 5 && count != 1) {
            return false;
        }
        count = 0;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (img.getRGB(x + dx, y + dy) == black) {
                    count++;
                }
            }
        }
        if (count != 16 && count != 4) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        officedraw.Offices.init();
        MainJFrame frame = new MainJFrame();
        while (true) {
            frame.repaint();
            Thread.sleep(15);
        }
    }
}
