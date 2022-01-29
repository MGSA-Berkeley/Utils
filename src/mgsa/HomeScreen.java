package mgsa;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class HomeScreen implements Screen {

    private final MainCanvas canvas;

    private Rectangle titlepos;
    private Rectangle officedrawpos;
    private Rectangle electionpos;
    private Rectangle exitpos;

    private Point click;

    private static final Color background = GraphicsUtils.Grey;
    private static final Color foreground = GraphicsUtils.Black;
    private static final Color mouseover = GraphicsUtils.BayFog;

    private static final Font bigfont = new Font(Font.SANS_SERIF, Font.BOLD, 72);
    private static final Font smallfont = new Font(Font.SANS_SERIF, Font.BOLD, 48);

    public HomeScreen(MainCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void paintComponent(Graphics g, int w, int h) {
        int padding = 8;
        int thickness = 3;
        String titletext = "MGSA Utils";
        String officedrawtext = "Office Draw";
        String electiontext = "Election";
        String exittext = "Exit";
        g.setFont(bigfont);
        titlepos = GraphicsUtils.getRectangle(g, titletext, new Point(w / 2, h / 3), false, padding);
        g.setFont(smallfont);
        officedrawpos = GraphicsUtils.getRectangle(g, officedrawtext, new Point(w / 2, h / 2), false, padding);
        electionpos = GraphicsUtils.getRectangle(g, electiontext, new Point(w / 2, 5 * h / 8), false, padding);
        exitpos = GraphicsUtils.getRectangle(g, exittext, new Point(w / 2, 3 * h / 4), false, padding);
        g.setColor(background);
        g.fillRect(0, 0, w, h);
        g.setColor(mouseover);
        Point p = canvas.getMousePosition();
        if (p != null) {
            if (officedrawpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, officedrawpos);
            }
            if (electionpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, electionpos);
            }
            if (exitpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, exitpos);
            }
        }
        p = click;
        if (p != null) {
            if (officedrawpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, officedrawpos);
            }
            if (electionpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, electionpos);
            }
            if (exitpos.contains(p)) {
                GraphicsUtils.fillRectangle(g, exitpos);
            }
        }
        g.setColor(foreground);
        g.setFont(bigfont);
        GraphicsUtils.drawCenteredString(g, titletext, titlepos, false);
        g.setFont(smallfont);
        GraphicsUtils.drawCenteredString(g, officedrawtext, officedrawpos, false);
        GraphicsUtils.drawCenteredString(g, electiontext, electionpos, false);
        GraphicsUtils.drawCenteredString(g, exittext, exitpos, false);
        GraphicsUtils.drawRectangle(g, officedrawpos, thickness);
        GraphicsUtils.drawRectangle(g, electionpos, thickness);
        GraphicsUtils.drawRectangle(g, exitpos, thickness);
    }

    @Override
    public void mousePressed() {
        click = canvas.getMousePosition();
    }

    @Override
    public void mouseReleased() {
        Point p = canvas.getMousePosition();
        if (p != null && click != null) {
            if (officedrawpos.contains(p) && officedrawpos.contains(click)) {
                canvas.setScreen(new OfficeDrawScreen(canvas));
            }
            if (electionpos.contains(p) && electionpos.contains(click)) {
                canvas.setScreen(new ElectionScreen(canvas));
            }
            if (exitpos.contains(p) && exitpos.contains(click)) {
                System.exit(0);
            }
        }
        click = null;
    }

    @Override
    public void keyPressed(int key) {
    }

    @Override
    public void keyReleased(int key) {
    }
}
