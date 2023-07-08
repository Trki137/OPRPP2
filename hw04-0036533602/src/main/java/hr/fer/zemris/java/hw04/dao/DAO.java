package hr.fer.zemris.java.hw04.dao;


import hr.fer.zemris.java.hw04.model.ExcelPollResultModel;
import hr.fer.zemris.java.hw04.model.PollsModel;
import hr.fer.zemris.java.hw04.model.PollOptionsModel;

import java.util.List;

public interface DAO {

    List<PollsModel> getAllPolls();

    List<PollOptionsModel> getOptionsForPoll(Integer id);
    List<PollOptionsModel> getPollResults(Integer id);

    String getPollMessage(Integer id);

    void addVote(Integer optionID);

    Integer getPollID(Integer optionID);

    ExcelPollResultModel getExcelResult(Integer pollID);
	
}