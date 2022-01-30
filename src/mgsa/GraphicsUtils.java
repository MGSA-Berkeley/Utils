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
    public static final Color Grey = new Color(238, 238, 238);
    public static final Color Lawrence = new Color(0, 176, 218);
    public static final Color SoyBean = new Color(133, 148, 56);

    private static final String sample1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String sample2 = "abcdefhiklmnorstuvwxzABCDEFGHIJKLMNOPRSTUVWXYZ";

    private static int getHeight(Graphics g, String sample) {
        return (int) new TextLayout(sample, g.getFont(), ((Graphics2D) g).getFontRenderContext()).getBounds().getHeight();
    }

    public static void drawRectangle(Graphics g, Rectangle r, int thickness) {
        ((Graphics2D) g).setStroke(new BasicStroke(thickness));
        g.drawRect(r.x, r.y, r.width, r.height);
        ((Graphics2D) g).setStroke(new BasicStroke());
    }

    public static void fillRectangle(Graphics g, Rectangle r) {
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    public static void drawCenteredString(Graphics g, String text, Rectangle r, boolean below) {
        FontMetrics metrics = g.getFontMetrics();
        int w = metrics.stringWidth(text);
        int h = getHeight(g, below ? sample1 : sample2);
        int descent = below ? metrics.getDescent() - 1 : 0;
        g.drawString(text, r.x + r.width / 2 - w / 2, r.y + r.height / 2 - descent + h / 2);
    }

    public static Rectangle getRectangle(Graphics g, String text, Point p, boolean below, int padding) {
        FontMetrics metrics = g.getFontMetrics();
        int w = metrics.stringWidth(text);
        int h = getHeight(g, below ? sample1 : sample2);
        return new Rectangle(p.x - w / 2 - padding, p.y - h / 2 - padding, w + 2 * padding, h + 2 * padding);
    }
}
