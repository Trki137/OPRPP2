package hr.fer.zemris.java.problem7;

public class Band {
    private final int id;
    private final String ytLink;
    private final String bandName;
    public Band(int id, String ytLink, String bandName) {
        this.id = id;
        this.ytLink = ytLink;
        this.bandName = bandName;
    }

    public String getBandName() {
        return bandName;
    }

    public int getId() {
        return id;
    }
    public String getYtLink() {
        return ytLink;
    }
}
