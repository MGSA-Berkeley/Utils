package elections;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoadElection {

    private static final String newline = "\n";
    private static final String tab = "\t";

    public static class ElectionData {

        public final String[] candidates;
        public final int[][] ballots;

        public ElectionData(String[] candidates, int[][] ballots) {
            this.candidates = candidates;
            this.ballots = ballots;
        }
    }

    public static ElectionData loadElection(String rawpaste) throws IOException {
        String[] rawlines = rawpaste.split(newline);
        int numlines = rawlines.length;
        if (numlines == 0) {
            throw new IllegalArgumentException("Clipboard did not contain any data");
        }
        String[][] splitlines = new String[numlines][];
        for (int i = 0; i < numlines; i++) {
            splitlines[i] = rawlines[i].split(tab);
        }
        boolean google = true;
        for (int i = 1; i < numlines; i++) {
            for (String s : splitlines[i]) {
                if (!s.isEmpty() && !s.matches("^[0-9]+[^0-9]*$")) {
                    google = false;
                }
            }
        }
        if (google) {
            return parseGoogle(splitlines);
        } else {
            // Technically any spreadsheet is a valid CalLink, so we can't validate the data, but it will be pretty clear if something is messed up
            return parseCalLink(splitlines);
        }
    }

    private static ElectionData parseGoogle(String[][] splitlines) {
        String[] candidates = splitlines[0];
        int numcandidates = candidates.length;
        int numballots = splitlines.length - 1;
        int[][] ballots = new int[numballots][numcandidates];
        for (int i = 0; i < numballots; i++) {
            if (splitlines[i + 1].length > numcandidates) {
                throw new IllegalArgumentException("Ballot " + (i + 1) + " has too many votes: " + Arrays.toString(splitlines[i + 1]));
            }
            for (int j = 0; j < splitlines[i + 1].length; j++) {
                if (splitlines[i + 1][j].isEmpty()) {
                    continue;
                }
                String s = splitlines[i + 1][j].replaceAll("[^0-9]", "");
                ballots[i][j] = Integer.parseInt(s);
                if (ballots[i][j] <= 0 || ballots[i][j] > numcandidates) {
                    throw new IllegalArgumentException("Ballot " + (i + 1) + " has an invalid vote: " + splitlines[i + 1][j]);
                }
            }
            int len = 0;
            for (int j : ballots[i]) {
                if (j != 0) {
                    len++;
                }
            }
            int[] ballot = new int[len];
            len = 0;
            for (int j = 1; j <= numcandidates; j++) { // run through each valid rank
                for (int k = 0; k < ballots[i].length; k++) { // run through each candidate
                    if (ballots[i][k] == j) { // is that candidate given that rank?
                        ballot[len++] = k;
                        break;
                    }
                }
            }
            if (len < ballot.length) {
                throw new IllegalArgumentException("Ballot " + (i + 1) + " has same rank twice: " + Arrays.toString(splitlines[i + 1]));
            }
            ballots[i] = ballot;
        }
        return new ElectionData(candidates, ballots);
    }

    private static ElectionData parseCalLink(String[][] splitlines) {
        int numballots = splitlines.length;
        Set<String> candidatelist = new HashSet<>();
        for (int i = 0; i < numballots; i++) {
            for (String candidate : splitlines[i]) {
                if (!candidate.isEmpty()) {
                    candidatelist.add(candidate);
                }
            }
        }
        int numcandidates = candidatelist.size();
        String[] candidates = new String[numcandidates];
        Map<String, Integer> lookup = new HashMap<>();
        int len = 0;
        for (String candidate : candidatelist) {
            candidates[len] = candidate;
            lookup.put(candidate, len);
            len++;
        }
        int[][] ballots = new int[numballots][];
        for (int i = 0; i < numballots; i++) {
            len = 0;
            for (String candidate : splitlines[i]) {
                if (!candidate.isEmpty()) {
                    len++;
                }
            }
            ballots[i] = new int[len];
            len = 0;
            for (String candidate : splitlines[i]) {
                if (!candidate.isEmpty()) {
                    ballots[i][len++] = lookup.get(candidate);
                }
            }
        }
        return new ElectionData(candidates, ballots);
    }
}
