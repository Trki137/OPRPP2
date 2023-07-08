package hr.fer.zemris.java.hw04.model;

public class PollsModel {

    private int id;

    private String title;

    private String message;

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
