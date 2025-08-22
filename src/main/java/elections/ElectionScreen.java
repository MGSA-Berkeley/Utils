package elections;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class ElectionScreen implements mgsa.Screen {

    private final mgsa.MainCanvas canvas;

    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private String semester = Calendar.getInstance().get(Calendar.MONTH) < 6 ? "Spring" : "Fall";
    private final mgsa.Button title = new mgsa.Button(semester + " " + year + " MGSA Election", null);
    private final mgsa.Button backbutton = new mgsa.Button("←", null);
    private final mgsa.Button exitbutton = new mgsa.Button("X", null);
    private final mgsa.Button instructions = new mgsa.Button(null, null);
    private final mgsa.Button pastebutton = new mgsa.Button("Paste (Ctrl-V)", null);
    private final mgsa.Button ballots = new mgsa.Button("0 Ballots", null);
    private final mgsa.Button candidates = new mgsa.Button("0 Candidates", null);
    private final mgsa.Button seats = new mgsa.Button("7 Seats", null);
    private final mgsa.Button savebutton = new mgsa.Button("Save (Ctrl-S)", null);

    private final Set<Integer> keyset = new HashSet<>();
    private String paste = "";

    private Point click;

    private static final Color BACKGROUND = mgsa.GraphicsUtils.Grey;
    private static final Color FOREGROUND = mgsa.GraphicsUtils.Black;
    private static final Color MOUSEOVER = mgsa.GraphicsUtils.BayFog;

    private static final Font BIGFONT = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    private static final Font SMALLFONT = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

    public ElectionScreen(mgsa.MainCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void paintComponent(Graphics g, int w, int h) {
        int bigpadding = 12;
        int smallpadding = 5;
        g.setFont(BIGFONT);
        int bannerheight = exitbutton.getHeight(g, bigpadding);
        title.setRectCenter(g, new Point(w / 2, bannerheight / 2), smallpadding);
        backbutton.setRectLeft(g, new Point(bigpadding - smallpadding, bannerheight / 2), smallpadding);
        exitbutton.setRectRight(g, new Point(w - bigpadding + smallpadding, bannerheight / 2), smallpadding);
        g.setFont(SMALLFONT);
        instructions.setText("Paste a CSV file. Each row should be a ballot listing candidates in order of preference.");
        int rowheight = 0;
        rowheight = Math.max(rowheight, instructions.getHeight(g, smallpadding));
        rowheight = Math.max(rowheight, pastebutton.getHeight(g, smallpadding));
        rowheight = Math.max(rowheight, ballots.getHeight(g, smallpadding));
        rowheight = Math.max(rowheight, candidates.getHeight(g, smallpadding));
        rowheight = Math.max(rowheight, seats.getHeight(g, smallpadding));
        rowheight = Math.max(rowheight, savebutton.getHeight(g, smallpadding));
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, w, bannerheight + rowheight);
        g.setFont(BIGFONT);
        Point mouse = canvas.getMousePosition();
        title.drawCenter(g, MOUSEOVER, FOREGROUND, 0);
        backbutton.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);
        exitbutton.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);
        g.drawLine(0, bannerheight, w, bannerheight);
        g.setFont(SMALLFONT);
        int instructionspos = bannerheight + bigpadding;
        int pastebuttonpos = instructionspos + rowheight + smallpadding;
        int ballotspos = pastebuttonpos + rowheight + smallpadding;
        int candidatespos = ballotspos + rowheight + smallpadding;
        int seatspos = candidatespos + rowheight + smallpadding;
        int savebuttonpos = seatspos + rowheight + smallpadding;
        instructions.setRect(new Rectangle(bigpadding, instructionspos, instructions.getWidth(g, smallpadding), rowheight));
        pastebutton.setRect(new Rectangle(bigpadding, pastebuttonpos, pastebutton.getWidth(g, smallpadding), rowheight));
        ballots.setRect(new Rectangle(bigpadding, ballotspos, ballots.getWidth(g, smallpadding), rowheight));
        candidates.setRect(new Rectangle(bigpadding, candidatespos, candidates.getWidth(g, smallpadding), rowheight));
        seats.setRect(new Rectangle(bigpadding, seatspos, seats.getWidth(g, smallpadding), rowheight));
        savebutton.setRect(new Rectangle(bigpadding, savebuttonpos, savebutton.getWidth(g, smallpadding), rowheight));
        instructions.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0);
        pastebutton.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
        ballots.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0);
        candidates.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0);
        seats.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
        savebutton.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
    }

    @Override
    public void mousePressed() {
        click = canvas.getMousePosition();
    }

    @Override
    public void mouseReleased() {
        Point p = canvas.getMousePosition();
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
    }

    @Override
    public void keyPressed(int key) {
        keyset.add(key);
        if (keyset.contains(KeyEvent.VK_CONTROL) && key == KeyEvent.VK_Q) {
            System.exit(0);
        }
        if (keyset.contains(KeyEvent.VK_CONTROL) && key == KeyEvent.VK_V) {
            try {
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable t = cb.getContents(null);
                if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    String s = (String) t.getTransferData(DataFlavor.stringFlavor);
                    if (!paste.equals(s)) {
                        paste = s;
                        // Todo: Let user adjust number of seats
                        LoadElection.loadElection(7, paste, title.getText());
                    }
                }
            } catch (HeadlessException | UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace(System.out);
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(int key) {
        keyset.remove(key);
    }
}
