package officedraw;

import java.awt.Graphics;

public class OfficeDrawScreen implements mgsa.Screen {

    private final mgsa.MainCanvas canvas;

    public OfficeDrawScreen(mgsa.MainCanvas canvas) {
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
