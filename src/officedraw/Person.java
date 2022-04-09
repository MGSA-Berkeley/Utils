package officedraw;

import mgsa.Button;

public class Person {

    public final Button[] buttons;

    public Person(String... data) {
        if (data.length != 5) {
            throw new IllegalArgumentException();
        }
        buttons = new Button[]{new Button(data[0], null), new Button(data[1], null), new Button(data[2], null), new Button(data[3], null), new Button(data[4], null)};
    }

    public Person() {
        this("", "", "", "", "");
    }
}
