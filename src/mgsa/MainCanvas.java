package mgsa;

import java.awt.Graphics;
import javax.swing.JComponent;

public class MainCanvas extends JComponent {

    private Screen screen = new HomeScreen(this);

    @Override
    public void paintComponent(Graphics g) {
        screen.paintComponent(g, getWidth(), getHeight());
    }

    public void mousePressed() {
        screen.mousePressed();
    }

    public void mouseReleased() {
        screen.mouseReleased();
    }

    public void keyPressed(int key) {
        screen.keyPressed(key);
    }

    public void keyReleased(int key) {
        screen.keyReleased(key);
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}
