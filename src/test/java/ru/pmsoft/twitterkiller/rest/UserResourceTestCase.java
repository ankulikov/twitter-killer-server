package ru.pmsoft.twitterkiller.rest;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.pmsoft.twitterkiller.domain.entity.User;
import ru.pmsoft.twitterkiller.domain.repository.UserRepository;
import ru.pmsoft.twitterkiller.rest.exceptions.ClientException;
import ru.pmsoft.twitterkiller.rest.exceptions.ExceptionBody;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class UserResourceTestCase {

    private static UserResource createSystemUnderTest(UserRepository repository) {
        return new UserResource(repository == null ? mock(UserRepository.class) : repository);
    }

    @Test(dataProvider = "invalidAuthenticationTestData")
    public void authentication_withInvalidArguments_shouldThrowClientException(
            String login, String password, ExceptionBody expected) {
        UserRepository repositoryDummy = mock(UserRepository.class);
        UserResource sut = createSystemUnderTest(repositoryDummy);

        try {
            sut.authentication(login, password);
        } catch (ClientException ex) {
            assertEquals(ex.getResponse().getEntity(), expected);
            return;
        }

        fail();
    }

    @DataProvider
    public Object[][] invalidAuthenticationTestData() {
        return new Object[][]{
                new Object[]{null, "foo", new ExceptionBody("Login can not be empty")},
                new Object[]{"foo", null, new ExceptionBody("Password can not be empty")},
                new Object[]{"foo", "bar", new ExceptionBody("User is not found")}
        };
    }

    @Test
    public void authentication_ifPasswordIsNotCorrect_shouldThrowClientException_semiIntegration() {
        final String login = "foo";
        final String password = "bar";
        User user = new User(login, password);

        UserRepository repositoryStub = mock(UserRepository.class);
        when(repositoryStub.getByLogin(login)).thenReturn(user);
        UserResource sut = createSystemUnderTest(repositoryStub);

        try {
            sut.authentication(login, "baz");
        } catch (ClientException e) {
            assertEquals(e.getResponse().getEntity(), new ExceptionBody("Password is not correct"));
            return;
        }

        fail();
    }

    @Test
    public void authentication_ifPasswordIsNotCorrect_shouldThrowClientException_unit() {
        User userStub = mock(User.class);
        when(userStub.checkPassword(any(String.class))).thenReturn(false);
        UserRepository repositoryStub = mock(UserRepository.class);
        when(repositoryStub.getByLogin(any(String.class))).thenReturn(userStub);
        UserResource sut = createSystemUnderTest(repositoryStub);

        try {
            sut.authentication("foo", "bar");
        } catch (ClientException e) {
            assertEquals(e.getResponse().getEntity(), new ExceptionBody("Password is not correct"));
            return;
        }

        fail();
    }
}
