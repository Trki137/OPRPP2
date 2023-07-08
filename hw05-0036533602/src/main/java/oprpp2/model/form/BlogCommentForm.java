package oprpp2.model.form;

import oprpp2.model.BlogComment;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BlogCommentForm {
    private String message;
    private String entryId;
    private final Map<String,String> errors = new HashMap<>();

    public boolean hasAnyError() {
        return !errors.isEmpty();
    }

    public void httpRequestFill(HttpServletRequest req) {
        this.message = prepare(req.getParameter("text"));
        this.entryId = prepare(req.getParameter("id"));
    }

    public void fillToBlogComment(BlogComment blogComment){
        blogComment.setMessage(this.message);
        blogComment.setPostedOn(new Date());
    }
    public void validate() {
        errors.clear();

        if (this.message.isEmpty())
            errors.put("text", "Text is required");
    }

    public String prepare(String value) {
        return value == null ? "" : value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }
}
