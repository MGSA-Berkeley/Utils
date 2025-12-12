package elections;

import elections.LoadElection.ElectionData;
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
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

public class ElectionScreen implements mgsa.Screen {
    
    private final mgsa.MainCanvas canvas;
    
    private int numseats = 7;
    private ElectionData data = null;
    private final int year = Calendar.getInstance().get(Calendar.YEAR);
    private final String semester = Calendar.getInstance().get(Calendar.MONTH) < 6 ? "Spring" : "Fall";
    private final mgsa.Button title = new mgsa.Button(semester + " " + year + " MGSA Election", null);
    private final mgsa.Button backbutton = new mgsa.Button("←", null);
    private final mgsa.Button exitbutton = new mgsa.Button("X", null);
    private final mgsa.Button instructions = new mgsa.Button(null, null);
    private final mgsa.Button pastebutton = new mgsa.Button("Paste (Ctrl-V)", null);
    private final mgsa.Button ballots = new mgsa.Button("0 Ballots", null);
    private final mgsa.Button candidates = new mgsa.Button("0 Candidates", null);
    private final mgsa.Button seats = new mgsa.Button(numseats + " Seats", null);
    private final mgsa.Button savebutton = new mgsa.Button("Save (Ctrl-S)", null);
    private final mgsa.Button error = new mgsa.Button("", null);
    
    private final Set<Integer> keyset = new HashSet<>();
    private static final String newline = "\n";
    private static final String tab = "\t";
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
        rowheight = Math.max(rowheight, error.getHeight(g, smallpadding));
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, w, bannerheight + rowheight);
        g.setFont(BIGFONT);
        Point mouse = canvas.getMousePosition();
        title.drawCenter(g, MOUSEOVER, FOREGROUND, 0, mouse, click);
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
        int errorpos = savebuttonpos + rowheight + smallpadding;
        instructions.setRect(new Rectangle(bigpadding, instructionspos, instructions.getWidth(g, smallpadding), rowheight));
        pastebutton.setRect(new Rectangle(bigpadding, pastebuttonpos, pastebutton.getWidth(g, smallpadding), rowheight));
        ballots.setRect(new Rectangle(bigpadding, ballotspos, ballots.getWidth(g, smallpadding), rowheight));
        candidates.setRect(new Rectangle(bigpadding, candidatespos, candidates.getWidth(g, smallpadding), rowheight));
        seats.setRect(new Rectangle(bigpadding, seatspos, seats.getWidth(g, smallpadding), rowheight));
        savebutton.setRect(new Rectangle(bigpadding, savebuttonpos, savebutton.getWidth(g, smallpadding), rowheight));
        error.setRect(new Rectangle(bigpadding, errorpos, error.getWidth(g, smallpadding), rowheight));
        instructions.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0);
        pastebutton.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
        ballots.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0);
        candidates.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0);
        seats.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
        savebutton.drawLeft(g, MOUSEOVER, FOREGROUND, smallpadding, 0, mouse, click);
        error.drawLeft(g, MOUSEOVER, Color.RED, smallpadding, 0);
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
        if (pastebutton.contains(p) && pastebutton.contains(click)) {
            paste();
        }
        if (savebutton.contains(p) && savebutton.contains(click)) {
            save();
        }
        if (title.contains(p) && title.contains(click)) {
            title.setText(JOptionPane.showInputDialog("Election Title:", title.getText()));
        }
        if (seats.contains(p) && seats.contains(click)) {
            String s = JOptionPane.showInputDialog("Number of Seats:", numseats);
            try {
                int k = Integer.parseInt(s);
                if (k <= 0) {
                    throw new IllegalArgumentException();
                }
                numseats = k;
                seats.setText(numseats+" Seats");
                error.setText("");
            } catch (Exception ex) {
                error.setText("Invalid number of seats: "+s);
            }
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
            paste();
        }
        if (keyset.contains(KeyEvent.VK_CONTROL) && key == KeyEvent.VK_S) {
            save();
        }
    }

    // why isn't empty throwing an error?
    private void paste() {
        try {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable t = cb.getContents(null);
            if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String s = (String) t.getTransferData(DataFlavor.stringFlavor);
                if (!paste.equals(s)) {
                    paste = s;
                    ElectionData data = LoadElection.loadElection(paste);
                    if (data.candidates.length <= numseats) {
                        throw new IllegalArgumentException("There are " + data.candidates.length + " candidates for " + numseats + " seats, so there is no point holding an election.");
                    }
                    this.data = data;
                    candidates.setText(data.candidates.length + " candidates");
                    ballots.setText(data.ballots.length + " ballots");
                    error.setText("");
                }
            }
        } catch (HeadlessException | UnsupportedFlavorException | IllegalArgumentException | IOException ex) {
            error.setText(ex.getMessage());
            data = null;
        }
    }
    
    private void save() {
        if (data == null) {
            error.setText("No valid data has been pasted yet");
            return;
        }
        List<ElectionState> record = RunElection.runElection(numseats, data.candidates.length, data.ballots);
        try {
            DisplayElection.displayElection(numseats, data.candidates, record, title.getText());
            error.setText("");
        } catch (IOException ex) {
            error.setText(ex.getMessage());
        }
    }
    
    @Override
    public void keyReleased(int key) {
        keyset.remove(key);
    }
}
