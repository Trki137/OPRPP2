package oprpp2.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NamedQueries({
        @NamedQuery(name="BlogUser.getUserByEmail", query = "SELECT user FROM BlogUser as user WHERE user.email =: email"),
        @NamedQuery(name="BlogUser.getUserByNick", query = "SELECT user FROM BlogUser as user WHERE user.nick =: nick"),
        @NamedQuery(name="BlogUser.getByDiffId", query = "SELECT user FROM BlogUser as user WHERE user.id <>: id"),
        @NamedQuery(name="BlogUser.getAll", query = "SELECT user FROM BlogUser as user")

})
@Entity
@Table(name = "blog_user")
@Cacheable
public class BlogUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 40, nullable = false)
    private String firstName;
    @Column(length = 40, nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String nick;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<BlogEntry> entries = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlogUser blogUser)) return false;
        return Objects.equals(id, blogUser.id) && Objects.equals(nick, blogUser.nick) && Objects.equals(email, blogUser.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nick, email);
    }
}
