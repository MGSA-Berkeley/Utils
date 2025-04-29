package officedraw;

import java.awt.Polygon;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Offices {

    public static final Map<String, Integer> offices = new HashMap<>();
    public static final Map<Integer, Map<String, Integer>> capacitymap = new HashMap<>();
    public static final Map<String, Polygon> polygons = new HashMap<>();

    public static void init() {
        try {
            String datafolder = "officedraw" + File.separator + "data" + File.separator;
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

        addOffice("710", 2, 632, 489, 681, 559);
        addOffice("716", 2, 427, 489, 476, 559);
        addOffice("737", 3, 89, 417, 169, 466);
        addOffice("739", 4, 69, 366, 169, 415);
        addOffice("741", 4, 69, 315, 169, 364);
        addOffice("743", 3, 89, 264, 169, 313);
        addOffice("745", 3, 89, 212, 169, 262);
        addOffice("747", 4, 69, 161, 169, 210);
        addOffice("775", 3, 734, 59, 783, 139);
        addOffice("787", 4, 888, 161, 988, 210);
        addOffice("789", 3, 888, 212, 968, 262);
        addOffice("810", 2, 632, 489, 681, 559);
        addOffice("812", 2, 581, 489, 630, 559);
        addOffice("814", 2, 529, 489, 579, 559);
        addOffice("816", 2, 478, 489, 527, 559);
        addOffice("818", 2, 427, 489, 476, 559);
        addOffice("820", 2, 376, 489, 425, 559);
        addOffice("822", 2, 325, 489, 374, 559);
        addOffice("826", 3, new Polygon(new int[]{374, 374, 304, 304, 325, 325}, new int[]{487, 417, 417, 456, 456, 487}, 6));
        addOffice("824", 3, 273, 458, 323, 559);
        addOffice("828", 2, 376, 417, 425, 487);
        addOffice("830", 2, 427, 417, 476, 487);
        addOffice("832", 2, 478, 417, 527, 487);
        addOffice("834", 2, 529, 417, 579, 487);
        addOffice("835", 3, 89, 468, 169, 518);
        addOffice("836", 2, 581, 417, 630, 487);
        addOffice("840", 4, 632, 284, 681, 384);
        addOffice("842", 4, 581, 284, 630, 384);
        addOffice("844", 4, 529, 284, 579, 384);
        addOffice("845", 3, 89, 212, 169, 262);
        addOffice("848", 4, 427, 284, 476, 384);
        addOffice("850", 4, 376, 284, 425, 384);
        addOffice("852", 4, 325, 284, 374, 384);
        addOffice("853", 4, 171, 38, 220, 139);
        addOffice("854", 4, 263, 284, 323, 364);
        addOffice("869", 4, 581, 38, 630, 139);
        addOffice("935", 3, 89, 468, 169, 518);
        addOffice("937", 3, 89, 417, 169, 466);
        addOffice("941", 3, 89, 264, 169, 313);
        addOffice("1004", 2, 621, 274, 681, 323);
        addOffice("1006", 2, 621, 325, 681, 374);
        addOffice("1008", 2, 621, 376, 681, 425);
        addOffice("1010", 2, 621, 427, 681, 487);
        addOffice("1020", 2, 304, 427, 364, 487);
        addOffice("1037", 3, 89, 468, 169, 518);
        addOffice("1039", 3, 89, 417, 169, 466);
        addOffice("1040", 2, 304, 376, 364, 425);
        addOffice("1041", 3, 89, 366, 169, 415);
        addOffice("1042", 3, 284, 325, 364, 374);
        addOffice("1043", 3, 89, 315, 169, 364);
        addOffice("1044", 3, 263, 274, 364, 323);
        addOffice("1045", 3, 89, 264, 169, 313);
        addOffice("1047", 3, 89, 212, 169, 262);
        addOffice("1049", 3, 89, 161, 169, 210);
        addOffice("1056", 2, 284, 171, 364, 221);
        addOffice("1057", 3, 314, 90, 384, 139);
        addOffice("1058", 3, 263, 223, 364, 272);
        addOffice("1060", 3, 366, 171, 415, 231);
        addOffice("1061", 3, 386, 90, 456, 139);
        addOffice("1062", 3, 417, 171, 466, 231);
        addOffice("1064", 3, 468, 171, 517, 231);
        addOffice("1065", 3, 529, 90, 599, 139);
        addOffice("1066", 3, 519, 171, 568, 231);
        addOffice("1068", 3, 570, 171, 619, 231);
        addOffice("1070", 2, 621, 171, 681, 221);
        addOffice("1075", 3, 816, 90, 886, 139);
        addOffice("1085", 3, 888, 212, 968, 262);
        addOffice("1087", 3, 888, 264, 968, 313);
        addOffice("1093", 3, 888, 417, 968, 466);
        addOffice("1095", 3, 888, 468, 968, 518);
        addOffice("1097", 3, 888, 520, 968, 569);
    }

    private static void addOffice(String office, int capacity, int x1, int y1, int x2, int y2) {
        addOffice(office, capacity, new Polygon(new int[]{x2, x2, x1, x1}, new int[]{y2, y1, y1, y2}, 4));
    }

    private static void addOffice(String office, int capacity, Polygon boundary) {
        offices.put(office, capacity);
        polygons.put(office, boundary);
    }
}
