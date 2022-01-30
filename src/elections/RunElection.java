package elections;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Runs Meek STV. Implements
 * https://prfound.org/resources/reference/reference-meek-rule/
 */
public class RunElection {

    private static final Decimal epsilon = new Decimal(BigInteger.ONE);
    private static final Decimal omega = new Decimal(BigInteger.TEN.pow(3));

    public static void runElection(int numseats, String[] candidates, int[][] ballots) throws IOException {
        List<ElectionState> record = new ArrayList<>();
        stepA(numseats, candidates.length, ballots, record);
        DisplayElection.displayElection(numseats, candidates, record);
    }

    //Initialize Election
    private static void stepA(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record) {
        CandidateState[] candidatestates = new CandidateState[numcandidates];
        Arrays.fill(candidatestates, CandidateState.HOPEFUL);
        Decimal[] keepfactors = new Decimal[numcandidates];
        Arrays.fill(keepfactors, Decimal.ONE);
        Decimal[] votes = new Decimal[numcandidates];
        ElectionState electionstate = new ElectionState(candidatestates, keepfactors, votes, null, null, null);
        stepB1(numseats, numcandidates, ballots, record, electionstate);
    }

    //Test count complete
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
        if (numelected >= numseats || numelected + numhopeful <= numseats) {
            stepC(numseats, numcandidates, ballots, record, current);
        } else {
            current = current.setSurplus(null);
            stepB2a(numseats, numcandidates, ballots, record, current);
        }
    }

    //Distribute votes
    private static void stepB2a(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current) {
        current = current.clearVotes();
        int numballots = ballots.length;
        for (int ballot = 0; ballot < numballots; ballot++) {
            Decimal weight = Decimal.ONE;
            for (int candidate : ballots[ballot]) {
                Decimal keepfactor = current.getKeepFactor(candidate);
                Decimal vote = weight.multiplyup(keepfactor);
                current = current.addVote(candidate, vote);
                weight = weight.subtract(vote);
                if (weight.signum() == 0) {
                    break;
                }
            }
        }
        stepB2b(numseats, numcandidates, ballots, record, current);
    }

    //Update quota
    private static void stepB2b(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current) {
        Decimal quota = Decimal.ZERO;
        for (int candidate = 0; candidate < numcandidates; candidate++) {
            Decimal vote = current.getVote(candidate);
            quota = quota.add(vote);
        }
        quota = quota.dividedown(Decimal.valueOf(numseats + 1));
        quota = quota.add(epsilon);
        current = current.setQuota(quota);
        stepB2c(numseats, numcandidates, ballots, record, current);
    }

    //Find winners
    private static void stepB2c(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current) {
        Decimal quota = current.getQuota();
        boolean elected = false;
        for (int candidate = 0; candidate < numcandidates; candidate++) {
            CandidateState candidatestate = current.getCandidateState(candidate);
            if (candidatestate != CandidateState.HOPEFUL) {
                continue;
            }
            Decimal vote = current.getVote(candidate);
            if (vote.compareTo(quota) >= 0) {
                current = current.setCandidateState(candidate, CandidateState.ELECTED);
                elected = true;
            }
        }
        if (elected) {
            record.add(current);
        }
        stepB2d(numseats, numcandidates, ballots, record, current, elected);
    }

    //Calculate the total surplus
    private static void stepB2d(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current, boolean elected) {
        Decimal quota = current.getQuota();
        Decimal surplus = Decimal.ZERO;
        for (int candidate = 0; candidate < numcandidates; candidate++) {
            CandidateState candidatestate = current.getCandidateState(candidate);
            if (candidatestate != CandidateState.ELECTED) {
                continue;
            }
            Decimal vote = current.getVote(candidate);
            Decimal individualsurplus = vote.subtract(quota);
            if (individualsurplus.signum() > 0) {
                surplus = surplus.add(individualsurplus);
            }
        }
        current = current.setSurplus(surplus);
        stepB2e(numseats, numcandidates, ballots, record, current, elected);
    }

    //Test for iteration finished
    private static void stepB2e(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current, boolean elected) {
        if (elected) {
            stepB1(numseats, numcandidates, ballots, record, current);
        } else {
            Decimal surplus = current.getSurplus();
            Decimal prevsurplus = current.getPrevSurplus();
            if (surplus.compareTo(omega) < 0 || (prevsurplus != null && surplus.compareTo(prevsurplus) >= 0)) {
                stepB3(numseats, numcandidates, ballots, record, current);
            } else {
                stepB2f(numseats, numcandidates, ballots, record, current);
            }
        }
    }

    //Update keep factors
    private static void stepB2f(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current) {
        Decimal quota = current.getQuota();
        for (int candidate = 0; candidate < numcandidates; candidate++) {
            CandidateState candidatestate = current.getCandidateState(candidate);
            if (candidatestate != CandidateState.ELECTED) {
                continue;
            }
            Decimal keepfactor = current.getKeepFactor(candidate);
            Decimal vote = current.getVote(candidate);
            keepfactor = keepfactor.multiplyup(quota);
            keepfactor = keepfactor.divideup(vote);
            current = current.setKeepFactor(candidate, keepfactor);
        }
        stepB2a(numseats, numcandidates, ballots, record, current);
    }

    private static void stepB3(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current) {
        Decimal minvote = null;
        for (int candidate = 0; candidate < numcandidates; candidate++) {
            CandidateState candidatestate = current.getCandidateState(candidate);
            if (candidatestate != CandidateState.HOPEFUL) {
                continue;
            }
            Decimal vote = current.getVote(candidate);
            if (minvote == null || vote.compareTo(minvote) < 0) {
                minvote = vote;
            }
        }
        Decimal surplus = current.getSurplus();
        Decimal tiedthreashold = minvote.add(surplus);
        List<Integer> tiedcandidates = new ArrayList<>();
        for (int candidate = 0; candidate < numcandidates; candidate++) {
            CandidateState candidatestate = current.getCandidateState(candidate);
            if (candidatestate != CandidateState.HOPEFUL) {
                continue;
            }
            Decimal vote = current.getVote(candidate);
            if (vote.compareTo(tiedthreashold) <= 0) {
                tiedcandidates.add(candidate);
            }
        }
        int numtiedcandidates = tiedcandidates.size();
        Random randy = new Random(Arrays.deepHashCode(ballots));
        int randomindex = randy.nextInt(numtiedcandidates);
        int tiedcandidate = tiedcandidates.get(randomindex);
        current = current.setCandidateState(tiedcandidate, CandidateState.DEFEATED);
        record.add(current);
        current = current.setKeepFactor(tiedcandidate, Decimal.ZERO);
        stepB1(numseats, numcandidates, ballots, record, current);
    }

    private static void stepC(int numseats, int numcandidates, int[][] ballots, List<ElectionState> record, ElectionState current) {
        int numelected = 0;
        for (int candidate = 0; candidate < numcandidates; candidate++) {
            CandidateState candidatestate = current.getCandidateState(candidate);
            if (candidatestate == CandidateState.ELECTED) {
                numelected++;
            }
        }
        boolean elected = false;
        if (numelected < numseats) {
            for (int candidate = 0; candidate < numcandidates; candidate++) {
                CandidateState candidatestate = current.getCandidateState(candidate);
                if (candidatestate == CandidateState.HOPEFUL) {
                    current = current.setCandidateState(candidate, CandidateState.ELECTED);
                    elected = true;
                }
            }
        } else {
            for (int candidate = 0; candidate < numcandidates; candidate++) {
                CandidateState candidatestate = current.getCandidateState(candidate);
                if (candidatestate == CandidateState.HOPEFUL) {
                    current = current.setCandidateState(candidate, CandidateState.DEFEATED);
                    elected = true;
                }
            }
        }
        if (elected) {
            record.add(current);
        }
    }
}
