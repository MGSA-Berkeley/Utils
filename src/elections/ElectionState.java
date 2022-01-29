package elections;

import java.util.Arrays;
import java.util.Objects;

public class ElectionState {

    private final CandidateState[] candidatestates;
    private final Decimal[] keepfactors;
    private final Decimal[] votes;
    private final Decimal quota;
    private final Decimal surplus;
    private final Decimal prevsurplus;

    public ElectionState(CandidateState[] candidatestates, Decimal[] keepfactors, Decimal[] votes, Decimal quota, Decimal surplus, Decimal prevsurplus) {
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

    public Decimal getKeepFactor(int candidate) {
        return keepfactors[candidate];
    }

    public ElectionState setKeepFactor(int candidate, Decimal keepfactor) {
        Decimal[] newkeepfactors = Arrays.copyOf(keepfactors, keepfactors.length);
        newkeepfactors[candidate] = keepfactor;
        return new ElectionState(candidatestates, newkeepfactors, votes, quota, surplus, prevsurplus);
    }

    public Decimal getVote(int candidate) {
        return votes[candidate];
    }

    public ElectionState addVote(int candidate, Decimal vote) {
        Decimal newvote = votes[candidate].add(vote);
        Decimal[] newvotes = Arrays.copyOf(votes, votes.length);
        newvotes[candidate] = newvote;
        return new ElectionState(candidatestates, keepfactors, newvotes, quota, surplus, prevsurplus);
    }
    
    public ElectionState clearVotes() {
        Decimal[] newvotes = new Decimal[votes.length];
        Arrays.fill(newvotes, Decimal.ZERO);
        return new ElectionState(candidatestates, keepfactors, newvotes, quota, surplus, prevsurplus);
    }

    public Decimal getQuota() {
        return quota;
    }

    public ElectionState setQuota(Decimal quota) {
        return new ElectionState(candidatestates, keepfactors, votes, quota, surplus, prevsurplus);
    }

    public Decimal getSurplus() {
        return surplus;
    }

    public ElectionState setSurplus(Decimal surplus) {
        return new ElectionState(candidatestates, keepfactors, votes, quota, surplus, this.surplus);
    }

    public Decimal getPrevSurplus() {
        return prevsurplus;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Arrays.deepHashCode(this.candidatestates);
        hash = 37 * hash + Arrays.deepHashCode(this.keepfactors);
        hash = 37 * hash + Arrays.deepHashCode(this.votes);
        hash = 37 * hash + Objects.hashCode(this.quota);
        hash = 37 * hash + Objects.hashCode(this.surplus);
        hash = 37 * hash + Objects.hashCode(this.prevsurplus);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ElectionState other = (ElectionState) obj;
        if (!Arrays.deepEquals(this.candidatestates, other.candidatestates)) {
            return false;
        }
        if (!Arrays.deepEquals(this.keepfactors, other.keepfactors)) {
            return false;
        }
        if (!Arrays.deepEquals(this.votes, other.votes)) {
            return false;
        }
        if (!Objects.equals(this.quota, other.quota)) {
            return false;
        }
        if (!Objects.equals(this.surplus, other.surplus)) {
            return false;
        }
        return Objects.equals(this.prevsurplus, other.prevsurplus);
    }
}
