package hr.fer.zemris.java.hw04.model;

public class PollOptionsModel {

    private int id;

    private String optionTitle;

    private String optionLink;

    private int pollId;

    private int votesCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public String getOptionLink() {
        return optionLink;
    }

    public void setOptionLink(String optionLink) {
        this.optionLink = optionLink;
    }

    public int getPollId() {
        return pollId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
    }

    public int getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(int votesCount) {
        this.votesCount = votesCount;
    }
}
