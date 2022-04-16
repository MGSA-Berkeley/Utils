package officedraw;

import java.util.Arrays;

public class Sorting {

    public static Person[] nameSort(Person[] oldpeople) {
        int len = oldpeople.length - 1;
        Person[] sortedpeople = Arrays.copyOf(oldpeople, len);
        Arrays.sort(sortedpeople, Sorting::nameCompare);
        Person[] newpeople = Arrays.copyOf(sortedpeople, len + 1);
        newpeople[len] = new Person();
        return newpeople;
    }

    public static Person[] yearSort(Person[] oldpeople) {
        int len = oldpeople.length - 1;
        Person[] sortedpeople = Arrays.copyOf(oldpeople, len);
        Arrays.sort(sortedpeople, Sorting::yearCompare);
        Person[] newpeople = Arrays.copyOf(sortedpeople, len + 1);
        newpeople[len] = new Person();
        return newpeople;
    }

    public static Person[] prioritySort(Person[] oldpeople) {
        int len = oldpeople.length - 1;
        Person[] sortedpeople = Arrays.copyOf(oldpeople, len);
        Arrays.sort(sortedpeople, Sorting::priorityCompare);
        Person[] newpeople = Arrays.copyOf(sortedpeople, len + 1);
        newpeople[len] = new Person();
        return newpeople;
    }

    public static Person[] adjustmentSort(Person[] oldpeople) {
        int len = oldpeople.length - 1;
        Person[] sortedpeople = Arrays.copyOf(oldpeople, len);
        Arrays.sort(sortedpeople, Sorting::adjustmentCompare);
        Person[] newpeople = Arrays.copyOf(sortedpeople, len + 1);
        newpeople[len] = new Person();
        return newpeople;
    }

    public static Person[] blockSort(Person[] oldpeople) {
        int len = oldpeople.length - 1;
        Person[] sortedpeople = Arrays.copyOf(oldpeople, len);
        Arrays.sort(sortedpeople, Sorting::blockCompare);
        Person[] newpeople = Arrays.copyOf(sortedpeople, len + 1);
        newpeople[len] = new Person();
        return newpeople;
    }

    public static Person[] officeSort(Person[] oldpeople) {
        int len = oldpeople.length - 1;
        Person[] sortedpeople = Arrays.copyOf(oldpeople, len);
        Arrays.sort(sortedpeople, Sorting::officeCompare);
        Person[] newpeople = Arrays.copyOf(sortedpeople, len + 1);
        newpeople[len] = new Person();
        return newpeople;
    }

    public static int nameCompare(Person p, Person q) {
        return p.buttons[0].getText().compareTo(q.buttons[0].getText());
    }

    public static int yearCompare(Person p, Person q) {
        int i = Integer.MIN_VALUE;
        int j = Integer.MIN_VALUE;
        try {
            i = Integer.parseInt(p.buttons[1].getText());
        } catch (NumberFormatException ex) {
        }
        try {
            j = Integer.parseInt(q.buttons[1].getText());
        } catch (NumberFormatException ex) {
        }
        return Integer.compare(i, j);
    }

    public static int priorityCompare(Person p, Person q) {
        int i = Integer.MIN_VALUE;
        int j = Integer.MIN_VALUE;
        try {
            i = Integer.parseInt(p.buttons[2].getText());
        } catch (NumberFormatException ex) {
        }
        try {
            j = Integer.parseInt(q.buttons[2].getText());
        } catch (NumberFormatException ex) {
        }
        return Integer.compare(i, j);
    }

    public static int adjustmentCompare(Person p, Person q) {
        int i = Integer.MAX_VALUE;
        int j = Integer.MAX_VALUE;
        String s = p.buttons[3].getText();
        String t = q.buttons[3].getText();
        if (!s.isEmpty()) {
            try {
                i = Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                i = Integer.MIN_VALUE;
            }
        }
        if (!t.isEmpty()) {
            try {
                j = Integer.parseInt(t);
            } catch (NumberFormatException ex) {
                j = Integer.MIN_VALUE;
            }
        }
        return Integer.compare(i, j);
    }

    public static int blockCompare(Person p, Person q) {
        return 0;
    }

    public static int officeCompare(Person p, Person q) {
        return p.buttons[5].getText().compareTo(q.buttons[5].getText());
    }
}
