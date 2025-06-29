package mgsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MGSA {

    public static void main(String[] args) throws InterruptedException, IOException {
        if (true) {
            List<String[]> data = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\thoma\\Downloads\\Temp Data - officedraw(3).csv"));
            while (true) {
                String s = reader.readLine();
                if (s == null) {
                    break;
                }
                data.add(s.split(","));
            }
            for (int year = 2006; year <= 2017; year++) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(year + ".officedraw"));
                System.out.println(year);
                List<String> names = new ArrayList<>();
                for (String[] a : data) {
                    if (a[4].equals(year + "")) {
                        String name = a[1] + " " + a[2];
                        if (names.contains(name)) {
                            System.out.println("eek! " + year + " " + name);
                        }
                        names.add(name);
                    }
                }
                Collections.sort(names);
                for (String name : names) {
                    String[] d = null;
                    for (String[] a : data) {
                        if (a[4].equals(year + "") && name.equals(a[1] + " " + a[2])) {
                            d = a;
                        }
                    }
                    if (d == null) {
                        throw new IllegalArgumentException();
                    }
                    int y = Integer.parseInt(d[4]) - Integer.parseInt(d[3]) + 1;
                    int p = d[7].equals("NULL") ? -37 : Integer.parseInt(d[7]);
                    int should = 0;
                    if (y <= 5) {
                        should = 6 - y;
                    } else if (y == 6) {
                        should = 1;
                    } else if (y == 7) {
                        should = 3;
                    } else {
                        should = 5;
                    }
                    if (y == 1) {
                        should = 6;
                    }
                    if (p == -37) {
                        p = should;
                    }
                    if (y == 1) {
                        p = should;
                    }
                    int adj = 0;
                    if (d[5].equals("1")) {
                        p = 0;
                        if (name.equals("Rahul Dalal")) {
                            System.out.println(d[5].equals("1") + " " + Arrays.toString(d));
                        }
                    }
                    adj = p - should;
                    String type = "";
                    boolean floating = d[9].equals("NULL");
                    boolean squatting = d[10].equals("1");
                    if (floating && squatting) {
                        throw new IllegalArgumentException();
                    }
                    String office = d[13];
                    if (office.equals("NULL") || office.equals("Null")) {
                        // I think these people are graduating
                        continue;
                    }
                    if (floating) {
                        type = "Float";
                    } else if (squatting) {
                        type = "Squat";
                    } else {
                        type = "Block " + d[9];
                    }
                    String TAB = "\t";
                    String s = name + TAB + y + TAB + p + TAB + (adj != 0 ? adj : "") + TAB + type + TAB + office;
                    writer.write(s);
                    writer.newLine();
                }
                writer.close();
            }
        }

        officedraw.Offices.init();
        MainJFrame frame = new MainJFrame();
        while (true) {
            frame.repaint();
            Thread.sleep(15);
        }
    }
}
