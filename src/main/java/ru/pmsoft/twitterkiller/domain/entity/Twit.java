package ru.pmsoft.twitterkiller.domain.entity;

/**
 * Created by Александра on 07.07.14.
 */
public class Twit {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int id;
    private String message;
    private Twit(){}
    public Twit(int id, String message) {this.id = id; this.message = message;}

}
