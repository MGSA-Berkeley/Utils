package mgsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
