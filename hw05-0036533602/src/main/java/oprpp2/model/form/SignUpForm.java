package oprpp2.model.form;

import oprpp2.model.BlogUser;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpForm {
    private String firstName;
    private String lastName;
    private String email;
    private String nick;
    private String password;

    private final Map<String, String> errors = new HashMap<>();

    public boolean hasAnyError() {
        return !errors.isEmpty();
    }

    public void httpRequestFill(HttpServletRequest req) {
        this.nick = prepare(req.getParameter("nick"));
        this.password = prepare(req.getParameter("password"));
        this.firstName = prepare(req.getParameter("firstname"));
        this.lastName = prepare(req.getParameter("lastname"));
        this.email = prepare(req.getParameter("email"));
    }

    public void validate() {
        errors.clear();

        if (this.nick.isEmpty())
            errors.put("nick", "Nickname is required");

        if (this.password.isEmpty())
            errors.put("password", "Password is required");

        if (this.email.isEmpty()) {
            errors.put("email", "Email is required");
        } else {
            String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

            boolean valid = Pattern.compile(regexPattern).matcher(this.email).matches();
            if (!valid)
                errors.put("email", "Invalid email");
        }

        if (this.firstName.isEmpty())
            errors.put("firstname", "First name is required");

        if (this.lastName.isEmpty())
            errors.put("lastname", "Last name is required");

    }

    public String prepare(String value) {
        return value == null ? "" : value;
    }

    public void fillFromBlogUser(BlogUser blogUser) {
        this.nick = blogUser.getNick();
        this.email = blogUser.getEmail();
        this.firstName = blogUser.getFirstName();
        this.lastName = blogUser.getLastName();
    }

    public void fillToBlogUser(BlogUser blogUser) {
        blogUser.setEmail(this.email);
        blogUser.setFirstName(this.firstName);
        blogUser.setLastName(this.lastName);
        blogUser.setNick(this.nick);

        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-512");
            byte[] result = md.digest(this.password.getBytes());
            hashedPassword = new String(result);
        } catch (Exception ignored) {}

        if(hashedPassword == null) throw new IllegalStateException();

        blogUser.setPasswordHash(hashedPassword);
    }

    public Map<String, String> getErrors() {
        return errors;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
