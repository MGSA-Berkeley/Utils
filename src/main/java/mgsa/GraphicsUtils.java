package mgsa;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.TextLayout;

public class GraphicsUtils {

    public static final Color BayFog = new Color(221, 213, 199);
    public static final Color Black = new Color(0, 0, 0);
    public static final Color CaliforniaGolf = new Color(253, 181, 21);
    public static final Color GoldenGate = new Color(237, 78, 51);
    public static final Color WebGray = new Color(136, 136, 136);
    public static final Color Grey = new Color(238, 238, 238);
    public static final Color Lawrence = new Color(0, 176, 218);
    public static final Color Pacific = new Color(70, 83, 94);
    public static final Color SatherGate = new Color(185, 211, 182);
    public static final Color SoyBean = new Color(133, 148, 56);

    private static final String sample1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String sample2 = "abcdefhiklmnorstuvwxzABCDEFGHIJKLMNOPRSTUVWXYZ";

    private static int getHeight(Graphics g, String sample) {
        return (int) new TextLayout(sample, g.getFont(), ((Graphics2D) g).getFontRenderContext()).getBounds().getHeight();
    }

    public static int getHeight(Graphics g) {
        return getHeight(g, sample2);
    }

    public static void drawLine(Graphics g, int x1, int y1, int x2, int y2, int thickness) {
        ((Graphics2D) g).setStroke(new BasicStroke(thickness));
        g.drawLine(x1, y1, x2, y2);
        ((Graphics2D) g).setStroke(new BasicStroke());
    }

    public static void drawRectangle(Graphics g, Rectangle r, int thickness) {
        ((Graphics2D) g).setStroke(new BasicStroke(thickness));
        g.drawRect(r.x, r.y, r.width, r.height);
        ((Graphics2D) g).setStroke(new BasicStroke());
    }

    public static void fillRectangle(Graphics g, Rectangle r) {
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    public static void drawCenterString(Graphics g, String text, Rectangle r, boolean below) {
        FontMetrics metrics = g.getFontMetrics();
        int w = metrics.stringWidth(text);
        int h = getHeight(g, below ? sample1 : sample2);
        int descent = (below ? metrics.getDescent() : 0) - 1;
        g.drawString(text, r.x + r.width / 2 - w / 2, r.y + r.height / 2 - descent + h / 2);
    }

    public static void drawLeftString(Graphics g, String text, Rectangle r, boolean below, int padding) {
        FontMetrics metrics = g.getFontMetrics();
        int h = getHeight(g, below ? sample1 : sample2);
        int descent = (below ? metrics.getDescent() : 0) - 1;
        g.drawString(text, r.x + padding, r.y + r.height / 2 - descent + h / 2);
    }

    public static Rectangle getCenterRectangle(Graphics g, String text, Point p, boolean below, int padding) {
        FontMetrics metrics = g.getFontMetrics();
        int w = metrics.stringWidth(text);
        int h = getHeight(g, below ? sample1 : sample2);
        return new Rectangle(p.x - w / 2 - padding, p.y - h / 2 - padding, w + 2 * padding, h + 2 * padding);
    }

    public static Rectangle getLeftRectangle(Graphics g, String text, Point p, int w, boolean below, int padding) {
        int h = getHeight(g, below ? sample1 : sample2);
        return new Rectangle(p.x, p.y - h / 2 - padding, w, h + 2 * padding);
    }
}
