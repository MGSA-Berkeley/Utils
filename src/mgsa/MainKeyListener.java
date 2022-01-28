package mgsa;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainKeyListener implements KeyListener {
    
    private final MainCanvas canvas;
    
    public MainKeyListener(MainCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
