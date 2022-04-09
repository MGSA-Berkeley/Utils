package officedraw;

import mgsa.Button;

public class Person {

    public final Button[] buttons;

    public Person(String name, String year, String priority, String group, String office) {
        buttons = new Button[]{new Button(name, null), new Button(year, null), new Button(priority, null), new Button(group, null), new Button(office, null)};
    }

    public Person() {
        this("", "", "", "", "");
    }
}
