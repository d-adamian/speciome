package com.epam.speciome.catalog.domain.users;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class GetUserPasswordTest {
    private static final String USER_EMAIL = "some@company.com";
    private static final String PASSWORD = "pwd123";

    private UseCaseFactory useCaseFactory;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
    }

    @Nested
    @DisplayName("Given no registered users")
    public class NoActionsPerformed {

        @Test
        @DisplayName("When password is requested. Then exception is thrown")
        public void testNotFoundExceptionIsThrown() {
            Assertions.assertThatThrownBy(() -> useCaseFactory.getUserPassword().getUserPassword(USER_EMAIL))
                    .isInstanceOf(GetUserPassword.UserNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Given one registered user")
    public class UserRegistered {

        @Test
        @DisplayName("When password for other user is requested. Then password exception is thrown")
        public void testExceptionIsThrownForOtherUser() {
            useCaseFactory.registerUser().registerNewUser(USER_EMAIL, PASSWORD);
            String otherEmail = "other@company.xyz";
            Assertions.assertThatThrownBy(() -> useCaseFactory.getUserPassword().getUserPassword(otherEmail))
                    .isInstanceOf(GetUserPassword.UserNotFoundException.class);
        }

        @Test
        @DisplayName("When password is requested. Then password is returned")
        public void testPasswordCanBeRetrieved() {
            useCaseFactory.registerUser().registerNewUser(USER_EMAIL, PASSWORD);
            Assertions.assertThat(useCaseFactory.getUserPassword().getUserPassword(USER_EMAIL))
                    .isEqualTo(PASSWORD);
        }
    }
}
