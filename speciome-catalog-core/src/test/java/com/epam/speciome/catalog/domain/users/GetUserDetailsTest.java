package com.epam.speciome.catalog.domain.users;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.domain.exceptions.InvalidCredentialsException;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Given single registered user")
public class GetUserDetailsTest {
    private static final String EMAIL = "user@company.com";
    private static final String PASSWORD = "password";
    private UseCaseFactory useCaseFactory;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
        useCaseFactory.registerUser().registerNewUser(EMAIL, PASSWORD);
    }

    @Nested
    @DisplayName("When user is not authenticated")
    public class NotAuthenticated {

        @Test
        @DisplayName("Then exception is thrown")
        public void testExceptionIsThrown() {
            GetUserDetails getUserDetails = useCaseFactory.getUserDetails();
            //noinspection ConstantConditions - it should throw exception, here we test for it
            Assertions.assertThatThrownBy(() -> getUserDetails.getUserDetails(null))
                    .isInstanceOf(InvalidCredentialsException.class);
        }
    }

    @Nested
    @DisplayName("When user is authenticated")
    public class Authenticated {
        private User user;

        @BeforeEach
        public void setUp() {
            user = useCaseFactory.logInUser().logIn(EMAIL, PASSWORD);
        }

        @Test
        @DisplayName("Then user email is returned in user details")
        public void testUserEmailMatches() {
            var response = useCaseFactory.getUserDetails().getUserDetails(user);
            Assertions.assertThat(response).isNotNull();
            Assertions.assertThat(response.email()).isEqualTo(EMAIL);
        }
    }
}
