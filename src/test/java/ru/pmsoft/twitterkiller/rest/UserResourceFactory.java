package ru.pmsoft.twitterkiller.rest;

import ru.pmsoft.twitterkiller.domain.repository.UserRepository;

/**
 * Created by Александра on 08.07.14.
 */
public class UserResourceFactory {
    public UserResource getRepository(UserRepository ur)
    {
        return new UserResource(ur);
    }
}
