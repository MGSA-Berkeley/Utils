package officedraw;

import java.awt.Graphics;
import mgsa.MainCanvas;
import mgsa.Screen;

public class OfficeDrawScreen implements Screen {

    private final MainCanvas canvas;

    public OfficeDrawScreen(MainCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void paintComponent(Graphics g, int w, int h) {
    }

    @Override
    public void mousePressed() {
    }

    @Override
    public void mouseReleased() {
    }

    @Override
    public void keyPressed(int key) {
    }

    @Override
    public void keyReleased(int key) {
    }
}
