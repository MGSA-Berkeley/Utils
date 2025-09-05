package officedraw;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class LoadData {

    private static final char TAB = '\t';
    private static final String LOCAL_FOLDER = "officedraw-local" + File.separator;
    private static final String REGEX_LOCAL_FOLDER = Pattern.quote(LOCAL_FOLDER);
    private static final int LOCAL_FOLDER_LENGTH = LOCAL_FOLDER.length();

    public static Map<Integer, Person[]> load() {
        Map<Integer, Person[]> data = new HashMap<>();
        try {
            Files.list(Paths.get(LOCAL_FOLDER)).filter(p -> p.toString().matches("^" + REGEX_LOCAL_FOLDER + "\\d+\\.officedraw$")).forEach(p -> {
                try {
                    List<String> lines;
                    lines = Files.readAllLines(p);
                    int len = lines.size();
                    Person[] people = new Person[len];
                    for (int i = 0; i < len; i++) {
                        people[i] = new Person(split(lines.get(i), TAB));
                    }
                    if (len == 0 || !people[len - 1].blank()) {
                        people = Arrays.copyOf(people, len + 1);
                        people[len] = new Person();
                    }
                    data.put(Integer.parseInt(p.toString().substring(LOCAL_FOLDER_LENGTH, p.toString().length() - 11)), people);
                } catch (IOException | NumberFormatException ex) {
                    System.out.println("Error in file " + p);
                    ex.printStackTrace(System.out);
                    System.exit(0);
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            System.exit(0);
        }
        if (data.isEmpty()) {
            data.put(Calendar.getInstance().get(Calendar.YEAR), new Person[]{new Person()});
        }
        return data;
    }

    private static String[] split(String s, char t) {
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
        return list.toArray(new String[0]);
    }
}
