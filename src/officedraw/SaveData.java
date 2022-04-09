package officedraw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SaveData {

    private static final String tab = "\t";

    public static void save(Map<Integer, Person[]> map) {
        try {
            for (int year : map.keySet()) {
                List<String> lines = new ArrayList<>();
                for (Person p : map.get(year)) {
                    lines.add(p.buttons[0].getText() + tab + p.buttons[1].getText() + tab + p.buttons[2].getText() + tab + p.buttons[3].getText() + tab + p.buttons[4].getText());
                }
                Path file = Paths.get(year + ".officedraw");
                Files.write(file, lines);
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }
}
