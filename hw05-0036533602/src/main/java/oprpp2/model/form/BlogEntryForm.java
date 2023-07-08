package oprpp2.model.form;

import oprpp2.model.BlogEntry;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class BlogEntryForm {
    private String id;
    private String title;
    private String text;
    private final Map<String,String> errors = new HashMap<>();

    public boolean hasAnyError() {
        return !errors.isEmpty();
    }

    public void httpRequestFill(HttpServletRequest req) {
        this.text = prepare(req.getParameter("text"));
        this.title = prepare(req.getParameter("title"));
        this.id = prepare(req.getParameter("id"));
    }

    public void fillToBlogEntry(BlogEntry blogEntry){
        blogEntry.setText(this.text);
        blogEntry.setTitle(this.title);

        if(this.id.isEmpty()) blogEntry.setId(null);
        else blogEntry.setId(Long.getLong(this.id));
    }

    public void update(BlogEntry entry){
        entry.setLastModifiedAt(new Date());
        entry.setText(this.text);
        entry.setTitle(this.title);
    }

    public void validate() {
        errors.clear();

        if (this.text.isEmpty())
            errors.put("text", "Text is required");

        if (this.title.isEmpty())
            errors.put("title", "Title is required");

    }

    public String prepare(String value) {
        return value == null ? "" : value;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
