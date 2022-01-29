package elections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Runs Meek STV. Implements
 * https://prfound.org/resources/reference/reference-meek-rule/ without the
 * rounding.
 */
public class RunElection {

    private static final double epsilon = 0.000000001;
    private static final double omega = 0.000001;

    public static List<ElectionState> runElection(int numseats, int numcandidates, int[][] ballots) {
        List<ElectionState> record = new ArrayList<>();
        stepA(numseats, numcandidates, ballots, record);
        return record;
    }

    private static void stepA(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record) {
        CandidateState[] candidatestates = new CandidateState[numcandidates];
        Arrays.fill(candidatestates, CandidateState.HOPEFUL);
        double[] keepfactors = new double[numcandidates];
        Arrays.fill(keepfactors, 1);
        double[] votes = new double[numcandidates];
        ElectionState electionstate = new ElectionState(candidatestates, keepfactors, votes, 0, Double.MAX_VALUE, 0);
        stepB1(numseats, numcandidates, ballots, record, electionstate);
    }

    private static void stepB1(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current) {
        int numhopeful = 0;
        int numelected = 0;
        for (int candidate = 0; candidate < numcandidates; candidate++) {
            CandidateState candidatestate = current.getCandidateState(candidate);
            if (candidatestate == CandidateState.HOPEFUL) {
                numhopeful++;
            } else if (candidatestate == CandidateState.ELECTED) {
                numelected++;
            }
        }
        if (numelected >= numseats || numhopeful + numelected <= numseats) {
            stepC();
        }
        stepB2a(numseats, numcandidates, ballots, record, current);
    }

    private static void stepB2a(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current) {
        int numballots = ballots.length;
        for (int ballot = 0; ballot < numballots; ballot++) {
            double weight = 1;
            for (int candidate : ballots[ballot]) {
                double keepfactor = current.getKeepFactor(candidate);
                double vote = weight * keepfactor;
                current = current.addVote(candidate, vote);
                if (keepfactor == 1) {
                    break;
                }
                weight -= vote;
            }
        }
        stepB2b(numseats, numcandidates, ballots, record, current);
    }

    private static void stepB2b(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current) {
        double quota = 0;
        for (int candidate = 0; candidate < numcandidates; candidate++) {
            double vote = current.getVote(candidate);
            quota += vote;
        }
        quota = (quota / (numseats + 1)) + epsilon;
        current = current.setQuota(quota);
        stepB2c(numseats, numcandidates, ballots, record, current);
    }

    private static void stepB2c(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current) {
        double quota = current.getQuota();
        boolean elected = false;
        for (int candidate = 0; candidate <= numcandidates; candidate++) {
            CandidateState candidatestate = current.getCandidateState(candidate);
            if (candidatestate != CandidateState.HOPEFUL) {
                continue;
            }
            double vote = current.getVote(candidate);
            if (vote >= quota) {
                current = current.setCandidateState(candidate, CandidateState.ELECTED);
                elected = true;
            }
        }
        record.add(current);
        stepB2d(numseats, numcandidates, ballots, record, current, elected);
    }

    private static void stepB2d(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current, boolean elected) {
        double quota = current.getQuota();
        double surplus = 0;
        for (int candidate = 0; candidate <= numcandidates; candidate++) {
            CandidateState candidatestate = current.getCandidateState(candidate);
            if (candidatestate == CandidateState.ELECTED) {
                double vote = current.getVote(candidate);
                surplus += Math.max(vote - quota, 0);
            }
        }
        current = current.setSurplus(surplus);
        stepB2e(numseats, numcandidates, ballots, record, current, elected);
    }

    private static void stepB2e(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current, boolean elected) {
        if (elected) {
            stepB1(numseats, numcandidates, ballots, record, current);
        } else {
            double surplus = current.getSurplus();
            double prevsurplus = current.getPrevSurplus();
            if (surplus < omega || surplus >= prevsurplus) {
                stepB3();
            } else {
                stepB2f(numseats, numcandidates, ballots, record, current);
            }
        }
    }
    
    private static void stepB2f(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current) {
        double quota = current.getQuota();
        for (int candidate = 0; candidate < numcandidates; candidate++) {
            CandidateState candidatestate = current.getCandidateState(candidate);
            if (candidatestate != CandidateState.ELECTED) {
                continue;
            }
            double keepfactor = current.getKeepFactor(candidate);
            double vote = current.getVote(candidate);
            keepfactor = keepfactor * (quota / vote);
            current = current.setKeepFactor(keepfactor);
        }
        stepB2a(numseats,numcandidates,ballots,record,current);
    }

    private static void stepB3() {

    }
    
    private static void stepC() {

    }
}
