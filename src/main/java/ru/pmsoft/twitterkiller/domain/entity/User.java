package ru.pmsoft.twitterkiller.domain.entity;

import org.hibernate.annotations.Table;
import ru.pmsoft.twitterkiller.domain.dto.Token;
import ru.pmsoft.twitterkiller.domain.util.UserUtil;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class User implements Serializable{
    //private List<Twit> twitts;
    private String login;
    private String passwordHash;
    private String salt;
    private int id;
    private Token token;


    private User() { }

    public User(String login, String password) {
        //twitts = new ArrayList<Twit>();
        this.login = login;
        salt = UserUtil.generateSalt();
        this.passwordHash = UserUtil.getSHA256(password, salt);
        token = new Token(UserUtil.generateToken(), UserUtil.computeExpiration(TimeUnit.DAYS, 1));
       // token = UserUtil.generateToken();
       // setExpiration(UserUtil.computeExpiration(TimeUnit.DAYS, 1));
    }


    public boolean checkPassword(String password) {
        return passwordHash.equals(UserUtil.getSHA256(password, salt));
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }


    //public void addTwitt (Twit twitt)
    //{
    //    twitts.add(twitt);
    //}
}
