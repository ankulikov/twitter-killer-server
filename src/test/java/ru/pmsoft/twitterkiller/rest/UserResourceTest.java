package ru.pmsoft.twitterkiller.rest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.pmsoft.twitterkiller.domain.dto.Token;
import ru.pmsoft.twitterkiller.domain.entity.User;
import ru.pmsoft.twitterkiller.domain.repository.UserRepository;
import ru.pmsoft.twitterkiller.rest.exceptions.ClientException;
import ru.pmsoft.twitterkiller.rest.exceptions.ExceptionBody;

import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;


/**
 * Created by Александра on 07.07.14.
 */
public class UserResourceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    public UserRepository hashrep = new InMemoryUserRepository();
    UserResource sut = new UserResourceFactory().getRepository(hashrep);

    @Test
    public void authentication_hasToThrow_ClientException_LoginIsNotCorrect() throws Exception {
        try{
              sut.authentication(null, null);
        }
        catch (ClientException ex)
        {
            ExceptionBody exbody = (ExceptionBody)(ex.getResponse().getEntity());
            assertEquals("Login can not be empty", exbody.getMessage());
        }

    }

    @Test
    public void authentication_hasToThrow_ClientException_PasswordDoesNotExist() throws Exception {
        try{
            sut.authentication("user", null);
        }
        catch (ClientException ex)
        {
            ExceptionBody exbody = (ExceptionBody)(ex.getResponse().getEntity());
            assertEquals("Password can not be empty", exbody.getMessage());
        }
    }

    @Test
    public void authentication_hasToThrow_ClientException_UserIsNotFound() throws Exception {
        try{

            hashrep.save(new User("user1", "password"));
            sut.authentication("user2", "password");
        }
        catch (ClientException ex)
        {
            ExceptionBody exbody = (ExceptionBody)(ex.getResponse().getEntity());
            assertEquals("User is not found", exbody.getMessage());
        }
    }

    @Test
    public void authentication_hasToThrow_ClientException_PasswordIsNotCorrect() throws Exception {

        try
        {
             User dummy = new User("user", "password");
             hashrep.save(dummy);
             sut.authentication("user", "0");
        }
        catch(ClientException ex)
        {
            ExceptionBody exbody = (ExceptionBody)(ex.getResponse().getEntity());
            assertEquals("Password is not correct", exbody.getMessage());
        }

    }

    @Test
    public void authentication_hasToChangeTokenAndExpiration() throws Exception {
        User dummy = new User("user", "password");
        Calendar calendar  = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR) - 1);
        dummy.getToken().setExpiration(calendar.getTime());
        hashrep.save(dummy);
        sut.authentication("user", "password");
        Date expiration = dummy.getToken().getExpiration();
        assertNotSame(calendar.getTime(), expiration);
    }

    @Test
    public void authentication_hasToReturnJSONwithExpirationAndToken() throws Exception {
        User dummy = new User("user", "password");
        hashrep.save(dummy);
        Response resp =  sut.authentication("user", "password");
        Token token = (Token)resp.getEntity();
        assertEquals(dummy.getToken().getToken(), token.getToken());
        assertEquals(dummy.getToken().getExpiration(), token.getExpiration());
    }

    @Test
    public void registration_hasToThrow_ClientException_LoginIsNotCorrect() throws Exception {
        try{
        sut.registration(null, null);
        }
        catch (ClientException ex)
        {
            ExceptionBody exbody = (ExceptionBody)(ex.getResponse().getEntity());
            assertEquals("Login can not be empty", exbody.getMessage());
        }
    }

    @Test
    public void registration_hasToThrow_ClientException_PasswordDoesNotExist() throws Exception {
        try{
            sut.registration("user", null);
        }
        catch (ClientException ex)
        {
            ExceptionBody exbody = (ExceptionBody)(ex.getResponse().getEntity());
            assertEquals("Password can not be empty", exbody.getMessage());
        }
    }

    @Test
    public void registration_hasToThrow_ClientException_SuchLoginAlreadyExist() throws Exception {
        try{

        hashrep.save(new User("user", "password"));
        sut.registration("user", "password");
        }
        catch (ClientException ex)
        {
            ExceptionBody exbody = (ExceptionBody)(ex.getResponse().getEntity());
            assertEquals("Login is already taken", exbody.getMessage());
        }
    }

    @Test
    public void registration_OK() throws Exception {
        UserRepository hashrep = new InMemoryUserRepository();
        String login = "user2";
        hashrep.save(new User("user1", "password"));
        UserResource sut = new UserResource(hashrep);
        Response resp =  sut.registration(login, "password");
        String s = (String)resp.getEntity();
        String arr[] = s.split(":");
        arr[1] = arr[1].trim();
        assertTrue(arr[1].equals(login));
    }
}
