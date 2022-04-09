package officedraw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
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

    public OfficeDrawScreen(mgsa.MainCanvas canvas) {
        this.canvas = canvas;
        for (char c : "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray()) {
            letters.add(Character.toString(c));
        }
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
        }
        // *** PAINT THE CANVAS ***
        Point mouse = canvas.getMousePosition();
        g.setColor(background);
        g.fillRect(0, 0, w, h);
        for (int i = 0; i < people.length; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == row && j == column) {
                    people[i].buttons[j].highlight(g, mouseover);
                }
                people[i].buttons[j].drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
            }
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
            scroll = 0;
            year--;
        }
        if (rightbutton.contains(p) && rightbutton.contains(click)) {
            if (!data.containsKey(year + 1)) {
                data.put(year + 1, new Person[]{new Person()});
            }
            row = 0;
            scroll = 0;
            year++;
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
        n *= 4;
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
            if (column < 4) {
                column++;
            }
            return;
        }
        Person[] people = data.get(year);
        mgsa.Button button = people[row].buttons[column];
        if (key == KeyEvent.VK_BACK_SPACE) {
            String s = button.getText();
            if (!s.isEmpty()) {
                button.setText(s.substring(0, s.length() - 1));
                for (mgsa.Button b : people[row].buttons) {
                    if (!b.getText().isEmpty()) {
                        return;
                    }
                }
                Person[] newpeople = new Person[people.length - 1];
                System.arraycopy(people, 0, newpeople, 0, row);
                System.arraycopy(people, row + 1, newpeople, row, newpeople.length - row);
                data.put(year, newpeople);
            }
            return;
        }
        boolean append = false;
        if (key == KeyEvent.VK_SPACE) {
            button.setText(button.getText() + " ");
            append = true;
        }
        if (key == KeyEvent.VK_MINUS) {
            button.setText(button.getText() + "-");
            append = true;
        }
        String s = KeyEvent.getKeyText(key);
        if (letters.contains(s)) {
            button.setText(button.getText() + (keyset.contains(KeyEvent.VK_SHIFT) ? s : s.toLowerCase()));
            append = true;
        }
        if (append && row == people.length - 1) {
            people = Arrays.copyOf(people, people.length + 1);
            people[people.length - 1] = new Person();
            data.put(year, people);
        }
    }

    @Override
    public void keyReleased(int key) {
        keyset.remove(key);
    }
}
