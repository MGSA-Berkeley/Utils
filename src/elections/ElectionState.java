package elections;

import java.util.Arrays;

public class ElectionState {

    private final CandidateState[] candidatestates;
    private final double[] keepfactors;
    private final double[] votes;
    private final double quota;
    private final double surplus;
    private final double prevsurplus;

    public ElectionState(CandidateState[] candidatestates, double[] keepfactors, double[] votes, double quota, double surplus, double prevsurplus) {
        this.candidatestates = candidatestates;
        this.keepfactors = keepfactors;
        this.votes = votes;
        this.quota = quota;
        this.surplus = surplus;
        this.prevsurplus = prevsurplus;
    }

    public CandidateState getCandidateState(int candidate) {
        return candidatestates[candidate];
    }

    public ElectionState setCandidateState(int candidate, CandidateState candidatestate) {
        CandidateState[] newcandidatestates = Arrays.copyOf(candidatestates, candidatestates.length);
        newcandidatestates[candidate] = candidatestate;
        return new ElectionState(newcandidatestates, keepfactors, votes, quota, surplus, prevsurplus);
    }

    public double getKeepFactor(int candidate) {
        return keepfactors[candidate];
    }

    public ElectionState setKeepFactor(int candidate, double keepfactor) {
        double[] newkeepfactors = Arrays.copyOf(keepfactors, keepfactors.length);
        newkeepfactors[candidate] = keepfactor;
        return new ElectionState(candidatestates, newkeepfactors, votes, quota, surplus, prevsurplus);
    }

    public double getVote(int candidate) {
        return votes[candidate];
    }

    public ElectionState addVote(int candidate, double vote) {
        double[] newvotes = Arrays.copyOf(votes, votes.length);
        newvotes[candidate] += vote;
        return new ElectionState(candidatestates, keepfactors, newvotes, quota, surplus, prevsurplus);
    }

    public double getQuota() {
        return quota;
    }

    public ElectionState setQuota(double quota) {
        return new ElectionState(candidatestates, keepfactors, votes, quota, surplus, prevsurplus);
    }

    public double getSurplus() {
        return surplus;
    }

    public ElectionState setSurplus(double surplus) {
        return new ElectionState(candidatestates, keepfactors, votes, quota, surplus, this.surplus);
    }

    public double getPrevSurplus() {
        return prevsurplus;
    }
}
