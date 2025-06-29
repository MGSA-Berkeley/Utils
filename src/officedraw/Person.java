package officedraw;

import mgsa.Button;

public class Person {

    public final Button[] buttons;
    public final Button warning;

    public Person(String... data) {
        int len = 6;
        if (data.length != len) {
            throw new IllegalArgumentException();
        }
        buttons = new Button[len];
        for (int i = 0; i < len; i++) {
            String s = data[i];
            if (i == 0) {
                s = s.replace("  "," ");
            }
            buttons[i] = new Button(s, null);
        }
        warning = new Button("", null);
    }

    public Person() {
        this("", "", "", "", "", "");
    }
}
