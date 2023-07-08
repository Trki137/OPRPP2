package oprpp2.model.form;

import oprpp2.model.BlogUser;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class SignInForm {
    private String nick;
    private String password;

    private final Map<String,String> errors = new HashMap<>();

    public String getError(String key){
        return errors.get(key);
    }

    public boolean hasAnyError(){
        return !errors.isEmpty();
    }
    public boolean hasError(String key){
        return errors.containsKey(key);
    }

    public void httpRequestFill(HttpServletRequest req){
        this.nick = prepare(req.getParameter("nick"));
        this.password = prepare(req.getParameter("password"));
    }

    public void validate(){
        errors.clear();

        if(this.nick.isEmpty())
            errors.put("nick", "Nickname is required");

        if(this.password.isEmpty())
            errors.put("password", "Password is required");

    }

    public String prepare(String value){
        return  value == null ? "" : value;
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

    public Map<String, String> getErrors() {
        return errors;
    }
}
