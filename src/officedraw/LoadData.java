package officedraw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadData {

    private static final String tab = "\t";

    public static Map<Integer, Person[]> load() {
        Map<Integer, Person[]> data = new HashMap<>();
        try {
            Files.list(Paths.get("")).filter(p -> p.toString().matches("^\\d+\\.officedraw$")).forEach(p -> {
                try {
                    List<String> lines;
                    lines = Files.readAllLines(p);
                    int len = lines.size();
                    Person[] people = new Person[len];
                    for (int i = 0; i < len; i++) {
                        people[i] = new Person(split(lines.get(i), tab));
                    }
                    data.put(Integer.parseInt(p.toString().substring(0, p.toString().length() - 11)), people);
                } catch (Exception ex) {
                    System.out.println("Error in file "+p);
                    ex.printStackTrace(System.out);
                    System.exit(0);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            System.exit(0);
        }
        if (data.isEmpty()) {
            data.put(Calendar.getInstance().get(Calendar.YEAR), new Person[]{new Person()});
        }
        return data;
    }

    private static String[] split(String s, String t) {
        List<String> list = new ArrayList<>();
        int i = 0;
        while (true) {
            int j = s.indexOf(t, i);
            if (j == -1) {
                break;
            }
            list.add(s.substring(i, j));
            i = j + 1;
        }
        list.add(s.substring(i));
        return list.toArray(new String[list.size()]);
    }
}
