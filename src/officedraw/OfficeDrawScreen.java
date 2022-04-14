package officedraw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OfficeDrawScreen implements mgsa.Screen {

    private final mgsa.MainCanvas canvas;

    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private final mgsa.Button bannerbutton = new mgsa.Button(Integer.toString(year), null);
    private final mgsa.Button leftbutton = new mgsa.Button("<", null);
    private final mgsa.Button rightbutton = new mgsa.Button(">", null);
    private final mgsa.Button backbutton = new mgsa.Button("←", null);
    private final mgsa.Button exitbutton = new mgsa.Button("X", null);
    private final mgsa.Button namebutton = new mgsa.Button("Name", null);
    private final mgsa.Button yearbutton = new mgsa.Button("Year", null);
    private final mgsa.Button prioritybutton = new mgsa.Button("Priority", null);
    private final mgsa.Button adjustmentbutton = new mgsa.Button("Adjustment", null);
    private final mgsa.Button blockbutton = new mgsa.Button("Block", null);
    private final mgsa.Button officebutton = new mgsa.Button("Office", null);
    private final mgsa.Button warningsbutton = new mgsa.Button("Warnings", null);

    private final Set<Integer> keyset = new HashSet<>();
    private final Set<String> letters = new HashSet<>();

    private Point click;

    private static final Color background = mgsa.GraphicsUtils.Grey;
    private static final Color foreground = mgsa.GraphicsUtils.Black;
    private static final Color mouseover = mgsa.GraphicsUtils.BayFog;

    private static final Font bigfont = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    private static final Font mediumfont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    private static final Font smallfont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

    private final Map<Integer, Person[]> data = LoadData.load();
    private int row = 0;
    private int column = 0;
    private int scroll;
    private boolean severe;

    private static final Map<String, Integer> offices = new HashMap<>();

    public OfficeDrawScreen(mgsa.MainCanvas canvas) {
        this.canvas = canvas;
        for (char c : "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray()) {
            letters.add(Character.toString(c));
        }
        offices.put("710", 2);
        offices.put("716", 2);
        offices.put("737", 3);
        offices.put("739", 4);
        offices.put("741", 4);
        offices.put("743", 3);
        offices.put("745", 3);
        offices.put("747", 4);
        offices.put("775", 3);
        offices.put("787", 4);
        offices.put("789", 3);
        offices.put("810", 2);
        offices.put("812", 2);
        offices.put("814", 2);
        offices.put("816", 2);
        offices.put("818", 2);
        offices.put("820", 2);
        offices.put("822", 2);
        offices.put("826", 3);
        offices.put("824", 3);
        offices.put("828", 2);
        offices.put("830", 2);
        offices.put("832", 2);
        offices.put("834", 2);
        offices.put("835", 3);
        offices.put("836", 2);
        offices.put("840", 4);
        offices.put("842", 4);
        offices.put("844", 4);
        offices.put("845", 3);
        offices.put("848", 4);
        offices.put("850", 4);
        offices.put("852", 4);
        offices.put("853", 4);
        offices.put("854", 4);
        offices.put("869", 4);
        offices.put("935", 3);
        offices.put("937", 3);
        offices.put("941", 3);
        offices.put("1004", 2);
        offices.put("1006", 2);
        offices.put("1008", 2);
        offices.put("1010", 2);
        offices.put("1020", 2);
        offices.put("1037", 3);
        offices.put("1039", 3);
        offices.put("1040", 2);
        offices.put("1041", 3);
        offices.put("1042", 3);
        offices.put("1043", 3);
        offices.put("1044", 3);
        offices.put("1045", 3);
        offices.put("1047", 3);
        offices.put("1049", 3);
        offices.put("1056", 2);
        offices.put("1057", 3);
        offices.put("1058", 3);
        offices.put("1060", 3);
        offices.put("1061", 3);
        offices.put("1062", 3);
        offices.put("1064", 3);
        offices.put("1065", 3);
        offices.put("1066", 3);
        offices.put("1068", 3);
        offices.put("1070", 2);
        offices.put("1075", 3);
        offices.put("1085", 3);
        offices.put("1087", 3);
        offices.put("1093", 3);
        offices.put("1095", 3);
        offices.put("1097", 3);
        update();
    }

    @Override
    public void paintComponent(Graphics g, int w, int h) {
        int year = this.year;
        Person[] people = data.get(year);
        Point click = this.click;
        int row = this.row;
        int column = this.column;
        int scroll = this.scroll;
        int bigpadding = 12;
        int smallpadding = 5;
        g.setFont(bigfont);
        bannerbutton.setText(Integer.toString(year));
        int bannerwidth = bannerbutton.getWidth(g, smallpadding);
        int bannerheight = bannerbutton.getHeight(g, bigpadding);
        bannerbutton.setRectCenter(g, new Point(w / 2, bannerheight / 2), smallpadding);
        leftbutton.setRectRight(g, new Point(w / 2 - bannerwidth / 2, bannerheight / 2), smallpadding);
        rightbutton.setRectLeft(g, new Point(w / 2 + bannerwidth / 2, bannerheight / 2), smallpadding);
        backbutton.setRectLeft(g, new Point(bigpadding - smallpadding, bannerheight / 2), smallpadding);
        exitbutton.setRectRight(g, new Point(w - bigpadding + smallpadding, bannerheight / 2), smallpadding);
        g.setFont(mediumfont);
        int rowheight = 0;
        int namelen = namebutton.getWidth(g, smallpadding);
        rowheight = Math.max(rowheight, namebutton.getHeight(g, smallpadding));
        int yearlen = yearbutton.getWidth(g, smallpadding);
        rowheight = Math.max(rowheight, yearbutton.getHeight(g, smallpadding));
        int prioritylen = prioritybutton.getWidth(g, smallpadding);
        rowheight = Math.max(rowheight, prioritybutton.getHeight(g, smallpadding));
        int adjustmentlen = adjustmentbutton.getWidth(g, smallpadding);
        rowheight = Math.max(rowheight, adjustmentbutton.getHeight(g, smallpadding));
        int blocklen = blockbutton.getWidth(g, smallpadding);
        rowheight = Math.max(rowheight, blockbutton.getHeight(g, smallpadding));
        int officelen = officebutton.getWidth(g, smallpadding);
        rowheight = Math.max(rowheight, officebutton.getHeight(g, smallpadding));
        g.setFont(smallfont);
        for (Person p : people) {
            namelen = Math.max(namelen, p.buttons[0].getWidth(g, smallpadding));
            yearlen = Math.max(yearlen, p.buttons[1].getWidth(g, smallpadding));
            prioritylen = Math.max(prioritylen, p.buttons[2].getWidth(g, smallpadding));
            adjustmentlen = Math.max(adjustmentlen, p.buttons[3].getWidth(g, smallpadding));
            blocklen = Math.max(blocklen, p.buttons[4].getWidth(g, smallpadding));
            officelen = Math.max(officelen, p.buttons[5].getWidth(g, smallpadding));
        }
        int namepos = 0;
        int yearpos = namepos + namelen;
        int prioritypos = yearpos + yearlen;
        int adjustmentpos = prioritypos + prioritylen;
        int blockpos = adjustmentpos + adjustmentlen;
        int officepos = blockpos + blocklen;
        int warningspos = officepos + officelen;
        int warningslen = w - warningspos;
        namebutton.setRect(new Rectangle(namepos, bannerheight, namelen, rowheight));
        yearbutton.setRect(new Rectangle(yearpos, bannerheight, yearlen, rowheight));
        prioritybutton.setRect(new Rectangle(prioritypos, bannerheight, prioritylen, rowheight));
        adjustmentbutton.setRect(new Rectangle(adjustmentpos, bannerheight, adjustmentlen, rowheight));
        blockbutton.setRect(new Rectangle(blockpos, bannerheight, blocklen, rowheight));
        officebutton.setRect(new Rectangle(officepos, bannerheight, officelen, rowheight));
        warningsbutton.setRect(new Rectangle(warningspos, bannerheight, warningslen, rowheight));
        int y = bannerheight;
        for (Person p : people) {
            y += rowheight;
            p.buttons[0].setRect(new Rectangle(namepos, y - scroll, namelen, rowheight));
            p.buttons[1].setRect(new Rectangle(yearpos, y - scroll, yearlen, rowheight));
            p.buttons[2].setRect(new Rectangle(prioritypos, y - scroll, prioritylen, rowheight));
            p.buttons[3].setRect(new Rectangle(adjustmentpos, y - scroll, adjustmentlen, rowheight));
            p.buttons[4].setRect(new Rectangle(blockpos, y - scroll, blocklen, rowheight));
            p.buttons[5].setRect(new Rectangle(officepos, y - scroll, officelen, rowheight));
            p.warning.setRect(new Rectangle(warningspos, y - scroll, warningslen, rowheight));
        }
        Point mouse = canvas.getMousePosition();
        g.setColor(background);
        g.fillRect(0, 0, w, h);
        for (int i = 0; i < people.length; i++) {
            for (int j = 0; j < 6; j++) {
                if (row == i && column == j) {
                    people[i].buttons[j].highlight(g, mouseover);
                }
                people[i].buttons[j].drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
            }
            if (row == i && column == 6) {
                people[i].warning.highlight(g, mouseover);
            }
            people[i].warning.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
        }
        for (int n = 2; n <= people.length + 1; n++) {
            g.drawLine(0, bannerheight + n * rowheight - scroll, w, bannerheight + n * rowheight - scroll);
        }
        g.setColor(background);
        g.fillRect(0, 0, w, bannerheight + rowheight);
        g.setFont(bigfont);
        bannerbutton.drawCenter(g, mouseover, foreground, mouse, click);
        leftbutton.drawCenter(g, mouseover, foreground, mouse, click);
        rightbutton.drawCenter(g, mouseover, foreground, mouse, click);
        backbutton.drawCenter(g, mouseover, foreground, mouse, click);
        exitbutton.drawCenter(g, mouseover, foreground, mouse, click);
        g.setFont(mediumfont);
        namebutton.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
        yearbutton.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
        prioritybutton.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
        adjustmentbutton.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
        blockbutton.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
        officebutton.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
        warningsbutton.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
        for (int n = 0; n < 2; n++) {
            g.drawLine(0, bannerheight + n * rowheight, w, bannerheight + n * rowheight);
        }
        g.drawLine(namepos, bannerheight, namepos, h);
        g.drawLine(yearpos, bannerheight, yearpos, h);
        g.drawLine(prioritypos, bannerheight, prioritypos, h);
        g.drawLine(adjustmentpos, bannerheight, adjustmentpos, h);
        g.drawLine(blockpos, bannerheight, blockpos, h);
        g.drawLine(officepos, bannerheight, officepos, h);
        g.drawLine(warningspos, bannerheight, warningspos, h);
        g.drawLine(w - 1, bannerheight, w - 1, h);
        g.drawLine(0, h - 1, w, h - 1);
    }

    @Override
    public void mousePressed() {
        click = canvas.getMousePosition();
    }

    @Override
    public void mouseReleased() {
        Point p = canvas.getMousePosition();
        if (bannerbutton.contains(p) && bannerbutton.contains(click)) {
            year = Calendar.getInstance().get(Calendar.YEAR);
        }
        if (leftbutton.contains(p) && leftbutton.contains(click)) {
            if (!data.containsKey(year - 1)) {
                data.put(year - 1, new Person[]{new Person()});
            }
            row = 0;
            column = 0;
            scroll = 0;
            year--;
            update();
        }
        if (rightbutton.contains(p) && rightbutton.contains(click)) {
            if (!data.containsKey(year + 1)) {
                data.put(year + 1, new Person[]{new Person()});
            }
            row = 0;
            column = 0;
            scroll = 0;
            year++;
            update();
        }
        if (backbutton.contains(p) && backbutton.contains(click)) {
            canvas.setScreen(new mgsa.HomeScreen(canvas));
        }
        if (exitbutton.contains(p) && exitbutton.contains(click)) {
            System.exit(0);
        }
        click = null;
    }

    @Override
    public void mouseScrolled(int n) {
        n *= 6;
        if (scroll + n < 0) {
            scroll = 0;
        } else {
            scroll += n;
        }
    }

    @Override
    public void keyPressed(int key) {
        keyset.add(key);
        if (keyset.contains(KeyEvent.VK_CONTROL) && key == KeyEvent.VK_Q) {
            System.exit(0);
        }
        if (keyset.contains(KeyEvent.VK_CONTROL) && key == KeyEvent.VK_S) {
            SaveData.save(data);
            return;
        }
        if (key == KeyEvent.VK_UP) {
            if (row > 0) {
                row--;
            }
            return;
        }
        if (key == KeyEvent.VK_DOWN) {
            if (row < data.get(year).length - 1) {
                row++;
            }
            return;
        }
        if (key == KeyEvent.VK_LEFT) {
            if (column > 0) {
                column--;
            }
            return;
        }
        if (key == KeyEvent.VK_RIGHT) {
            if (column < 6) {
                column++;
            }
            return;
        }
        if (column < 6) {
            Person[] people = data.get(year);
            mgsa.Button button = people[row].buttons[column];
            boolean append = false;
            String s = KeyEvent.getKeyText(key);
            if (key == KeyEvent.VK_BACK_SPACE) {
                String text = button.getText();
                if (!text.isEmpty()) {
                    button.setText(text.substring(0, text.length() - 1));
                    boolean delete = true;
                    for (mgsa.Button b : people[row].buttons) {
                        if (!b.getText().isEmpty()) {
                            delete = false;
                            break;
                        }
                    }
                    if (delete) {
                        Person[] newpeople = new Person[people.length - 1];
                        System.arraycopy(people, 0, newpeople, 0, row);
                        System.arraycopy(people, row + 1, newpeople, row, newpeople.length - row);
                        data.put(year, newpeople);
                    }
                }
            } else if (key == KeyEvent.VK_SPACE) {
                button.setText(button.getText() + " ");
                append = true;
            } else if (key == KeyEvent.VK_MINUS) {
                button.setText(button.getText() + "-");
                append = true;
            } else if (letters.contains(s)) {
                button.setText(button.getText() + (keyset.contains(KeyEvent.VK_SHIFT) ? s : s.toLowerCase()));
                append = true;
            } else {
                return;
            }
            if (append && row == people.length - 1) {
                people = Arrays.copyOf(people, people.length + 1);
                people[people.length - 1] = new Person();
                data.put(year, people);
            }
            update();
        }
    }

    @Override
    public void keyReleased(int key) {
        keyset.remove(key);
    }

    private void update() {
        Set<Integer> yearset = data.keySet();
        int numyears = yearset.size();
        int[] years = new int[numyears];
        int thisyear = 0;
        for (int year : yearset) {
            years[thisyear++] = year;
        }
        for (int year = 0; year < numyears; year++) {
            if (years[year] == this.year) {
                thisyear = year;
            }
        }
        Arrays.sort(years);
        int[] numpeople = new int[numyears];
        Person[][] people = new Person[numyears][];
        String[][] namestrings = new String[numyears][];
        String[][] yearstrings = new String[numyears][];
        String[][] prioritystrings = new String[numyears][];
        String[][] adjustmentstrings = new String[numyears][];
        String[][] blockstrings = new String[numyears][];
        String[][] officestrings = new String[numyears][];
        for (int year = 0; year < numyears; year++) {
            people[year] = data.get(years[year]);
            numpeople[year] = people[year].length - 1;
            namestrings[year] = new String[numpeople[year]];
            yearstrings[year] = new String[numpeople[year]];
            prioritystrings[year] = new String[numpeople[year]];
            adjustmentstrings[year] = new String[numpeople[year]];
            blockstrings[year] = new String[numpeople[year]];
            officestrings[year] = new String[numpeople[year]];
            for (int person = 0; person < numpeople[year]; person++) {
                namestrings[year][person] = people[year][person].buttons[0].getText();
                yearstrings[year][person] = people[year][person].buttons[1].getText();
                prioritystrings[year][person] = people[year][person].buttons[2].getText();
                adjustmentstrings[year][person] = people[year][person].buttons[3].getText();
                blockstrings[year][person] = people[year][person].buttons[4].getText();
                officestrings[year][person] = people[year][person].buttons[5].getText();
            }
        }
        List<Integer> badyears = new ArrayList<>();
        for (int year = 0; year < numyears; year++) {
            Set<String> names = new HashSet<>();
            for (int person = 0; person < numpeople[year]; person++) {
                String namestring = namestrings[year][person];
                String yearstring = yearstrings[year][person];
                String prioritystring = prioritystrings[year][person];
                String blockstring = blockstrings[year][person];
                String officestring = officestrings[year][person];
                if (names.contains(namestring)) {
                    badyears.add(years[year]);
                    break;
                }
                names.add(namestring);
                try {
                    Integer.parseInt(yearstring);
                } catch (NumberFormatException ex) {
                    badyears.add(years[year]);
                    break;
                }
                try {
                    Integer.parseInt(prioritystring);
                } catch (NumberFormatException ex) {
                    badyears.add(years[year]);
                    break;
                }
                if (!(blockstring.equals("Squat") || blockstring.equals("Float") || blockstring.startsWith("Block"))) {
                    badyears.add(years[year]);
                    break;
                }
                if (!(officestring.isEmpty() || offices.containsKey(officestring))) {
                    badyears.add(years[year]);
                    break;
                }
            }
        }
        Set<String> names = new HashSet<>();
        Set<String> duplicatenames = new HashSet<>();
        for (String name : namestrings[thisyear]) {
            if (names.contains(name)) {
                duplicatenames.add(name);
            }
            names.add(name);
        }
        for (int person = 0; person < numpeople[thisyear]; person++) {
            String warning = "";
            String namestring = namestrings[thisyear][person];
            String yearstring = yearstrings[thisyear][person];
            String prioritystring = prioritystrings[thisyear][person];
            String blockstring = blockstrings[thisyear][person];
            String officestring = officestrings[thisyear][person];
            if (duplicatenames.contains(namestring)) {
                warning += "Duplicate name. ";
            }
            try {
                Integer.parseInt(yearstring);
            } catch (NumberFormatException ex) {
                warning += "Non-integer year. ";
            }
            try {
                Integer.parseInt(prioritystring);
            } catch (NumberFormatException ex) {
                warning += "Non-integer priority. ";
            }
            if (!(blockstring.equals("Squat") || blockstring.equals("Float") || blockstring.startsWith("Block"))) {
                warning += "Invalid block. ";
            }
            if (!(officestring.isEmpty() || offices.containsKey(officestring))) {
                warning += "Invalid office. ";
            }
            people[thisyear][person].warning.setText(warning);
        }
        if (badyears.isEmpty()) {
            warningsbutton.setText("Warnings");
            severe = false;
        } else {
            String s = badyears.toString();
            warningsbutton.setText("Warnings (Severe error in " + s.substring(1, s.length() - 1) + ")");
            severe = true;
            return;
        }
        List<Map<String, Integer>> namelookup = new ArrayList<>();
        for (int year = 0; year < numyears; year++) {
            Map<String, Integer> lookup = new HashMap<>();
            for (int person = 0; person < numpeople[year]; person++) {
                lookup.put(namestrings[year][person], person);
            }
            namelookup.add(lookup);
        }
        boolean[][] floating = new boolean[numyears][];
        boolean[][] squatting = new boolean[numyears][];
        boolean[][] blocking = new boolean[numyears][];
        for (int year = 0; year < numyears; year++) {
            floating[year] = new boolean[numpeople[year]];
            squatting[year] = new boolean[numpeople[year]];
            blocking[year] = new boolean[numpeople[year]];
            for (int person = 0; person < numpeople[year]; person++) {
                if (blockstrings[year][person].equals("Float")) {
                    blockstrings[year][person] += namestrings[year][person];
                    floating[year][person] = true;
                } else if (blockstrings[year][person].equals("Squat")) {
                    blockstrings[year][person] += officestrings[year][person];
                    squatting[year][person] = true;
                } else {
                    blocking[year][person] = true;
                }
            }
        }
        int[][] yearints = new int[numyears][];
        for (int year = 0; year < numyears; year++) {
            yearints[year] = new int[numpeople[year]];
            for (int person = 0; person < numpeople[year]; person++) {
                yearints[year][person] = Integer.parseInt(yearstrings[year][person]);
            }
        }
        int[][] individualpriorities = new int[numyears][];
        for (int year = 0; year < numyears; year++) {
            individualpriorities[year] = new int[numpeople[year]];
            for (int person = 0; person < numpeople[year]; person++) {
                individualpriorities[year][person] = Integer.parseInt(prioritystrings[year][person]);
            }
        }
        BigFraction[][] blockpriorities = new BigFraction[numyears][];
        for (int year = 0; year < numyears; year++) {
            blockpriorities[year] = new BigFraction[numpeople[year]];
            Map<String, Integer> numerators = new HashMap<>();
            Map<String, Integer> denominators = new HashMap<>();
            for (int person = 0; person < numpeople[year]; person++) {
                String block = blockstrings[year][person];
                numerators.put(block, numerators.getOrDefault(block, 0) + individualpriorities[year][person]);
                denominators.put(block, denominators.getOrDefault(block, 0) + 1);
            }
            for (int person = 0; person < numpeople[year]; person++) {
                String block = blockstrings[year][person];
                blockpriorities[year][person] = new BigFraction(numerators.get(block), denominators.get(block));
            }
        }
        BigFraction[][] effectivepriorities = new BigFraction[numyears][];
        for (int year = 0; year < numyears; year++) {
            effectivepriorities[year] = new BigFraction[numpeople[year]];
            for (int person = 0; person < numpeople[year]; person++) {
                String name = namestrings[year][person];
                if (squatting[year][person]) {
                    if (year > 0 && namelookup.get(year - 1).containsKey(name)) {
                        effectivepriorities[year][person] = effectivepriorities[year - 1][namelookup.get(year - 1).get(name)];
                    }
                } else {
                    effectivepriorities[year][person] = blockpriorities[year][person];
                }
            }
        }
        Map<String, Integer> blocktotals = new HashMap<>();
        Map<String, Integer> blockcounts = new HashMap<>();
        for (int person = 0; person < numpeople[thisyear]; person++) {
            String block = blockstrings[thisyear][person];
            String office = officestrings[thisyear][person];
            if (!blocktotals.containsKey(block)) {
                blocktotals.put(block, 0);
                blockcounts.put(block, 0);
            }
            blocktotals.put(block, blocktotals.get(block) + 1);
            if (!office.isEmpty()) {
                blockcounts.put(block, blockcounts.get(block) + 1);
            }
        }
        Map<String, List<Integer>> officecache = new HashMap<>();
        if (thisyear > 0) {
            for (int person = 0; person < numpeople[thisyear - 1]; person++) {
                String office = officestrings[thisyear - 1][person];
                if (!officecache.containsKey(office)) {
                    officecache.put(office, new ArrayList<>());
                }
                officecache.get(office).add(person);
            }
        }
        Map<String, List<Integer>> blockcache = new HashMap<>();
        for (int person = 0; person < numpeople[thisyear];person++) {
            String block = blockstrings[thisyear][person];
            if (!blockcache.containsKey(block)) {
                blockcache.put(block, new ArrayList<>());
            }
            blockcache.get(block).add(person);
        }
        for (int person = 0; person < numpeople[thisyear]; person++) {
            String warning = "";
            int yearint = yearints[thisyear][person];
            int priorityint = individualpriorities[thisyear][person];
            int adjustment = 0;
            String namestring = namestrings[thisyear][person];
            String adjustmentstring = adjustmentstrings[thisyear][person];
            String blockstring = blockstrings[thisyear][person];
            String officestring = officestrings[thisyear][person];
            boolean parse = true;
            if (!adjustmentstring.isEmpty()) {
                try {
                    adjustment = Integer.parseInt(adjustmentstrings[thisyear][person]);
                    if (adjustment >= 0) {
                        warning += "Invalid adjustment. ";
                    }
                } catch (NumberFormatException ex) {
                    warning += "Non-integer adjustment. ";
                    parse = false;
                }
            }
            if (yearint > 1) {
                boolean nohistory = true;
                for (int oldyear = thisyear - 1; oldyear >= 0; oldyear--) {
                    if (namelookup.get(oldyear).containsKey(namestring)) {
                        int oldperson = namelookup.get(oldyear).get(namestring);
                        int oldyearint = yearints[oldyear][oldperson];
                        if (yearint != oldyearint + 1) {
                            warning += "Inconsistent year. ";
                        } else if (thisyear != oldyear + 1) {
                            warning += "Skipped " + (thisyear - oldyear - 1) + " year" + (thisyear == oldyear + 2 ? "" : "s") + ". ";
                        }
                        nohistory = false;
                        break;
                    }
                }
                if (nohistory) {
                    warning += "No history. ";
                }
            }
            if (parse) {
                int[] lookup = {6, 4, 3, 2, 1, 1, 3};
                int basepriority = yearint >= 8 ? 5 : lookup[yearint - 1];
                if (priorityint != basepriority + adjustment) {
                    warning += "Priority off by " + (priorityint - basepriority - adjustment) + ". ";
                }
            }
            int blocktotal = blocktotals.get(blockstring);
            int blockcount = blockcounts.get(blockstring);
            if (blocking[thisyear][person]) {
                if (blocktotal == 1) {
                    warning += "Singleton block. ";
                }
                if (blocking[thisyear][person] && blockcount > 0 && blockcount < blocktotal) {
                    warning += "Incomplete pick. ";
                } else {
                    // Todo: Check subset condition (but this depends on the block ordering)
                }
            }
            if (squatting[thisyear][person]) {
                if (thisyear == 0) {
                    if (officestring.isEmpty()) {
                        warning += "Invalid squat (must specify an office). ";
                    }
                } else if (!namelookup.get(thisyear - 1).containsKey(namestring)) {
                    warning += "Invalid squat (did not receive an office last year). ";
                } else {
                    String oldoffice = officestrings[thisyear - 1][namelookup.get(thisyear - 1).get(namestring)];
                    if (officestring.isEmpty()) {
                        warning += "Invalid squat (must specify " + oldoffice + "). ";
                    } else if (!officestring.equals(oldoffice)) {
                        warning += "Invalid squat (must squat in " + oldoffice + "). ";
                    } else {
                        List<String> badnames = new ArrayList<>();
                        for (int oldperson : officecache.get(officestring)) {
                            String name = namestrings[thisyear - 1][oldperson];
                            if (namelookup.get(thisyear).containsKey(name)) {
                                int newperson = namelookup.get(thisyear).get(name);
                                if (!(squatting[thisyear][newperson] && officestrings[thisyear][newperson].equals(officestring))) {
                                    badnames.add(name);
                                }
                            }
                        }
                        if (!badnames.isEmpty()) {
                            Collections.sort(badnames);
                            String s = badnames.toString();
                            s = s.substring(1, s.length() - 1);
                            warning += "Illegal squat (" + s + "). ";
                        } else {
                            BigFraction oldsum = BigFraction.ZERO;
                            int oldamt = 0;
                            int newsum = 0;
                            int newamt = 0;
                            List<String> badnames2 = new ArrayList<>();
                            for (int squatter : blockcache.get(blockstring)) {
                                BigFraction effectivepriority = effectivepriorities[thisyear][squatter];
                                if (effectivepriority == null) {
                                    badnames2.add(namestrings[thisyear][squatter]);
                                } else {
                                    oldsum = oldsum.add(effectivepriority);
                                    oldamt++;
                                    newsum += individualpriorities[thisyear][squatter];
                                    newamt++;
                                }
                            }
                            if (!badnames2.isEmpty()) {
                                Collections.sort(badnames2);
                                String s = badnames2.toString();
                                warning += "Unable to determine effective priority (" + s.substring(1, s.length() - 1) + "). ";
                            } else {
                                BigFraction pold = oldsum.divide(new BigFraction(oldamt));
                                BigFraction pnew = new BigFraction(newsum, newamt);
                                if (pold.compareTo(pnew) < 0) {
                                    warning += "Illegal squat (" + pold + " < " + pnew + "). ";
                                }
                            }
                        }
                    }
                }
            }
            // --- Office stuff ---
            // Overcrowded office
            people[thisyear][person].warning.setText(warning);
        }

        /*int year = this.year;
        Person[] mypeople = data.get(year);
        Map<String, Map<Integer, Person>> namelookup = new HashMap<>();
        for (int n : data.keySet()) {
            Person[] prevpeople = data.get(n);
            for (int i = 0; i < prevpeople.length - 1; i++) {
                Person person = prevpeople[i];
                String name = person.buttons[0].getText();
                if (!namelookup.containsKey(name)) {
                    namelookup.put(name, new HashMap<>());
                }
                namelookup.get(name).put(n, person);
            }
        }
        Map<String, List<Person>> prevofficelookup = new HashMap<>();
        if (data.containsKey(year - 1)) {
            Person[] prevpeople = data.get(year - 1);
            for (int i = 0; i < prevpeople.length - 1; i++) {
                Person person = prevpeople[i];
                String office = person.buttons[5].getText();
                if (!prevofficelookup.containsKey(office)) {
                    prevofficelookup.put(office, new ArrayList<>());
                }
                prevofficelookup.get(office).add(person);
            }
        }
        Map<String, List<Person>> squatlookup = new HashMap<>();
        for (int i = 0; i < mypeople.length - 1; i++) {
            Person person = mypeople[i];
            String group = person.buttons[4].getText();
            if (group.equals("Squat")) {
                String office = person.buttons[5].getText();
                if (!squatlookup.containsKey(office)) {
                    squatlookup.put(office, new ArrayList<>());
                }
                squatlookup.get(office).add(person);
            }
        }
        Map<Integer, Map<String, List<Person>>> blocklookup = new HashMap<>();
        for (int n : data.keySet()) {
            blocklookup.put(n, new HashMap<>());
            Person[] prevpeople = data.get(n);
            for (int i = 0; i < prevpeople.length - 1; i++) {
                Person person = prevpeople[i];
                String block = person.buttons[4].getText();
                if (block.startsWith("Block ")) {
                    if (!blocklookup.get(n).containsKey(block)) {
                        blocklookup.get(n).put(block, new ArrayList<>());
                    }
                    blocklookup.get(n).get(block).add(person);
                }
            }
        }
        Map<String, Integer> officeamounts = new HashMap<>();
        for (String office : offices.keySet()) {
            officeamounts.put(office, 0);
        }
        for (int i = 0; i < mypeople.length - 1; i++) {
            Person p = mypeople[i];
            String office = p.buttons[5].getText();
            if (offices.containsKey(office)) {
                officeamounts.put(office, officeamounts.get(office) + 1);
            }
        }
        for (int i = 0; i < mypeople.length - 1; i++) {
            Person p = mypeople[i];
            String s0 = p.buttons[0].getText();
            String s1 = p.buttons[1].getText();
            String s2 = p.buttons[2].getText();
            String s3 = p.buttons[3].getText();
            String s4 = p.buttons[4].getText();
            String s5 = p.buttons[5].getText();
            String warning = "";
            int a1 = 0;
            boolean b1 = true;
            try {
                a1 = Integer.parseInt(s1);
                if (a1 < 1) {
                    warning += "Invalid year. ";
                    b1 = false;
                } else if (a1 > 1) {
                    int maxyear = 0;
                    int yearthen = 0;
                    for (int n : namelookup.get(s0).keySet()) {
                        if (n < maxyear || n >= year) {
                            continue;
                        }
                        maxyear = n;
                        yearthen = Integer.parseInt(namelookup.get(s0).get(n).buttons[1].getText());
                    }
                    if (maxyear == 0) {
                        warning += "No history. ";
                    } else if (a1 != yearthen + 1) {
                        warning += "Inconsistent year. ";
                    } else if (year != maxyear + 1) {
                        warning += "Skipped " + (year - maxyear - 1) + " year" + (year == maxyear + 2 ? "" : "s") + ". ";
                    }
                }
            } catch (NumberFormatException ex) {
                warning += "Non-integer year. ";
                b1 = false;
            }
            int a2 = 0;
            boolean b2 = true;
            try {
                a2 = Integer.parseInt(s2);
                if (a2 > 6) {
                    warning += "Invalid priority. ";
                }
            } catch (NumberFormatException ex) {
                warning += "Non-integer priority. ";
                b2 = false;
            }
            int a3 = 0;
            boolean b3 = true;
            if (!s3.isEmpty()) {
                try {
                    a3 = Integer.parseInt(s3);
                    if (a3 >= 0) {
                        warning += "Invalid adjustment. ";
                    }
                } catch (NumberFormatException ex) {
                    warning += "Non-integer adjustment. ";
                    b3 = false;
                }
            }
            if (b1 && b2 && b3) {
                int[] lookup = {6, 4, 3, 2, 1, 1, 3};
                int priority = a1 >= 8 ? 5 : lookup[a1 - 1];
                if (a2 != priority + a3) {
                    warning += "Priority off by " + (a2 - priority - a3) + ". ";
                }
            }
            String a4;
            boolean b4 = true;
            if (!s4.equals("Squat") && !s4.equals("Float") && !s4.startsWith("Block ")) {
                warning += "Invalid block. ";
                b4 = false;
            }
            if (s4.equals("Squat")) {
                if (!(namelookup.get(s0).containsKey(year - 1) && namelookup.get(s0).get(year - 1).buttons[5].getText().equals(s5))) {
                    warning += "Invalid squat (" + s5 + "). ";
                } else if (!(b1 && a1 >= 9)) {
                    List<String> badsquat = new ArrayList<>();
                    for (Person q : prevofficelookup.get(s5)) {
                        String s = q.buttons[0].getText();
                        if (namelookup.get(s).containsKey(year)) {
                            Person r = namelookup.get(s).get(year);
                            if (!(r.buttons[4].getText().equals(s4) && r.buttons[5].getText().equals(s5))) {
                                badsquat.add(s);
                            }
                        }
                    }
                    if (!badsquat.isEmpty()) {
                        Collections.sort(badsquat);
                        String s = badsquat.toString();
                        warning += "Invalid squat (" + s.substring(1, s.length() - 1) + "). ";
                    } else if (b2) {
                        try {
                            BigFraction newsum = BigFraction.ZERO;
                            int newamt = 0;
                            for (Person q : squatlookup.get(s5)) {
                                newsum = newsum.add(new BigFraction(Integer.parseInt(q.buttons[2].getText())));
                                newamt++;
                            }
                            BigFraction oldsum = BigFraction.ZERO;
                            int oldamt = 0;
                            List<String> onlysquat = new ArrayList<>();
                            for (Person q : prevofficelookup.get(s5)) {
                                String name = q.buttons[0].getText();
                                int maxyear = 0;
                                Person nonsquatter = null;
                                for (int n : namelookup.get(name).keySet()) {
                                    if (n < maxyear || n >= year) {
                                        continue;
                                    }
                                    Person r = namelookup.get(name).get(n);
                                    if (!r.buttons[4].getText().equals("Squat")) {
                                        maxyear = n;
                                        nonsquatter = r;
                                    }
                                }
                                if (nonsquatter == null) {
                                    onlysquat.add(name);
                                } else {
                                    String group = nonsquatter.buttons[4].getText();
                                    if (group.equals("Float")) {
                                        oldsum = oldsum.add(new BigFraction(Integer.parseInt(nonsquatter.buttons[2].getText())));
                                        oldamt++;
                                    } else if (group.startsWith("Block ")) {
                                        BigFraction average = BigFraction.ZERO;
                                        int amt = 0;
                                        for (Person r : blocklookup.get(maxyear).get(nonsquatter.buttons[4].getText())) {
                                            average = average.add(new BigFraction(Integer.parseInt(r.buttons[2].getText())));
                                            amt++;
                                        }
                                        average = average.divide(new BigFraction(amt));
                                        oldsum = oldsum.add(average);
                                        oldamt++;
                                    } else {
                                        onlysquat.add(name);
                                    }
                                }
                            }
                            if (!onlysquat.isEmpty()) {
                                Collections.sort(onlysquat);
                                String s = onlysquat.toString();
                                warning += "Unable to determine effective priority (" + s.substring(1, s.length() - 1) + "). ";
                            } else {
                                newsum = newsum.divide(new BigFraction(newamt));
                                oldsum = oldsum.divide(new BigFraction(oldamt));
                                if (newsum.compareTo(oldsum) > 0) {
                                    warning += "Invalid squat (" + newsum + ">" + oldsum + "). ";
                                }
                            }
                        } catch (NumberFormatException ex) {
                            warning += "Non-integer priority. ";
                        }
                    }
                }
            }
            if (s4.startsWith("Block ")) {
                if (blocklookup.get(year).get(s4).size() == 1) {
                    warning += "Singleton block. ";
                }
            }
            if (!offices.containsKey(s5)) {
                if (!s5.isEmpty()) {
                    warning += "Invalid office. ";
                }
            } else {
                if (officeamounts.get(s5) > offices.get(s5)) {
                    warning += "Overfull office (" + s5 + " " + officeamounts.get(s5) + " vs " + offices.get(s5) + ". ";
                } else if (s4.startsWith("Block ")) {
                    // Check splitting
                }
            }
            p.warning.setText(warning);
        }*/
    }
}
