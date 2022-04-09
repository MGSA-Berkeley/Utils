package officedraw;

import mgsa.Button;

public class Person {

    public final Button name;
    public final Button year;
    public final Button priority;
    public final Button group;
    public final Button office;

    public Person(String name, String year, String priority, String group, String office) {
        this.name = new Button(name, null);
        this.year = new Button(year, null);
        this.priority = new Button(priority, null);
        this.group = new Button(group, null);
        this.office = new Button(office, null);
    }
}
