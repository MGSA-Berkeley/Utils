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
import java.io.FileReader;
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
import static officedraw.Offices.capacitymap;

public class SaveData {

    private static final String TAB = "\t";

    public static void save(int year, Map<Integer, Person[]> data, boolean severe) {
        saveData(year, data.get(year));
        if (!severe) {
            saveJson(year, data.get(year));
        }
    }

    private static void saveJson(int year, Person[] data) {
        Person[] people = Arrays.copyOf(Sorting.blockSort(data, year), data.length - 1);
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
        int index = 0;
        Map<Person, Integer> indices = new HashMap<>();
        List<List<String>> blocks = new ArrayList<>();
        List<BigFraction> blocksums = new ArrayList<>();
        List<String> block = new ArrayList<>();
        String oldblock = null;
        List<Long> times = new ArrayList<>();
        int len = 0;
        int amt = 0;
        for (int i = 0; i < people.length; i++) {
            Person p = people[i];
            String newblock = Sorting.block(p);
            if (!newblock.equals(oldblock)) {
                if (oldblock != null) {
                    blocks.add(block);
                    index++;
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
            String name = p.buttons[0].getText();
            block.add(name);
            indices.put(p, index);
        }
        blocks.add(block);
        index++;
        List<BigFraction> priorities = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            priorities.add(blocksums.get(i).divide(new BigFraction(blocks.get(i).size())));
        }
        try {
            String datafolder = "officedraw" + File.separator + "data" + File.separator;
            String datafile = datafolder + "data.json";
            List<Object> yearlist = (List) JsonParser.parse(new FileReader(datafile));
            for (int i = 0; i < yearlist.size(); i++) {
                Map<String, Object> yearmap = (Map) yearlist.get(i);
                if (Integer.parseInt(yearmap.get("year").toString()) != year) {
                    continue;
                }
                long base = (Long) yearmap.get("start");
                long delta = (Long) yearmap.get("delta");
                long numslots = (Long) yearmap.get("timeslots");
                int pos = 0;
                for (int j = 0; j < len; j++) {
                    if (times.get(j) == -1) {
                        times.set(j, base + pos * numslots / amt * delta);
                        pos++;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        List<String> lines = new ArrayList<>();
        lines.add("[");
        people = Arrays.copyOf(Sorting.nameSort(data), data.length - 1);
        for (int i = 0; i < people.length; i++) {
            Person p = people[i];
            String name = p.buttons[0].getText();
            index = indices.get(p);
            lines.add("    {");
            lines.add("        \"name\": \"" + name + "\",");
            lines.add("        \"priority\": \"" + priorities.get(index) + "\",");
            lines.add("        \"index\": \"" + index + "\",");
            lines.add("        \"time\": \"" + times.get(index) + "\",");
            lines.add("        \"office\": \"" + p.buttons[5].getText() + "\"");
            lines.add(i == people.length - 1 ? "    }" : "    },");
        }
        lines.add("]");
        // todo: harvest file name from data.json
        Path file = Paths.get("officedraw" + File.separator + "data" + File.separator + "people" + year + ".json");
        try {
            Files.write(file, lines);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void saveData(int year, Person[] data) {
        List<String> lines = new ArrayList<>();
        for (Person p : Sorting.nameSort(data)) {
            lines.add(p.buttons[0].getText() + TAB + p.buttons[1].getText() + TAB + p.buttons[2].getText() + TAB + p.buttons[3].getText() + TAB + p.buttons[4].getText() + TAB + p.buttons[5].getText());
        }
        Path file = Paths.get(year + ".officedraw");
        try {
            Files.write(file, lines);
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }
}
