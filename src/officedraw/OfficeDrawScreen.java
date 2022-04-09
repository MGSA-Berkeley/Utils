package officedraw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
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
    private final mgsa.Button groupbutton = new mgsa.Button("Group", null);
    private final mgsa.Button officebutton = new mgsa.Button("Office", null);
    private final mgsa.Button warningsbutton = new mgsa.Button("Warnings", null);

    private final Set<Integer> keyset = new HashSet<>();

    private Point click;

    private static final Color background = mgsa.GraphicsUtils.Grey;
    private static final Color foreground = mgsa.GraphicsUtils.Black;
    private static final Color mouseover = mgsa.GraphicsUtils.BayFog;

    private static final Font bigfont = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    private static final Font mediumfont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    private static final Font smallfont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

    private final Map<Integer, List<Person>> data = new HashMap<>();

    public OfficeDrawScreen(mgsa.MainCanvas canvas) {
        this.canvas = canvas;
        data.put(2022, new ArrayList<>());
        data.get(2022).add(new Person("Thomas Browning", "4", "0", "Squat", "1041"));
        data.get(2022).add(new Person("Ted Kaczynski", "8", "1", "Group 1", "1942"));
        data.get(2022).add(new Person("Albert Einstein", "5", "2", "Group 1004913", "1945"));
        data.get(2022).add(new Person("", "", "", "", ""));
    }

    @Override
    public void paintComponent(Graphics g, int w, int h) {
        int year = this.year; // fix the year to avoid concurrency bugs
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
        int grouplen = groupbutton.getWidth(g, smallpadding);
        rowheight = Math.max(rowheight, groupbutton.getHeight(g, smallpadding));
        int officelen = officebutton.getWidth(g, smallpadding);
        rowheight = Math.max(rowheight, officebutton.getHeight(g, smallpadding));
        if (data.containsKey(year)) {
            g.setFont(smallfont);
            for (Person p : data.get(year)) {
                namelen = Math.max(namelen, p.name.getWidth(g, smallpadding));
                yearlen = Math.max(yearlen, p.year.getWidth(g, smallpadding));
                prioritylen = Math.max(prioritylen, p.priority.getWidth(g, smallpadding));
                grouplen = Math.max(grouplen, p.group.getWidth(g, smallpadding));
                officelen = Math.max(officelen, p.office.getWidth(g, smallpadding));
            }
        }
        int namepos = 0;
        int yearpos = namepos + namelen;
        int prioritypos = yearpos + yearlen;
        int grouppos = prioritypos + prioritylen;
        int officepos = grouppos + grouplen;
        int warningspos = officepos + officelen;
        int warningslen = w - warningspos;
        namebutton.setRect(new Rectangle(namepos, bannerheight, namelen, rowheight));
        yearbutton.setRect(new Rectangle(yearpos, bannerheight, yearlen, rowheight));
        prioritybutton.setRect(new Rectangle(prioritypos, bannerheight, prioritylen, rowheight));
        groupbutton.setRect(new Rectangle(grouppos, bannerheight, grouplen, rowheight));
        officebutton.setRect(new Rectangle(officepos, bannerheight, officelen, rowheight));
        warningsbutton.setRect(new Rectangle(warningspos, bannerheight, warningslen, rowheight));
        if (data.containsKey(year)) {
            int y = bannerheight;
            for (Person p : data.get(year)) {
                y += rowheight;
                p.name.setRect(new Rectangle(namepos, y, namelen, rowheight));
                p.year.setRect(new Rectangle(yearpos, y, yearlen, rowheight));
                p.priority.setRect(new Rectangle(prioritypos, y, prioritylen, rowheight));
                p.group.setRect(new Rectangle(grouppos, y, grouplen, rowheight));
                p.office.setRect(new Rectangle(officepos, y, officelen, rowheight));
            }
        }
        // *** PAINT THE CANVAS ***
        Point mouse = canvas.getMousePosition();
        g.setColor(background);
        g.fillRect(0, 0, w, h);
        if (data.containsKey(year)) {
            for (Person p : data.get(year)) {
                p.name.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
                p.year.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
                p.priority.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
                p.group.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
                p.office.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
            }
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
        groupbutton.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
        officebutton.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
        warningsbutton.drawLeft(g, mouseover, foreground, smallpadding, mouse, click);
        for (int n = 0; n <= (data.containsKey(year) ? data.get(year).size() + 1 : 1); n++) {
            g.drawLine(0, bannerheight + n * rowheight, w, bannerheight + n * rowheight);
        }
        g.drawLine(yearpos, bannerheight, yearpos, h);
        g.drawLine(prioritypos, bannerheight, prioritypos, h);
        g.drawLine(grouppos, bannerheight, grouppos, h);
        g.drawLine(officepos, bannerheight, officepos, h);
        g.drawLine(warningspos, bannerheight, warningspos, h);
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
            year--;
        }
        if (rightbutton.contains(p) && rightbutton.contains(click)) {
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
    public void keyPressed(int key) {
        keyset.add(key);
        if (keyset.contains(KeyEvent.VK_CONTROL) && key == KeyEvent.VK_Q) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(int key) {
        keyset.remove(key);
    }
}
