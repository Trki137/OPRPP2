package oprpp2.dao;

import oprpp2.dao.jpa.JPADAOImpl;

public class DAOProvider {
    private static DAO dao = new JPADAOImpl();

    public static DAO getDAO() {
        return dao;
    }
}
