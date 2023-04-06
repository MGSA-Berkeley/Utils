package elections;

import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ElectionScreen implements mgsa.Screen {

    private final mgsa.MainCanvas canvas;
    
    private final mgsa.Button backbutton = new mgsa.Button("←", null);
    private final mgsa.Button exitbutton = new mgsa.Button("X", null);

    private final Set<Integer> keyset = new HashSet<>();
    private String paste = "";

    public ElectionScreen(mgsa.MainCanvas canvas) {
        this.canvas = canvas;
//        int bigpadding = 12;
//        int smallpadding = 5;
//        backbutton.setRectLeft(g, new Point(bigpadding - smallpadding, bannerheight / 2), smallpadding);
//        exitbutton.setRectRight(g, new Point(w - bigpadding + smallpadding, bannerheight / 2), smallpadding);
    }

    @Override
    public void paintComponent(Graphics g, int w, int h) {
        //GraphicsUtils.drawCenteredString(g, paste, new Rectangle(0, 0, w, h), true);
    }

    @Override
    public void mousePressed() {
    }

    @Override
    public void mouseReleased() {
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
                        LoadElection.loadElection(7, paste);
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
