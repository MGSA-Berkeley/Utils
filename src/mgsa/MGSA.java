package mgsa;

import java.io.IOException;

public class MGSA {

    public static void main(String[] args) throws InterruptedException, IOException {
        officedraw.Offices.init();
        MainJFrame frame = new MainJFrame();
        while (true) {
            frame.repaint();
            Thread.sleep(15);
        }
    }
}
