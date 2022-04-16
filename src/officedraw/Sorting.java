package officedraw;

import java.util.Arrays;

public class Sorting {

    public static Person[] nameSort(Person[] oldpeople) {
        int len = oldpeople.length - 1;
        Person[] sortedpeople = Arrays.copyOf(oldpeople, len);
        Arrays.sort(sortedpeople, (a, b) -> a.buttons[0].getText().compareTo(b.buttons[0].getText()));
        Person[] newpeople = Arrays.copyOf(sortedpeople, len + 1);
        newpeople[len] = new Person();
        return newpeople;
    }

    public static Person[] yearSort(Person[] oldpeople) {
        return oldpeople;
    }

    public static Person[] prioritySort(Person[] oldpeople) {
        return oldpeople;
    }

    public static Person[] adjustmentSort(Person[] oldpeople) {
        return oldpeople;
    }

    public static Person[] blockSort(Person[] oldpeople) {
        return oldpeople;
    }
    
    public static Person[] officeSort(Person[] oldpeople) {
        return oldpeople;
    }
}
