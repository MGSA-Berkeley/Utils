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
    private final Map<String, String> letters = new HashMap<>();

    private Point click;

    private static final Color BACKGROUND = mgsa.GraphicsUtils.Grey;
    private static final Color FOREGROUND = mgsa.GraphicsUtils.Black;
    private static final Color MOUSEOVER = mgsa.GraphicsUtils.BayFog;
    private static final Color SELECT = mgsa.GraphicsUtils.SatherGate;

    private static final Font BIGFONT = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    private static final Font MEDIUMFONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    private static final Font SMALLFONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    private int bannerheight;
    private int rowheight;

    private final Map<Integer, Person[]> data = LoadData.load();
    private int row = 0;
    private int column = 0;
    private int scroll = 0;
    private boolean severe;

    public OfficeDrawScreen(mgsa.MainCanvas canvas) {
        this.canvas = canvas;
        String lower = "abcdefghijklmnopqrstuvwxyz1234567890";
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()";
        for (int i = 0; i < lower.length(); i++) {
            letters.put(Character.toString(lower.charAt(i)), Character.toString(upper.charAt(i)));
        }
        if (!data.containsKey(year)) {
            data.put(year, new Person[]{new Person()});
        }
        update();
    }

    @Override
    public void paintComponent(Graphics g, int w, int h) {
        Person[] people = data.get(year);
        int bigpadding = 12;
        int smallpadding = 5;
        g.setFont(BIGFONT);
        bannerbutton.setText(Integer.toString(year));
        int bannerwidth = bannerbutton.getWidth(g, smallpadding);
        bannerheight = bannerbutton.getHeight(g, bigpadding);
        bannerbutton.setRectCenter(g, new Point(w / 2, bannerheight / 2), smallpadding);
        leftbutton.setRectRight(g, new Point(w / 2 - bannerwidth / 2, bannerheight / 2), smallpadding);
        rightbutton.setRectLeft(g, new Point(w / 2 + bannerwidth / 2, bannerheight / 2), smallpadding);
        backbutton.setRectLeft(g, new Point(bigpadding - smallpadding, bannerheight / 2), smallpadding);
        exitbutton.setRectRight(g, new Point(w - bigpadding + smallpadding, bannerheight / 2), smallpadding);
        g.setFont(MEDIUMFONT);
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
        this.rowheight = rowheight;
        g.setFont(SMALLFONT);
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
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, w, h);
        for (int i = 0; i < people.length; i++) {
            for (int j = 0; j < 6; j++) {
                if (row == i && column == j) {
                    people[i].buttons[j].highlight(g, SELECT);
                    people[i].buttons[j].drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, bannerheight + rowheight);
                } else {
                    people[i].buttons[j].drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, bannerheight + rowheight, mouse, click);
                }
            }
            if (row == i && column == 6) {
                people[i].warning.highlight(g, SELECT);
                people[i].warning.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, bannerheight + rowheight);
            } else {
                people[i].warning.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, bannerheight + rowheight, mouse, click);
            }
        }
        for (int n = 2; n <= people.length + 1; n++) {
            g.drawLine(0, bannerheight + n * rowheight - scroll, w, bannerheight + n * rowheight - scroll);
        }
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, w, bannerheight + rowheight);
        g.setFont(BIGFONT);
        bannerbutton.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);
        leftbutton.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);
        rightbutton.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);
        backbutton.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);
        exitbutton.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);
        g.setFont(MEDIUMFONT);
        namebutton.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
        yearbutton.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
        prioritybutton.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
        adjustmentbutton.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
        blockbutton.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
        officebutton.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
        warningsbutton.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
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
        if (namebutton.contains(p) && namebutton.contains(click)) {
            data.put(year, Sorting.nameSort(data.get(year)));
            scroll = 0;
            update();
        }
        if (yearbutton.contains(p) && yearbutton.contains(click)) {
            data.put(year, Sorting.yearSort(data.get(year)));
            scroll = 0;
            update();
        }
        if (prioritybutton.contains(p) && prioritybutton.contains(click)) {
            data.put(year, Sorting.prioritySort(data.get(year)));
            scroll = 0;
            update();
        }
        if (adjustmentbutton.contains(p) && adjustmentbutton.contains(click)) {
            data.put(year, Sorting.adjustmentSort(data.get(year)));
            scroll = 0;
            update();
        }
        if (blockbutton.contains(p) && blockbutton.contains(click)) {
            data.put(year, Sorting.blockSort(data.get(year), year));
            scroll = 0;
            update();
        }
        if (officebutton.contains(p) && officebutton.contains(click)) {
            data.put(year, Sorting.officeSort(data.get(year)));
            scroll = 0;
            update();
        }
        if (warningsbutton.contains(p) && warningsbutton.contains(click)) {
            data.put(year, Sorting.warningsSort(data.get(year)));
            scroll = 0;
            update();
        }
        if (p != null && click != null) {
            if (p.y > bannerheight + rowheight && click.y > bannerheight + rowheight) {
                for (int i = 0; i < data.get(year).length; i++) {
                    Person person = data.get(year)[i];
                    for (int j = 0; j < 6; j++) {
                        if (person.buttons[j].contains(p) && person.buttons[j].contains(click)) {
                            row = i;
                            column = j;
                        }
                    }
                    if (person.warning.contains(p) && person.warning.contains(click)) {
                        row = i;
                        column = 6;
                    }
                }
            }
        }
        click = null;
    }

    @Override
    public void mouseScrolled(int n) {
        n *= 6;
        int maxscroll = rowheight * (data.get(year).length - 1);
        if (scroll + n < 0) {
            scroll = 0;
        } else if (scroll + n > maxscroll) {
            scroll = maxscroll;
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
            normalizeBlocks();
            SaveData.save(year, data, severe);
            update();
            return;
        }
        if (key == KeyEvent.VK_UP) {
            if (row > 0) {
                row--;
            }
            int maxscroll = row * rowheight;
            if (scroll > maxscroll) {
                scroll = maxscroll;
            }
            return;
        }
        if (key == KeyEvent.VK_DOWN) {
            if (row < data.get(year).length - 1) {
                row++;
            }
            int minscroll = (row + 2) * rowheight - canvas.getHeight() + bannerheight + 1;
            if (scroll < minscroll) {
                scroll = minscroll;
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
            String s = KeyEvent.getKeyText(key).toLowerCase();
            if (key == KeyEvent.VK_BACK_SPACE) {
                String text = button.getText();
                if (!text.isEmpty()) {
                    button.setText(text.substring(0, text.length() - 1));
                    boolean delete = people[row].blank();
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
            } else if (key == KeyEvent.VK_SLASH) {
                button.setText(button.getText() + "/");
                append = true;
            } else if (key == KeyEvent.VK_PERIOD) {
                button.setText(button.getText() + ".");
                append = true;
            } else if (letters.keySet().contains(s)) {
                button.setText(button.getText() + (keyset.contains(KeyEvent.VK_SHIFT) ? letters.get(s) : s));
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
        Arrays.sort(years);
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
                if (namestring.isEmpty()) {
                    badyears.add(years[year]);
                    break;
                }
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
                    new BigFraction(prioritystring);
                } catch (NumberFormatException ex) {
                    badyears.add(years[year]);
                    break;
                }
                if (!(blockstring.equals("Squat") || blockstring.equals("Float") || blockstring.startsWith("Block"))) {
                    badyears.add(years[year]);
                    break;
                }
                if (!(officestring.isEmpty() || (Offices.capacitymap.containsKey(years[year]) && Offices.capacitymap.get(years[year]).containsKey(officestring)))) {
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
            if (namestring.isEmpty()) {
                warning += "Empty name. ";
            } else if (duplicatenames.contains(namestring)) {
                warning += "Duplicate name. ";
            }
            try {
                Integer.parseInt(yearstring);
            } catch (NumberFormatException ex) {
                warning += "Non-integer year. ";
            }
            try {
                new BigFraction(prioritystring);
            } catch (NumberFormatException ex) {
                warning += "Non-rational priority. ";
            }
            if (!(blockstring.equals("Squat") || blockstring.equals("Float") || blockstring.startsWith("Block"))) {
                warning += "Invalid block. ";
            }
            if (!(officestring.isEmpty() || (Offices.capacitymap.containsKey(year) && Offices.capacitymap.get(year).containsKey(officestring)))) {
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
        BigFraction[][] individualpriorities = new BigFraction[numyears][];
        for (int year = 0; year < numyears; year++) {
            individualpriorities[year] = new BigFraction[numpeople[year]];
            for (int person = 0; person < numpeople[year]; person++) {
                individualpriorities[year][person] = new BigFraction(prioritystrings[year][person]);
            }
        }
        BigFraction[][] blockpriorities = new BigFraction[numyears][];
        for (int year = 0; year < numyears; year++) {
            blockpriorities[year] = new BigFraction[numpeople[year]];
            Map<String, BigFraction> numerators = new HashMap<>();
            Map<String, Integer> denominators = new HashMap<>();
            for (int person = 0; person < numpeople[year]; person++) {
                String block = blockstrings[year][person];
                numerators.put(block, numerators.getOrDefault(block, BigFraction.ZERO).add(individualpriorities[year][person]));
                denominators.put(block, denominators.getOrDefault(block, 0) + 1);
            }
            for (int person = 0; person < numpeople[year]; person++) {
                String block = blockstrings[year][person];
                blockpriorities[year][person] = numerators.get(block).divide(new BigFraction(denominators.get(block)));
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
        Map<String, List<Integer>> blockcache = new HashMap<>();
        for (int person = 0; person < numpeople[thisyear]; person++) {
            String block = blockstrings[thisyear][person];
            if (!blockcache.containsKey(block)) {
                blockcache.put(block, new ArrayList<>());
            }
            blockcache.get(block).add(person);
        }
        Map<String, Integer> blocktotals = new HashMap<>();
        Map<String, Integer> blockcounts = new HashMap<>();
        for (String block : blockcache.keySet()) {
            blocktotals.put(block, blockcache.get(block).size());
            int count = 0;
            for (int person : blockcache.get(block)) {
                if (!officestrings[thisyear][person].isEmpty()) {
                    count++;
                }
            }
            blockcounts.put(block, count);
        }
        Map<String, List<Integer>> prevofficecache = new HashMap<>();
        if (thisyear > 0) {
            for (int person = 0; person < numpeople[thisyear - 1]; person++) {
                String office = officestrings[thisyear - 1][person];
                if (!prevofficecache.containsKey(office)) {
                    prevofficecache.put(office, new ArrayList<>());
                }
                prevofficecache.get(office).add(person);
            }
        }
        Person[] sorted = Sorting.blockSort(people[thisyear], year);
        Map<String, Integer> officecache = new HashMap<>();
        for (int i = 0; i < numpeople[thisyear]; i++) {
            Person person = sorted[i];
            String office = person.buttons[5].getText();
            if (!office.isEmpty()) {
                officecache.put(office, officecache.getOrDefault(office, 0) + 1);
            }
        }
        for (int person = 0; person < numpeople[thisyear]; person++) {
            String warning = "";
            int yearint = yearints[thisyear][person];
            BigFraction priorityint = individualpriorities[thisyear][person];
            BigFraction adjustment = BigFraction.ZERO;
            String namestring = namestrings[thisyear][person];
            String adjustmentstring = adjustmentstrings[thisyear][person];
            String blockstring = blockstrings[thisyear][person];
            String officestring = officestrings[thisyear][person];
            boolean parse = true;
            if (!adjustmentstring.isEmpty()) {
                try {
                    adjustment = new BigFraction(adjustmentstrings[thisyear][person]);
                    if (adjustment.signum() >= 0) {
                        warning += "Invalid adjustment. ";
                    }
                } catch (NumberFormatException ex) {
                    warning += "Non-rational adjustment. ";
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
                BigFraction off = priorityint.subtract(new BigFraction(basepriority)).subtract(adjustment);
                if (off.signum() != 0) {
                    warning += "Priority off by " + off + ". ";
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
                        BigFraction oldsum = BigFraction.ZERO;
                        int oldamt = 0;
                        BigFraction newsum = BigFraction.ZERO;
                        int newamt = 0;
                        List<String> badnames2 = new ArrayList<>();
                        for (int squatter : blockcache.get(blockstring)) {
                            BigFraction effectivepriority = effectivepriorities[thisyear][squatter];
                            if (effectivepriority == null) {
                                badnames2.add(namestrings[thisyear][squatter]);
                            } else {
                                oldsum = oldsum.add(effectivepriority);
                                oldamt++;
                                newsum = newsum.add(individualpriorities[thisyear][squatter]);
                                newamt++;
                            }
                        }
                        if (!badnames2.isEmpty()) {
                            Collections.sort(badnames2);
                            String s = badnames2.toString();
                            warning += "Unable to determine effective priority (" + s.substring(1, s.length() - 1) + "). ";
                        } else {
                            BigFraction pold = oldsum.divide(new BigFraction(oldamt));
                            BigFraction pnew = newsum.divide(new BigFraction(newamt));
                            if (pold.compareTo(pnew) < 0) {
                                warning += "Illegal squat (" + pold + " < " + pnew + "). ";
                            }
                        }
                    }
                }
            }
            if (!officestring.isEmpty() && Offices.capacitymap.containsKey(year) && officecache.get(officestring) > Offices.capacitymap.get(year).get(officestring)) {
                warning += "Overfull office. ";
            }
            people[thisyear][person].warning.setText(warning);
        }
    }

    private void normalizeBlocks() {
        Person[] people = data.get(year);
        Map<String, List<Person>> blocks = new HashMap<>();
        for (Person person : people) {
            String block = person.buttons[4].getText();
            if (block.startsWith("Block")) {
                if (!blocks.containsKey(block)) {
                    blocks.put(block, new ArrayList<>());
                }
                blocks.get(block).add(person);
            }
        }
        for (String blockname : blocks.keySet()) {
            Collections.sort(blocks.get(blockname), Sorting::nameCompare);
        }
        for (Person person : people) {
            String block = person.buttons[4].getText();
            if (block.startsWith("Block")) {
                person.buttons[4].setText("Block " + blocks.get(block).get(0).buttons[0].getText());
            }
        }
    }
}
