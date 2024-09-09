package officedraw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Calendar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class OfficeDrawScreen2 implements mgsa.Screen {

    private final mgsa.MainCanvas canvas;

    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private final mgsa.Button bannerbutton = new mgsa.Button(Integer.toString(year), null);
    private final mgsa.Button leftbutton = new mgsa.Button("<", null);
    private final mgsa.Button rightbutton = new mgsa.Button(">", null);
    private final mgsa.Button backbutton = new mgsa.Button("←", null);
    private final mgsa.Button exitbutton = new mgsa.Button("X", null);

    private Point click;

    private static final Color BACKGROUND = mgsa.GraphicsUtils.Grey;
    private static final Color FOREGROUND = mgsa.GraphicsUtils.Black;
    private static final Color MOUSEOVER = mgsa.GraphicsUtils.BayFog;
    private static final Color SELECT = mgsa.GraphicsUtils.SatherGate;

    private static final Font BIGFONT = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    private static final Font MEDIUMFONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    private static final Font SMALLFONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    private int bannerheight;

    private final JTable table = new JTable();
    private final JScrollPane scrollpane = new JScrollPane(table);

    public OfficeDrawScreen2(mgsa.MainCanvas canvas) {
        this.canvas = canvas;
        // load data?
    }

    @Override
    public void paintComponent(Graphics g, int w, int h) {
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
        Point mouse = canvas.getMousePosition();
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, w, h);
        g.setFont(BIGFONT);
        bannerbutton.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);
        leftbutton.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);
        rightbutton.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);
        backbutton.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);
        exitbutton.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);

        scrollpane.setSize(w + 1, h - bannerheight + 1);
        g.translate(0, bannerheight);
        scrollpane.paint(g);
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
    }

    @Override
    public void mouseScrolled(int n) {
    }

    @Override
    public void keyPressed(int key) {
    }

    @Override
    public void keyReleased(int key) {
    }
}
