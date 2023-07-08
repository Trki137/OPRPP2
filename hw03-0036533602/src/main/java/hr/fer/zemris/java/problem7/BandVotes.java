package hr.fer.zemris.java.problem7;

public class BandVotes {

    private String bandName;
    private int votes;

    public BandVotes(String bandName, int votes) {
        this.bandName = bandName;
        this.votes = votes;
    }

    public int getVotes() {
        return votes;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
