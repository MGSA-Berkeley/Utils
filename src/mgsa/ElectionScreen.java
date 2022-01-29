package mgsa;

import elections.LoadElection;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class ElectionScreen implements Screen {

    private final MainCanvas canvas;
    private final Set<Integer> keyset = new HashSet<>();
    private String paste = "";

    public ElectionScreen(MainCanvas canvas) {
        this.canvas = canvas;
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
    public void keyPressed(int key) {
        keyset.add(key);
        if (keyset.contains(KeyEvent.VK_CONTROL) && key == KeyEvent.VK_V) {
            try {
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable t = cb.getContents(null);
                if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    String s = (String) t.getTransferData(DataFlavor.stringFlavor);
                    if (!paste.equals(s)) {
                        paste = s;
                        LoadElection.runElection(paste);
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(int key) {
        keyset.remove(key);
    }
}
