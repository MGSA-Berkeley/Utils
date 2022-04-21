package officedraw;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class SaveData {

    private static final String tab = "\t";

    public static void save(int year, Map<Integer, Person[]> data, boolean severe) {
        saveData(year, data);
        if (!severe) {
            save(year, data.get(year));
        }
    }

    public static void save(int year, Person[] people) {
        people = Arrays.copyOf(Sorting.blockSort(people, year), people.length - 1);
        Map<String, List<String>> officeToPerson = new HashMap<>();
        Map<String, String> personToOffice = new HashMap<>();
        for (String office : Offices.offices.keySet()) {
            officeToPerson.put(office, new ArrayList<>());
        }
        for (Person person : people) {
            String name = person.buttons[0].getText();
            String office = person.buttons[5].getText();
            if (!office.isEmpty()) {
                officeToPerson.get(office).add(name);
                personToOffice.put(name, office);
            }
        }

        List<List<String>> blocks = new ArrayList<>();
        List<Integer> blocksums = new ArrayList<>();
        List<String> block = new ArrayList<>();
        String oldblock = null;
        int len = 0;
        for (Person p : people) {
            String newblock = Sorting.block(p);
            if (!newblock.equals(oldblock)) {
                if (oldblock != null) {
                    blocks.add(block);
                }
                blocksums.add(0);
                block = new ArrayList<>();
                oldblock = newblock;
                len++;
            }
            blocksums.set(len - 1, blocksums.get(len - 1) + Integer.parseInt(p.buttons[2].getText()));
            block.add(p.buttons[0].getText());
        }
        blocks.add(block);
        List<BigFraction> priorities = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            priorities.add(new BigFraction(blocksums.get(i), blocks.get(i).size()));
        }
        try {
            images(year, officeToPerson);
            saveHtml(year, len, blocks, priorities, personToOffice, officeToPerson);
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
            ex.printStackTrace();
        }
    }

    private static void saveData(int year, Map<Integer, Person[]> data) {
        List<String> lines = new ArrayList<>();
        for (Person p : Sorting.nameSort(data.get(year))) {
            lines.add(p.buttons[0].getText() + tab + p.buttons[1].getText() + tab + p.buttons[2].getText() + tab + p.buttons[3].getText() + tab + p.buttons[4].getText() + tab + p.buttons[5].getText());
        }
        Path file = Paths.get(year + ".officedraw");
        try {
            Files.write(file, lines);
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    private static void drawCenteredString(Graphics g, String text, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics();
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
    }

    private static void images(int year, Map<String, List<String>> officeToPerson) throws IOException {
        BufferedImage[] images = new BufferedImage[4];
        Graphics2D[] graphics = new Graphics2D[4];
        for (int i = 0; i < 4; i++) {
            BufferedImage img = ImageIO.read(new File("floor" + (i + 7) + ".png"));
            images[i] = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    images[i].setRGB(x, y, img.getRGB(x, y));
                }
            }
            graphics[i] = images[i].createGraphics();
        }
        for (String office : Offices.offices.keySet()) {
            int size = Offices.offices.get(office);
            int amt = officeToPerson.get(office).size();
            Polygon p = Offices.polygons.get(office);
            Rectangle r = p.getBounds();
            int i = (Integer.parseInt(office) / 100) - 7;
            if (amt == 0) {
                graphics[i].setColor(new Color(255, 255, 240));
            } else if (amt < size) {
                graphics[i].setColor(new Color(255, 255, 240));
            } else {
                graphics[i].setColor(new Color(217, 217, 217));
            }
            graphics[i].fillPolygon(p);
            graphics[i].setColor(Color.BLACK);
            graphics[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
            drawCenteredString(graphics[i], amt + "/" + size, r);
            graphics[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            int x = r.x + r.width - graphics[i].getFontMetrics().stringWidth(office) - 1;
            int y = r.y + r.height - 2;
            graphics[i].drawString(office, x, y);
        }
        for (int i = 0; i < 4; i++) {
            ImageIO.write(images[i], "png", new File("C:\\Users\\thoma\\mgsa\\officedraw\\" + year + "\\floor" + (i + 7) + ".png"));
        }
    }

    private static void saveHtml(int year, int numblocks, List<List<String>> blocks, List<BigFraction> priorities,
            Map<String, String> personToOffice, Map<String, List<String>> officeToPerson) throws IOException {
        List<String> sb = new ArrayList<>();
        sb.add("<html>");
        sb.add("<head>");
        sb.add("<title>" + year + " MGSA Office Draw</title>");
        sb.add("<style>");
        sb.add("body {font-family: sans-serif; background-color: #003262;}");
        sb.add(".row {white-space: nowrap;}");
        sb.add(".column {display: inline-block;}");
        sb.add(".left {float: left;}");
        sb.add(".right {padding: 10px;}");
        sb.add(".boxed1 {background-color: #DDD5C7; border: 2px solid black; padding: 4px; margin: 4px; font-size: 18px;}");
        sb.add(".boxed2 {background-color: #CFDD45; border: 2px solid black; padding: 4px; margin: 4px; font-size: 18px;}");
        sb.add(".boxed3 {background-color: #D9661F; border: 2px solid black; padding: 4px; margin: 4px; font-size: 18px;}");
        sb.add(".tooltip {position: relative;}");
        sb.add(".tooltiptext {visibility: hidden; white-space: nowrap; background-color: FDB515; color: #000; border-radius: 6px; padding: 5px; position: absolute; z-index: 1; top: 50%; -ms-transform: translateY(-14px); transform: translateY(-14px); left: 110%;}");
        sb.add(".tooltiptext::after {content: \"\"; position: absolute; top: 14px; right: 100%; margin-top: -6px; border-width: 6px; border-style: solid; border-color: transparent FDB515 transparent transparent;}");
        sb.add(".tooltip:hover .tooltiptext {visibility: visible;}");
        sb.add(".container {width: 1160px; height: 730px; position: relative; margin: 0 auto;}");
        sb.add("</style>");
        sb.add("</head>");
        sb.add("<body>");
        sb.add("<h1 style=\"text-align:center;color:#FDB515;\">" + year + " MGSA Office Draw</h1>");
        sb.add("<h2 style=\"text-align:center;color:#FDB515;\">You can hover over names and offices for more information</h2>");
        sb.add("<div class=\"row\">");
        sb.add("<div class=\"column left\" style=\"background-color:#FDB515;\">");
        sb.add("<h2 style=\"text-align:center;\">Draw Order</h2>");
        for (int blocknum = 0; blocknum < numblocks; blocknum++) {
            List<String> block = blocks.get(blocknum);
            int amt = 0;
            for (String person : block) {
                if (personToOffice.containsKey(person)) {
                    amt++;
                }
            }
            if (amt == block.size()) {
                sb.add("<div class=\"boxed2\">");
            } else if (amt == 0) {
                sb.add("<div class=\"boxed1\">");
            } else {
                sb.add("<div class=\"boxed3\">");
            }
            sb.add("(TIME) (" + priorities.get(blocknum) + ")");
            for (String person : block) {
                if (personToOffice.containsKey(person)) {
                    sb.add("<div class=\"tooltip\">" + person);
                    printOffice(sb, officeToPerson, personToOffice.get(person));
                    sb.add("</div>");
                } else {
                    sb.add("<div class=\"tooltip\">" + person + "<span class=\"tooltiptext\">No Office</span></div>");
                }
            }
            sb.add("</div>");
        }
        sb.add("</div>");
        sb.add("<div class=\"column right\" style=\"background-color: #3B7EA1;\">");
        sb.add("<h2 style=\"text-align: center; color: #DDD5C7;\">Available Offices</h2>");
        for (int i = 0; i < 4; i++) {
            sb.add("<div class=\"container\">");
            sb.add("<p style=\"text-align:center;\"><img src=\"floor" + (i + 7) + ".png?time=" + System.currentTimeMillis() + "\" usemap=\"#map" + (i + 7) + "\"></p>");
            for (String office : Offices.offices.keySet()) {
                if (Integer.parseInt(office) / 100 == i + 7) {
                    Rectangle rect = Offices.polygons.get(office).getBounds();
                    sb.add("<div class=\"tooltip\" style=\"position: absolute; left: " + rect.x + "px; top: " + rect.y + "px; width: " + rect.width + "; height: " + rect.height + ";\">");
                    printOffice(sb, officeToPerson, office);
                    sb.add("</div>");
                }
            }
            sb.add("</div>");
        }
        sb.add("</div>");
        sb.add("</div>");
        sb.add("</body>");
        sb.add("</html>");
        Path file = Paths.get("C:\\Users\\thoma\\mgsa\\officedraw\\" + year + "\\officedraw.html");
        Files.write(file, sb, StandardCharsets.UTF_8);
    }

    private static void printOffice(List<String> sb, Map<String, List<String>> offices, String office) {
        sb.add("<span class=\"tooltiptext\">Office " + office + " (" + offices.get(office).size() + "/" + Offices.offices.get(office) + ")");
        for (String q : offices.get(office)) {
            sb.add("<br>" + q);
        }
        sb.add("</span>");
    }
}
