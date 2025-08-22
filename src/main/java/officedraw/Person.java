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
                s = s.replace("  ", " ");
                while (!s.isEmpty() && s.charAt(s.length() - 1) == ' ') {
                    s = s.substring(0, s.length() - 1);
                }
                if (s.equals("Alexander Sherman")) {
                    s = "Alex Sherman";
                }
                if (s.equals("Alexander Youcis")) {
                    s = "Alex Youcis";
                }
                if (s.equals("Alexander Bertoloni")) {
                    s = "Alexander Bertoloni Meli";
                }
                if (s.equals("Archit Kukarni")) {
                    s = "Archit Kulkarni";
                }
                if (s.equals("Benjamin Gamage")) {
                    s = "Benjamin Gammage";
                }
                if (s.equals("Thomas Mack-Crane")) {
                    s = "Sander Mack-Crane";
                }
                if (s.equals("Patrick George Lutz")) {
                    s = "Patrick Lutz";
                }
                if (s.equals("Maxim Wimberley")) {
                    s = "Max Wimberley";
                }
                if (s.equals("Paula Elisabeth Burkhardt")) {
                    s = "Paula Burkhardt-Guim";
                }
                if (s.equals("Mohandes Pillai")) {
                    s = "Mohandas Pillai";
                }
                if (s.equals("Christopher Ling-Po Kuo")) {
                    s = "Christopher Kuo";
                }
                if (s.equals("Luke Pernal Corcos")) {
                    s = "Luke Corcos";
                }
                if (s.equals("Michael Ross Franco")) {
                    s = "Michael Franco";
                }
                if (s.equals("Meredith Anne Shea")) {
                    s = "Meredith Shea";
                }
                if (s.equals("Melissa Ulrika Sherman-Bennett")) {
                    s = "Melissa Sherman-Bennett";
                }
                if (s.equals("Madeleine Aster Weinstein")) {
                    s = "Maddie Weinstein";
                }
                if (s.equals("Magda Hlavacek")) {
                    s = "Max Hlavacek";
                }
                if (s.equals("Mikayla Lynn Kelley")) {
                    s = "Mikayla Kelley";
                }
                if (s.equals("German Ezequiel Stefanich")) {
                    s = "German Stefanich";
                }
                if (s.equals("Milind Hedge")) {
                    s = "Milind Hegde";
                }
                if (s.equals("Zahra Ahmadianhosseini")) {
                    s = "Raha Ahmadian";
                }
                if (s.equals("Russel Buehler")) {
                    s = "Russell Ahmed-Buehler";
                }
                if (s.equals("Kevin ONeill")) {
                    s = "Kevin O'Neill";
                }
                if (s.equals("Qiaoyu Yang")) {
                    s = "Chiao-Yu Yang";
                }
                if (s.equals("Alexander Appleton")) {
                    s = "Alex Appleton";
                }
                if (s.equals("Alexander Rusciano")) {
                    s = "Alex Rusciano";
                }
                if (s.equals("Chao Kusollershariya")) {
                    s = "Chao Kusollerschariya";
                }
                if (s.equals("Cristian Munteanu")) {
                    s = "Cristian Mihai Munteanu";
                }
                if (s.equals("Isabelle Susheela Shankar")) {
                    s = "Isabelle Shankar";
                }
                if (s.equals("Jeffrey Hicks")) {
                    s = "Jeff Hicks";
                }
                if (s.equals("Kyeong Sik Nam")) {
                    s = "Kyeongsik Nam";
                }
                if (s.equals("Mariel Josephine Supina")) {
                    s = "Mariel Supina";
                }
                if (s.equals("Rockford Foster")) {
                    s = "Rockford Sison";
                }
                if (s.equals("Katherine Grace Christianson")) {
                    s = "Michael Christianson";
                }
                if (s.equals("Ka Kin Kenneth Hung")) {
                    s = "Kenneth Hung";
                }
            }
            buttons[i] = new Button(s, null);
        }
        warning = new Button("", null);
    }

    public Person() {
        this("", "", "", "", "", "");
    }

    public boolean blank() {
        for (Button b : buttons) {
            if (!b.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
