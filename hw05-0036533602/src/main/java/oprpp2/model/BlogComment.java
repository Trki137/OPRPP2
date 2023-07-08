package oprpp2.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "blog_comments")
@Cacheable
public class BlogComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false, name = "blog_entry_id")
    private BlogEntry blogEntry;
    @Column(nullable = false, length = 100)
    private String usersEMail;
    @Column(length = 4096 , nullable = false)
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date postedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BlogEntry getBlogEntry() {
        return blogEntry;
    }

    public void setBlogEntry(BlogEntry blogEntry) {
        this.blogEntry = blogEntry;
    }

    public String getUsersEMail() {
        return usersEMail;
    }

    public void setUsersEMail(String usersEMail) {
        this.usersEMail = usersEMail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(Date postedOn) {
        this.postedOn = postedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlogComment that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
