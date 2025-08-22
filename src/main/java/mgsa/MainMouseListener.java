package mgsa;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainMouseListener implements MouseListener {
    
    private final MainCanvas canvas;
    
    public MainMouseListener(MainCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        canvas.mousePressed();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        canvas.mouseReleased();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
