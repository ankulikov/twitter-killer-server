package ru.pmsoft.twitterkiller.rest;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import ru.pmsoft.twitterkiller.domain.repository.InMemoryUserRepository;
import ru.pmsoft.twitterkiller.domain.repository.UserRepository;


/**
 * Created by Лев on 07.07.2014.
 */
public class ServerTest {
   public UserRepository hashrep;
   @Before
   public void setUp(){
        hashrep = new InMemoryUserRepository();
   }


   @Test(expected = Exception.class)
   public void ServerErrorHandlingTest() throws Exception{
       UserResource sut = new UserResource(hashrep);
           sut.getError();
   }
}
