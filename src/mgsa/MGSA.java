package mgsa;

public class MGSA {

    public static void main(String[] args) throws InterruptedException {
        officedraw.Offices.init();
        MainJFrame frame = new MainJFrame();
        while (true) {
            frame.repaint();
            Thread.sleep(15);
        }
    }
}
