package mgsa;

public class MGSA {

    public static void main(String[] args) throws InterruptedException {
        MainJFrame frame = new MainJFrame();
        while (true) {
            frame.repaint();
            Thread.sleep(15);
        }
    }
}
