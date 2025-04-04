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
        // temp code to generate evans json file
        Map<String, BufferedImage> floorplans = new HashMap<>();
        floorplans.put("7", ImageIO.read(new File("data\\evans7.png")));
        floorplans.put("8", ImageIO.read(new File("data\\evans8.png")));
        floorplans.put("9", ImageIO.read(new File("data\\evans9.png")));
        floorplans.put("10", ImageIO.read(new File("data\\evans10.png")));
        List<String> offices = new ArrayList<>();
        offices.addAll(officedraw.Offices.offices.keySet());
        Collections.sort(offices);
        while (offices.get(0).charAt(0) == '1') {
            offices.add(offices.remove(0));
        }
        for (String s : offices) {
            System.out.println("        {");
            String capacity = officedraw.Offices.offices.get(s).toString();
            System.out.println("            \"floor\": \"" + s.substring(0, s.length() - 2) + "\",");
            System.out.println("            \"number\": \"" + s + "\",");
            System.out.println("            \"capacity\": " + capacity + ",");
            int len = officedraw.Offices.polygons.get(s).xpoints.length;
            int[] xpoints = Arrays.copyOf(officedraw.Offices.polygons.get(s).xpoints, len);
            int[] ypoints = Arrays.copyOf(officedraw.Offices.polygons.get(s).ypoints, len);
            loop:
            for (int i = 0; i < len; i++) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (test(floorplans.get(s.substring(0, s.length() - 2)), xpoints[i] + dx, ypoints[i] + dy)) {
                            xpoints[i] += dx;
                            ypoints[i] += dy;
                            continue loop;
                        }
                    }
                }
                throw new IllegalArgumentException();
            }
            System.out.println("            \"xpoints\": " + Arrays.toString(xpoints) + ",");
            System.out.println("            \"ypoints\": " + Arrays.toString(ypoints));
            System.out.println("        },");
        }
        System.out.println("------------------------------------");
        for (String s : offices) {
            System.out.println("        \""+s+"\",");
        }
        // end temp code
        MainJFrame frame = new MainJFrame();
        while (true) {
            frame.repaint();
            Thread.sleep(15);
        }
    }
}
