package mgsa;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class HomeScreen implements Screen {

    private final MainCanvas canvas;

    private final Button title = new Button("MGSA Utils", null);
    private final Button officedraw = new Button("Office Draw", null);
    private final Button election = new Button("Election", null);
    private final Button exit = new Button("Exit", null);

    private final Set<Integer> keyset = new HashSet<>();

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
        g.setFont(bigfont);
        title.setRectCenter(g, new Point(w / 2, h / 3), padding);
        g.setFont(smallfont);
        officedraw.setRectCenter(g, new Point(w / 2, h / 2), padding);
        election.setRectCenter(g, new Point(w / 2, 5 * h / 8), padding);
        exit.setRectCenter(g, new Point(w / 2, 3 * h / 4), padding);
        // *** PAINT THE CANVAS ***
        Point mouse = canvas.getMousePosition();
        g.setColor(background);
        g.fillRect(0, 0, w, h);
        g.setFont(bigfont);
        title.drawCenter(g, mouseover, foreground, 0);
        g.setFont(smallfont);
        officedraw.drawCenter(g, mouseover, foreground, 0, mouse, click);
        election.drawCenter(g, mouseover, foreground, 0, mouse, click);
        exit.drawCenter(g, mouseover, foreground, 0, mouse, click);
    }

    @Override
    public void mousePressed() {
        click = canvas.getMousePosition();
    }

    @Override
    public void mouseReleased() {
        Point p = canvas.getMousePosition();
        if (officedraw.contains(p) && officedraw.contains(click)) {
            canvas.setScreen(new officedraw.OfficeDrawScreen(canvas));
        }
        if (election.contains(p) && election.contains(click)) {
            canvas.setScreen(new elections.ElectionScreen(canvas));
        }
        if (exit.contains(p) && exit.contains(click)) {
            System.exit(0);
        }
        click = null;
    }

    @Override
    public void mouseScrolled(int n) {
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
