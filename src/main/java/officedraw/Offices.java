package officedraw;

import java.awt.Polygon;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Offices {

    public static final Map<Integer, Map<String, Integer>> capacitymap = new HashMap<>();
    public static final Map<String, Polygon> polygons = new HashMap<>();

    public static void init() {
        try {
            String datafolder = "officedraw-website" + File.separator + "data" + File.separator;
            String datafile = datafolder + "data.json";
            List<Object> yearlist = (List) JsonParser.parse(new FileReader(datafile));
            for (int i = 0; i < yearlist.size(); i++) {
                Map<String, Object> yearmap = (Map) yearlist.get(i);
                int year = Integer.parseInt(yearmap.get("year").toString());
                String floorfile = datafolder + yearmap.get("floorplan");
                Map<String, Object> floormap = (Map) JsonParser.parse(new FileReader(floorfile));
                List<Object> officelist = (List) floormap.get("offices");
                Map<String, Integer> capacities = new HashMap<>();
                for (int j = 0; j < officelist.size(); j++) {
                    Map<String, Object> officemap = (Map) officelist.get(j);
                    String number = (String) officemap.get("number");
                    Integer capacity = Integer.parseInt(officemap.get("capacity").toString());
                    capacities.put(number, capacity);
                }
                String activefile = datafolder + yearmap.get("activeoffices");
                List<Object> activelist = (List) JsonParser.parse(new FileReader(activefile));
                capacitymap.put(year, new HashMap<>());
                for (int j = 0; j < activelist.size(); j++) {
                    String number = (String) activelist.get(j);
                    Integer capacity = capacities.get(number);
                    capacitymap.get(year).put(number, capacity);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
