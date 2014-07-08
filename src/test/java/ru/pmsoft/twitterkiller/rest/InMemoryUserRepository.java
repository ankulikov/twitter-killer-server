package ru.pmsoft.twitterkiller.rest;

import ru.pmsoft.twitterkiller.domain.entity.User;
import ru.pmsoft.twitterkiller.domain.repository.UserRepository;

import java.util.HashMap;

/**
 * Created by Андрей on 05.07.2014.
 */
public class InMemoryUserRepository implements UserRepository {
    HashMap<String, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getLogin(), user);
    }

    @Override
    public User getByLogin(String name) {
        return users.get(name);
    }

    @Override
    public Iterable<User> values() {
       return users.values();
    }
}
