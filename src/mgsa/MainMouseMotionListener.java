package mgsa;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MainMouseMotionListener implements MouseMotionListener {
    
    private final MainCanvas canvas;
    
    public MainMouseMotionListener(MainCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
