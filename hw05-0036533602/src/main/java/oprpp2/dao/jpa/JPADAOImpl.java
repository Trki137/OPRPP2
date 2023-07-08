package oprpp2.dao.jpa;

import oprpp2.dao.DAO;
import oprpp2.model.BlogComment;
import oprpp2.model.BlogEntry;
import oprpp2.model.BlogUser;

import javax.persistence.EntityManager;
import java.util.List;

public class JPADAOImpl implements DAO {
    @Override
    public void saveUser(BlogUser user) {
        JPAEMProvider.getEntityManager().persist(user);
    }

    @Override
    public boolean checkNickExists(String nick) {
        return JPAEMProvider.getEntityManager()
                .createNamedQuery("BlogUser.getUserByNick")
                .setParameter("nick", nick)
                .getResultList().size() > 0;
    }

    @Override
    public boolean checkEmailExists(String email) {
        return JPAEMProvider.getEntityManager()
                .createNamedQuery("BlogUser.getUserByEmail")
                .setParameter("email", email)
                .getResultList().size() > 0;
    }

    @Override
    public List<BlogUser> getUsers(Long id) {
        return JPAEMProvider.getEntityManager()
                .createNamedQuery("BlogUser.getByDiffId", BlogUser.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public BlogUser getUserById(Long id) {
        return JPAEMProvider.getEntityManager().find(BlogUser.class, id);
    }

    @Override
    public BlogUser getUserByNick(String nick) {
        EntityManager em = JPAEMProvider.getEntityManager();

        List<BlogUser> blogUsers = em.createNamedQuery("BlogUser.getUserByNick", BlogUser.class)
                .setParameter("nick", nick)
                .getResultList();

        return blogUsers.size() == 1 ? blogUsers.get(0) : null;
    }

    @Override
    public void saveEntry(BlogEntry blogEntry) {
        JPAEMProvider.getEntityManager().persist(blogEntry);
    }

    @Override
    public BlogEntry getBlogEntryById(Long id) {
        return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
    }

    @Override
    public void updateEntry(BlogEntry entry) {
        JPAEMProvider.getEntityManager().persist(entry);
    }

    @Override
    public List<BlogEntry> getBlogEntriesForUser(BlogUser user) {
        return JPAEMProvider.getEntityManager()
                .createNamedQuery("BlogEntry.getEntryForUser", BlogEntry.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public void saveBlogComment(BlogComment blogComment) {
        JPAEMProvider.getEntityManager().persist(blogComment);
    }

    @Override
    public List<BlogUser> getAllUsers() {
        return JPAEMProvider.getEntityManager()
                .createNamedQuery("BlogUser.getAll", BlogUser.class)
                .getResultList();
    }
}
