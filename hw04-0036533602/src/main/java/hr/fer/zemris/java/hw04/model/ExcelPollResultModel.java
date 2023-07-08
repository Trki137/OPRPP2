package hr.fer.zemris.java.hw04.model;

import java.util.List;

public class ExcelPollResultModel {
    private List<PollOptionsModel> pollOptionsModelList;
    private String pollTitle;

    public List<PollOptionsModel> getPollOptionsModelList() {
        return pollOptionsModelList;
    }

    public void setPollOptionsModelList(List<PollOptionsModel> pollOptionsModelList) {
        this.pollOptionsModelList = pollOptionsModelList;
    }

    public String getPollTitle() {
        return pollTitle;
    }

    public void setPollTitle(String pollTitle) {
        this.pollTitle = pollTitle;
    }
}
