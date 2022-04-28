package officedraw;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    public static Person[] blockSort(Person[] oldpeople, int year) {
        int len = oldpeople.length - 1;
        Person[] sortedpeople = Arrays.copyOf(oldpeople, len);
        Map<String, Integer> numerators = new HashMap<>();
        Map<String, Integer> denominators = new HashMap<>();
        for (Person p : sortedpeople) {
            String s = block(p);
            if (!denominators.containsKey(s)) {
                numerators.put(s, 0);
                denominators.put(s, 0);
            }
            if (numerators.containsKey(s)) {
                try {
                    numerators.put(s, numerators.get(s) + Integer.parseInt(p.buttons[2].getText()));
                } catch (NumberFormatException ex) {
                    numerators.remove(s);
                }
            }
            denominators.put(s, denominators.getOrDefault(s, 0) + 1);
        }
        Map<String, BigFraction> fractions = new HashMap<>();
        for (String s : numerators.keySet()) {
            fractions.put(s, new BigFraction(numerators.get(s), denominators.get(s)));
        }
        Arrays.sort(sortedpeople, (p, q) -> blockCompare(p, q, fractions, year));
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

    public static Person[] warningsSort(Person[] oldpeople) {
        int len = oldpeople.length - 1;
        Person[] sortedpeople = Arrays.copyOf(oldpeople, len);
        Arrays.sort(sortedpeople, Sorting::warningsCompare);
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

    public static int blockCompare(Person p, Person q, Map<String, BigFraction> fractions, int year) {
        String sp = p.buttons[4].getText();
        String sq = q.buttons[4].getText();
        String tp = block(p);
        String tq = block(q);
        if (tp.equals(tq)) {
            return p.buttons[0].getText().compareTo(q.buttons[0].getText());
        }
        if (tp.isEmpty() || tq.isEmpty()) {
            return tp.isEmpty() ? (tq.isEmpty() ? 0 : -1) : 1;
        }
        if (!(fractions.containsKey(tp) && fractions.containsKey(tq))) {
            return fractions.containsKey(tp) ? 1 : (fractions.containsKey(tq) ? -1 : 0);
        }
        if (sp.equals("Squat") != sq.equals("Squat")) {
            return sp.equals("Squat") ? -1 : 1;
        }
        int comparison = fractions.get(tp).compareTo(fractions.get(tq));
        // This tiebreak is exploitable, but last-minute changes won't scramble the pick order
        return comparison == 0 ? Integer.compare((tp + year).hashCode(), (tq + year).hashCode()) : comparison;
    }

    public static int officeCompare(Person p, Person q) {
        int i = Integer.MIN_VALUE;
        int j = Integer.MIN_VALUE;
        try {
            i = Integer.parseInt(p.buttons[5].getText());
        } catch (NumberFormatException ex) {
        }
        try {
            j = Integer.parseInt(q.buttons[5].getText());
        } catch (NumberFormatException ex) {
        }
        return Integer.compare(i, j);
    }

    public static int warningsCompare(Person p, Person q) {
        String s = p.warning.getText();
        String t = q.warning.getText();
        if (s.isEmpty() || t.isEmpty()) {
            return s.isEmpty() ? (t.isEmpty() ? 0 : 1) : -1;
        }
        return p.warning.getText().compareTo(q.warning.getText());
    }

    public static String block(Person person) {
        String block = person.buttons[4].getText();
        if (block.equals("Float")) {
            block += person.buttons[0].getText();
        } else if (block.equals("Squat")) {
            block += person.buttons[5].getText();
        } else if (!block.startsWith("Block")) {
            block = "";
        }
        return block;
    }
}
