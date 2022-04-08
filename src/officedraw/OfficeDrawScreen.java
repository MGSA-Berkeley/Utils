package officedraw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import mgsa.GraphicsUtils;

public class OfficeDrawScreen implements mgsa.Screen {

    private final mgsa.MainCanvas canvas;

    private Rectangle yearpos;
    private Rectangle leftpos;
    private Rectangle rightpos;
    private Rectangle backpos;
    private Rectangle exitpos;

    private final Set<Integer> keyset = new HashSet<>();

    private Point click;

    private static final Color background = GraphicsUtils.Grey;
    private static final Color foreground = GraphicsUtils.Black;
    private static final Color mouseover = GraphicsUtils.BayFog;

    private static final Font bigfont = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    private static final Font smallfont = new Font(Font.SANS_SERIF, Font.BOLD, 14);

    private int year = Calendar.getInstance().get(Calendar.YEAR);

    public OfficeDrawScreen(mgsa.MainCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void paintComponent(Graphics g, int w, int h) {
        int bigpadding = 16;
        int mediumpadding = 4;
        int smallpadding = 2;
        int bigthickness = 4;
        int smallthickness = 2;
        String yeartext = Integer.toString(year);
        String lefttext = "<";
        String righttext = ">";
        String backtext = "←";
        String exittext = "X";
        g.setFont(bigfont);
        Rectangle banner = GraphicsUtils.getRectangle(g, "  " + yeartext + "  ", new Point(w / 2, 0), false, bigpadding);
        yearpos = GraphicsUtils.getRectangle(g, yeartext, new Point(w / 2, banner.height / 2), false, mediumpadding);
        leftpos = GraphicsUtils.getRectangle(g, lefttext, new Point(banner.x, banner.height / 2), false, mediumpadding);
        rightpos = GraphicsUtils.getRectangle(g, righttext, new Point(banner.x + banner.width, banner.height / 2), false, mediumpadding);
        backpos = GraphicsUtils.getRectangle(g, backtext, new Point(banner.height / 2, banner.height / 2), false, mediumpadding);
        exitpos = GraphicsUtils.getRectangle(g, backtext, new Point(w - banner.height / 2, banner.height / 2), false, mediumpadding);
        g.setColor(background);
        g.fillRect(0, 0, w, h);
        g.setColor(mouseover);
        Point p = canvas.getMousePosition();
        if (p != null) {
            if (yearpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, yearpos);
            }
            if (leftpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, leftpos);
            }
            if (rightpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, rightpos);
            }
            if (backpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, backpos);
            }
            if (exitpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, exitpos);
            }
        }
        p = click;
        if (p != null) {
            if (yearpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, yearpos);
            }
            if (leftpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, leftpos);
            }
            if (rightpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, rightpos);
            }
            if (backpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, backpos);
            }
            if (exitpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, exitpos);
            }
        }
        g.setColor(foreground);
        GraphicsUtils.drawCenteredString(g, yeartext, yearpos, false);
        GraphicsUtils.drawCenteredString(g, lefttext, leftpos, false);
        GraphicsUtils.drawCenteredString(g, righttext, rightpos, false);
        GraphicsUtils.drawCenteredString(g, backtext, backpos, false);
        GraphicsUtils.drawCenteredString(g, exittext, exitpos, false);
        GraphicsUtils.drawLine(g, 0, banner.height, w, banner.height, bigthickness);
    }

    @Override
    public void mousePressed() {
        click = canvas.getMousePosition();
    }

    @Override
    public void mouseReleased() {
        Point p = canvas.getMousePosition();
        if (p != null && click != null) {
            if (yearpos.contains(p) && yearpos.contains(click)) {
                year = Calendar.getInstance().get(Calendar.YEAR);
            }
            if (leftpos.contains(p) && leftpos.contains(click)) {
                year--;
            }
            if (rightpos.contains(p) && rightpos.contains(click)) {
                year++;
            }
            if (backpos.contains(p) && backpos.contains(click)) {
                canvas.setScreen(new mgsa.HomeScreen(canvas));
            }
            if (exitpos.contains(p) && exitpos.contains(click)) {
                System.exit(0);
            }
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
