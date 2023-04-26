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
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class SaveData {

    private static final String TAB = "\t";

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
        List<BigFraction> blocksums = new ArrayList<>();
        List<String> block = new ArrayList<>();
        String oldblock = null;
        List<Long> times = new ArrayList<>();
        int len = 0;
        int amt = 0;
        for (Person p : people) {
            String newblock = Sorting.block(p);
            if (!newblock.equals(oldblock)) {
                if (oldblock != null) {
                    blocks.add(block);
                }
                blocksums.add(BigFraction.ZERO);
                block = new ArrayList<>();
                oldblock = newblock;
                if (newblock.startsWith("Squat")) {
                    times.add(0L);
                } else {
                    times.add(-1L);
                    amt++;
                }
                len++;
            }
            blocksums.set(len - 1, blocksums.get(len - 1).add(new BigFraction(p.buttons[2].getText())));
            block.add(p.buttons[0].getText());
        }
        blocks.add(block);
        List<BigFraction> priorities = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            priorities.add(blocksums.get(i).divide(new BigFraction(blocks.get(i).size())));
        }
        if (year == 2022) {
            long base = 1651258800000L;
            long delta = 900000L;
            int numslots = 12;
            int pos = 0;
            for (int i = 0; i < len; i++) {
                if (times.get(i) == -1) {
                    times.set(i, base + pos * numslots / amt * delta);
                    pos++;
                }
            }
        } else if (year == 2023) {
            long base = 1682708400000L;
            long delta = 900000L;
            int numslots = 12;
            int pos = 0;
            for (int i = 0; i < len; i++) {
                if (times.get(i) == -1) {
                    times.set(i, base + pos * numslots / amt * delta);
                    pos++;
                }
            }
        }
        try {
            // Hacky edge case, delete after the office draw
            if (year == 2023) {
                int pos = 33;
                String name = "Yifan Chen";
                long time = 1682710200000L;
                String office = null;
                List<String> badblock = new ArrayList<>();
                badblock.add(name);
                blocks.add(pos, badblock);
                priorities.add(pos, new BigFraction(2));
                times.add(pos, time);
                if (office != null) {
                    personToOffice.put(name, office);
                    officeToPerson.get(office).add(name);
                }
                len++;
            }
            images(year, officeToPerson);
            saveHtml(year, len, blocks, priorities, times, personToOffice, officeToPerson);
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
            ex.printStackTrace(System.out);
        }
    }

    private static void saveData(int year, Map<Integer, Person[]> data) {
        List<String> lines = new ArrayList<>();
        for (Person p : Sorting.nameSort(data.get(year))) {
            lines.add(p.buttons[0].getText() + TAB + p.buttons[1].getText() + TAB + p.buttons[2].getText() + TAB + p.buttons[3].getText() + TAB + p.buttons[4].getText() + TAB + p.buttons[5].getText());
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
                graphics[i].setColor(mgsa.GraphicsUtils.BayFog);
            } else if (amt < size) {
                graphics[i].setColor(mgsa.GraphicsUtils.BayFog);
            } else {
                graphics[i].setColor(mgsa.GraphicsUtils.WebGray);
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
        new File("C:\\Users\\thoma\\mgsa\\officedraw\\" + year).mkdirs();
        for (int i = 0; i < 4; i++) {
            ImageIO.write(images[i], "png", new File("C:\\Users\\thoma\\mgsa\\officedraw\\" + year + "\\floor-" + (((i + 6) % 9) + 1) + ".png"));
        }
    }

    private static void saveHtml(int year, int numblocks, List<List<String>> blocks, List<BigFraction> priorities, List<Long> times,
            Map<String, String> personToOffice, Map<String, List<String>> officeToPerson) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("window.blocks=[");
        for (int i = 0; i < numblocks; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append("{\"time\":");
            sb.append(times.get(i));
            sb.append(",\"priority\":\"");
            sb.append(priorities.get(i));
            sb.append("\",\"people\":[");
            for (int j = 0; j < blocks.get(i).size(); j++) {
                if (j != 0) {
                    sb.append(",");
                }
                sb.append("\"");
                sb.append(blocks.get(i).get(j));
                sb.append("\"");
            }
            sb.append("],\"done\":");
            int amt = 0;
            for (String person : blocks.get(i)) {
                if (personToOffice.containsKey(person)) {
                    amt++;
                }
            }
            sb.append(amt == blocks.get(i).size());
            sb.append("}");
        }
        sb.append("];\nwindow.officePops=[");
        List<String> offices = new ArrayList<>();
        offices.addAll(officeToPerson.keySet());
        Collections.sort(offices, (String a, String b) -> {
            int i;
            int j;
            try {
                i = Integer.parseInt(a);
                j = Integer.parseInt(b);
            } catch (NumberFormatException ex) {
                ex.printStackTrace(System.out);
                return a.compareTo(b);
            }
            return Integer.compare(i, j);
        });
        int numoffices = offices.size();
        for (int i = 0; i < numoffices; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append("{\"number\":\"");
            sb.append(offices.get(i));
            sb.append("\",\"people\":[");
            for (int j = 0; j < officeToPerson.get(offices.get(i)).size(); j++) {
                if (j != 0) {
                    sb.append(",");
                }
                sb.append("\"");
                sb.append(officeToPerson.get(offices.get(i)).get(j));
                sb.append("\"");
            }
            sb.append("]}");
        }
        sb.append("];\n");
        Path datajs = Paths.get("C:\\Users\\thoma\\mgsa\\officedraw\\" + year + "\\data.js");
        Path indexjs = Paths.get("C:\\Users\\thoma\\mgsa\\officedraw\\" + year + "\\index.js");
        Path indexphp = Paths.get("C:\\Users\\thoma\\mgsa\\officedraw\\" + year + "\\index.php");
        Path mainhtml = Paths.get("C:\\Users\\thoma\\mgsa\\officedraw\\" + year + "\\main.html");
        Path officesjs = Paths.get("C:\\Users\\thoma\\mgsa\\officedraw\\" + year + "\\offices.js");
        Path stylecss = Paths.get("C:\\Users\\thoma\\mgsa\\officedraw\\" + year + "\\style.css");
        Files.writeString(datajs, sb.toString(), StandardCharsets.UTF_8);
        Files.copy(Paths.get("index.js"), indexjs, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("index.php"), indexphp, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("main.html"), mainhtml, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("offices.js"), officesjs, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("style.css"), stylecss, StandardCopyOption.REPLACE_EXISTING);
    }
}
