package ru.pmsoft.twitterkiller.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.pmsoft.twitterkiller.domain.util.JsonDateSerializer;

import java.util.Date;

/**
 * Created by Андрей on 05.07.2014.
 */
public class Token {




    private String value;
    private Date expiration;

    public Token(String value, Date expiration) {
        this.value = value;
        this.expiration = expiration;
    }

    public String getToken() {
        return value;
    }

    public void setToken(String value) {
        this.value = value;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getExpiration() {
        return expiration;
    }
}
