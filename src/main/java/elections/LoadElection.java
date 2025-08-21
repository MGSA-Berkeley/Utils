package elections;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoadElection {

    private static final String newline = "\n";
    private static final String tab = "\t";

    public static void loadElection(int numseats, String rawpaste, String title) throws IOException {
        String[] rawlines = rawpaste.split(newline);
        int numballots = rawlines.length;
        String[][] splitlines = new String[numballots][];
        Set<String> candidatelist = new HashSet<>();
        for (int i = 0; i < numballots; i++) {
            splitlines[i] = rawlines[i].split(tab);
            for (String candidate : splitlines[i]) {
                if (!candidate.isEmpty()) {
                    candidatelist.add(candidate);
                }
            }
        }
        int numcandidates = candidatelist.size();
        if (numcandidates < numseats) {
            throw new IllegalArgumentException("There are " + numcandidates + " for " + numseats + " seats, so there is no point holding an election.");
        }
        String[] candidates = new String[numcandidates];
        int len = 0;
        for (String candidate : candidatelist) {
            candidates[len++] = candidate;
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
                for (int j = 0; j < numcandidates; j++) {
                    if (candidate.equals(candidates[j])) {
                        ballots[i][len++] = j;
                    }
                }
            }
        }
        List<ElectionState> record = RunElection.runElection(numseats, candidates.length, ballots);
        DisplayElection.displayElection(numseats, candidates, record, title);
    }
}
