package hr.fer.zemris.java.hw04.dao.sql;

import hr.fer.zemris.java.hw04.dao.DAO;
import hr.fer.zemris.java.hw04.dao.DAOException;
import hr.fer.zemris.java.hw04.model.ExcelPollResultModel;
import hr.fer.zemris.java.hw04.model.PollsModel;
import hr.fer.zemris.java.hw04.model.PollOptionsModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOImpl implements DAO {
    @Override
    public List<PollsModel> getAllPolls() {
        List<PollsModel> pollsModelList = new ArrayList<>();
        Connection connection = SQLConnectionProvider.getConnection();

        //                                          1, 2,    3
        final String selectAllPollsQuery = "SELECT id,title,message FROM polls";
        try(PreparedStatement selectAllPreparedStatement = connection.prepareStatement(selectAllPollsQuery)){
            try(ResultSet rs = selectAllPreparedStatement.executeQuery()){
                while(rs != null && rs.next()){
                    PollsModel pollsModel = new PollsModel();

                    pollsModel.setId(rs.getInt(1));
                    pollsModel.setTitle(rs.getString(2));
                    pollsModel.setMessage(rs.getString(3));

                    pollsModelList.add(pollsModel);
                }
            }
        }catch (SQLException e){
            throw new DAOException("Error while fetching polls from DB.",e);
        }

        return pollsModelList;
    }

    @Override
    public List<PollOptionsModel> getOptionsForPoll(Integer id) {
        List<PollOptionsModel> pollOptionsModelList = new ArrayList<>();
        Connection connection = SQLConnectionProvider.getConnection();
        //                                             1,      2
        final String selectPollOptionsQuery = "SELECT id, optionTitle FROM PollOptions WHERE pollID = ?";

        try(PreparedStatement selectPreparedStatement = connection.prepareStatement(selectPollOptionsQuery)){
            selectPreparedStatement.setInt(1,id);
            try(ResultSet resultSet = selectPreparedStatement.executeQuery()) {
                while (resultSet != null && resultSet.next()){
                    PollOptionsModel pollOptionsModel = new PollOptionsModel();

                    pollOptionsModel.setId(resultSet.getInt(1));
                    pollOptionsModel.setOptionTitle(resultSet.getString(2));
                    pollOptionsModel.setPollId(id);

                    pollOptionsModelList.add(pollOptionsModel);
                }
            }
        }catch (SQLException e){
            throw new DAOException("Failed to get data for poll id = "+id,e);
        }

        return pollOptionsModelList;
    }

    @Override
    public List<PollOptionsModel> getPollResults(Integer id) {
        List<PollOptionsModel> pollOptionsModelList = new ArrayList<>();
        Connection connection = SQLConnectionProvider.getConnection();
        //                                             1,      2,           3,         4
        final String selectPollOptionsQuery = "SELECT id, optionTitle, optionLink, votesCount  FROM PollOptions WHERE pollID = ? ORDER BY votesCount DESC";

        try(PreparedStatement selectPreparedStatement = connection.prepareStatement(selectPollOptionsQuery)){
            selectPreparedStatement.setInt(1,id);
            try(ResultSet resultSet = selectPreparedStatement.executeQuery()) {
                while (resultSet != null && resultSet.next()){
                    PollOptionsModel pollOptionsModel = new PollOptionsModel();

                    pollOptionsModel.setId(resultSet.getInt(1));
                    pollOptionsModel.setOptionTitle(resultSet.getString(2));
                    pollOptionsModel.setPollId(id);
                    pollOptionsModel.setOptionLink(resultSet.getString(3));
                    pollOptionsModel.setVotesCount(resultSet.getInt(4));

                    pollOptionsModelList.add(pollOptionsModel);
                }
            }
        }catch (SQLException e){
            throw new DAOException("Failed to get data for poll id = "+id,e);
        }

        return pollOptionsModelList;
    }

    @Override
    public String getPollMessage(Integer id) {
        String message = null;
        Connection connection = SQLConnectionProvider.getConnection();
        //                                              1
        final String selectPollOptionsQuery = "SELECT message FROM Polls WHERE id = ?";

        try(PreparedStatement selectPreparedStatement = connection.prepareStatement(selectPollOptionsQuery)){
            selectPreparedStatement.setInt(1,id);
            try(ResultSet resultSet = selectPreparedStatement.executeQuery()) {
               if(resultSet.next()){
                   message = resultSet.getString(1);
               }
            }
        }catch (SQLException e){
            throw new DAOException("Failed to get data for poll id = "+id,e);
        }

        return message;
    }

    @Override
    public void addVote(Integer optionID) {
        Connection connection = SQLConnectionProvider.getConnection();

        final String updateQuery = "UPDATE pollOptions SET votesCount = votesCount + 1 WHERE id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)){
            preparedStatement.setInt(1,optionID);
            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected != 1){
                throw new DAOException();
            }
            System.out.println("Affected rows: "+ rowsAffected);

        }catch (SQLException e){
            throw new DAOException(e);
        }

    }

    @Override
    public Integer getPollID(Integer optionID) {
        Integer pollID = null;
        Connection connection = SQLConnectionProvider.getConnection();
        //                                              1
        final String selectPollOptionsQuery = "SELECT pollID FROM PollOptions WHERE id = ?";

        try(PreparedStatement selectPreparedStatement = connection.prepareStatement(selectPollOptionsQuery)){
            selectPreparedStatement.setInt(1,optionID);
            try(ResultSet resultSet = selectPreparedStatement.executeQuery()) {
                if(resultSet.next()){
                    pollID = resultSet.getInt(1);
                }
            }
        }catch (SQLException e){
            throw new DAOException("Failed to get data for poll id = "+pollID,e);
        }

        return pollID;
    }

    @Override
    public ExcelPollResultModel getExcelResult(Integer pollID) {
        ExcelPollResultModel model = new ExcelPollResultModel();
        List<PollOptionsModel> options = new ArrayList<>();

        Connection connection = SQLConnectionProvider.getConnection();
        final String query = "SELECT title, optionTitle, votesCount FROM Polls JOIN PollOptions ON Polls.id = PollOptions.pollID WHERE polls.id = ? ORDER BY votesCount DESC";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,pollID);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet != null && resultSet.next()){
                    PollOptionsModel pollOptionsModel = new PollOptionsModel();

                    pollOptionsModel.setOptionTitle(resultSet.getString(2));
                    pollOptionsModel.setVotesCount(resultSet.getInt(3));
                    options.add(pollOptionsModel);

                    if(model.getPollTitle() == null)
                        model.setPollTitle(resultSet.getString(1));
                }
            }

        }catch (SQLException e){
            throw new DAOException(e);
        }
        model.setPollOptionsModelList(options);
        return model;
    }
}
