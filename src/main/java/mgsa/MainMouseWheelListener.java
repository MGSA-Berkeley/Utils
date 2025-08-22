package mgsa;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MainMouseWheelListener implements MouseWheelListener {

    private final MainCanvas canvas;

    private static final int bound = 30;

    public MainMouseWheelListener(MainCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            int n = e.getUnitsToScroll();
            if (n <= bound && n >= - bound) {
                canvas.mouseScrolled(e.getUnitsToScroll());
            }
        }
    }
}
