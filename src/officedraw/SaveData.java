package officedraw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SaveData {

    private static final String tab = "\t";

    public static void save(Map<Integer, Person[]> map) {
        try {
            for (int year : map.keySet()) {
                Person[] oldpeople = map.get(year);
                int len = oldpeople.length - 1;
                Person[] sortedpeople = Arrays.copyOf(map.get(year), len);
                Arrays.sort(sortedpeople, (p, q) -> p.buttons[0].getText().compareTo(q.buttons[0].getText()));
                Person[] newpeople = Arrays.copyOf(sortedpeople, len + 1);
                newpeople[len] = new Person();
                List<String> lines = new ArrayList<>();
                for (Person p : newpeople) {
                    lines.add(p.buttons[0].getText() + tab + p.buttons[1].getText() + tab + p.buttons[2].getText() + tab + p.buttons[3].getText() + tab + p.buttons[4].getText() + tab + p.buttons[5].getText());
                }
                Path file = Paths.get(year + ".officedraw");
                Files.write(file, lines);
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }
}
