package oprpp2.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@NamedQueries({
        @NamedQuery(name="BlogEntry.getEntryForUser", query = "SELECT blog FROM BlogEntry as blog WHERE blog.user =: user"),
})
@Entity
@Table(name = "blog_entries")
@Cacheable
public class BlogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST,orphanRemoval = true)
    private List<BlogComment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false, name = "blog_user_id")
    private BlogUser user;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedAt;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false, length = 4096)
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BlogComment> getComments() {
        return comments;
    }

    public void setComments(List<BlogComment> comments) {
        this.comments = comments;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(BlogUser user) {
        this.user = user;
    }

    public BlogUser getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlogEntry blogEntry)) return false;
        return Objects.equals(id, blogEntry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
