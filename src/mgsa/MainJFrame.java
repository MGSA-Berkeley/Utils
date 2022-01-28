package mgsa;

import javax.swing.JFrame;

public class MainJFrame extends JFrame {

    private final MainCanvas canvas = new MainCanvas();
    private final MainKeyListener keylistener = new MainKeyListener(canvas);
    private final MainMouseListener mouselistener = new MainMouseListener(canvas);
    private final MainMouseMotionListener mousemotionlistener = new MainMouseMotionListener(canvas);
    private final MainMouseWheelListener mousewheellistener = new MainMouseWheelListener(canvas);

    public MainJFrame() {
        init();
    }

    private void init() {
        setTitle("MGSA Utils");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        add(canvas);

        addKeyListener(keylistener);
        addMouseListener(mouselistener);
        addMouseMotionListener(mousemotionlistener);
        addMouseWheelListener(mousewheellistener);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    }
}
