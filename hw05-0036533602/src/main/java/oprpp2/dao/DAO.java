package oprpp2.dao;

import oprpp2.model.BlogComment;
import oprpp2.model.BlogEntry;
import oprpp2.model.BlogUser;

import java.util.List;

public interface DAO {
    void saveUser(BlogUser user);
    boolean checkNickExists(String nick);
    boolean checkEmailExists(String email);
    List<BlogUser> getUsers(Long id);
    BlogUser getUserById(Long id);

    BlogUser getUserByNick(String nick);
    void saveEntry(BlogEntry blogEntry);
    BlogEntry getBlogEntryById(Long id);

    void updateEntry(BlogEntry entry);

    List<BlogEntry> getBlogEntriesForUser(BlogUser blogUser);

    void saveBlogComment(BlogComment blogComment);

    List<BlogUser> getAllUsers();
}
