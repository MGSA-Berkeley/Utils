package mgsa;

import java.awt.Graphics;

public interface Screen {
    
    public void paintComponent(Graphics g, int w, int h);
    
    public void mousePressed();
    
    public void mouseReleased();
}
