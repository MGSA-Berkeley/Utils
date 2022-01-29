package elections;

import java.util.Arrays;

public class ElectionManager {

    private static final String tab = "\t";
    private static final String newline = "\n";

    public static void runElection(String rawpaste) {
        String[] rawlines = rawpaste.split(newline);
        int numlines = rawlines.length;
        String[][] splitlines = new String[numlines][];
        for (int i = 0; i < numlines; i++) {
            splitlines[i] = rawlines[i].split(tab);
        }
        if (splitlines.length == 0) {
            throw new IllegalArgumentException("Did not find any lines");
        }
        String[] candidates = splitlines[0];
        int numcandidates = candidates.length;
        int numballots = numlines - 1;
        int[][] ballots = new int[numballots][numcandidates];
        for (int i = 0; i < numballots; i++) {
            if (splitlines[i + 1].length > numcandidates) {
                throw new IllegalArgumentException("Ballot" + (i + 1) + " has too many votes");
            }
            for (int j = 0; j < splitlines[i + 1].length; j++) {
                ballots[i][j] = parse(splitlines[i + 1][j]);
                if (ballots[i][j] < 0 || ballots[i][j] > numcandidates) {
                    throw new IllegalArgumentException("Ballot" + (i + 1) + " has an illegal vote");
                }
            }
            System.out.println(Arrays.toString(ballots[i]));
        }
        String[][] cleanballots = cleanballots(candidates, ballots);
        for (String[] s : cleanballots) {
            System.out.println(Arrays.toString(s));
        }
    }

    private static String[][] cleanballots(String[] candidates, int[][] ballots) {
        String[][] cleanballots = new String[ballots.length][];
        for (int i = 0; i < ballots.length; i++) {
            cleanballots[i] = cleanballot(candidates, ballots[i]);
        }
        return cleanballots;
    }

    private static String[] cleanballot(String[] candidates, int[] ballot) {
        int len = 0;
        for (int i : ballot) {
            if (i != 0) {
                len++;
            }
        }
        String[] cleanballot = new String[len];
        len = 0;
        for (int i = 1; i <= candidates.length; i++) {
            for (int j = 0; j < ballot.length; j++) {
                if (ballot[j] == i) {
                    cleanballot[len++] = candidates[j];
                    break;
                }
            }
        }
        if (len < cleanballot.length) {
            throw new IllegalArgumentException("Ballot has same rank twice");
        }
        return cleanballot;
    }

    private static int parse(String s) {
        if (!s.matches("^[0-9]*[^0-9]*$")) {
            throw new IllegalArgumentException("Cannot parse vote: " + s);
        }
        String t = s.replaceAll("[^0-9]", "");
        if (t.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(t);
    }
}
