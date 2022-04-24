package mgsa;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Button {

    private String text;
    private Rectangle rect;

    public Button(String text, Rectangle rect) {
        this.text = text;
        this.rect = rect;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public void setRectCenter(Graphics g, Point p, int padding) {
        int w = getWidth(g, padding);
        int h = getHeight(g, padding);
        setRect(new Rectangle(p.x - w / 2, p.y - h / 2, w, h));
    }

    public void setRectTop(Graphics g, Point p, int padding) {
        int w = getWidth(g, padding);
        int h = getHeight(g, padding);
        setRect(new Rectangle(p.x - w / 2, p.y, w, h));
    }

    public void setRectLeft(Graphics g, Point p, int padding) {
        int w = getWidth(g, padding);
        int h = getHeight(g, padding);
        setRect(new Rectangle(p.x, p.y - h / 2, w, h));
    }

    public void setRectRight(Graphics g, Point p, int padding) {
        int w = getWidth(g, padding);
        int h = getHeight(g, padding);
        setRect(new Rectangle(p.x - w, p.y - h / 2, w, h));
    }

    public int getWidth(Graphics g, int padding) {
        return g.getFontMetrics().stringWidth(text) + 2 * padding;
    }

    public int getHeight(Graphics g, int padding) {
        return mgsa.GraphicsUtils.getHeight(g) + 2 * padding;
    }

    public void drawCenter(Graphics g, Color mouseover, Color foreground, int miny, Point... points) {
        checkhighlight(g, mouseover, miny, points);
        g.setColor(foreground);
        mgsa.GraphicsUtils.drawCenterString(g, text, rect, false);
    }

    public void drawLeft(Graphics g, Color mouseover, Color foreground, int padding, int miny, Point... points) {
        checkhighlight(g, mouseover, miny, points);
        g.setColor(foreground);
        mgsa.GraphicsUtils.drawLeftString(g, text, rect, false, padding);
    }

    public void checkhighlight(Graphics g, Color mouseover, int miny, Point... points) {
        for (Point p : points) {
            if (contains(p) && p.y >= miny) {
                highlight(g, mouseover);
                break;
            }
        }
    }

    public void highlight(Graphics g, Color mouseover) {
        g.setColor(mouseover);
        mgsa.GraphicsUtils.fillRectangle(g, rect);
    }

    public boolean contains(Point p) {
        return p != null && rect.contains(p);
    }
}
