package hr.fer.zemris.java.hw04.dao;


import hr.fer.zemris.java.hw04.dao.sql.DAOImpl;

public class DAOProvider {
    private static final DAO dao = new DAOImpl();

    private DAOProvider(){}

    public static DAO getInstance(){
        return dao;
    }
}