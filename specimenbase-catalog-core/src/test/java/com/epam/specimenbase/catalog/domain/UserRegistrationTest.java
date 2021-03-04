package com.epam.specimenbase.catalog.domain;

import com.epam.specimenbase.catalog.ports.UseCaseFactory;
import com.epam.specimenbase.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

@DisplayName("Given empty users storage")
public class UserRegistrationTest {
    private static final String USER_1 = "user1@company1.com";
    private static final String PASSWORD_1 = "password_1";

    private static final String USER_2 = "user2@company2.com";
    private static final String PASSWORD_2 = "password_2";

    private UseCaseFactory useCaseFactory;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
    }


    @Nested
    @DisplayName("When no actions performed")
    public class NoActionsPerformed {

        @Test
        @DisplayName("Then no user can log in")
        public void testNoUserCanLogIn() {
            LogInUser logInUser = useCaseFactory.logInUser();
            Assertions.assertThatThrownBy(() -> logInUser.logIn(USER_1, PASSWORD_1))
                    .isInstanceOf(LogInUser.InvalidCredentialsException.class);
        }
    }

    @Nested
    @DisplayName("When user 1 has been registered")
    public class User1Registered {

        @BeforeEach
        public void setUp() {
            RegisterUser registerUser = useCaseFactory.registerUser();
            registerUser.registerNewUser(USER_1, PASSWORD_1);
        }

        @Test
        @DisplayName("Then user 1 can log in with correct password")
        public void testUser1CanLogIn() {
            LogInUser logInUser = useCaseFactory.logInUser();
            User user = logInUser.logIn(USER_1, PASSWORD_1);
            Assertions.assertThat(user).isNotNull();
        }

        @Test
        @DisplayName("Then user 1 can not log in with incorrect password")
        public void testUser1CantLogInWithIncorrectPassword() {
            LogInUser logInUser = useCaseFactory.logInUser();
            Assertions.assertThatThrownBy(() -> logInUser.logIn(USER_1, PASSWORD_2))
                    .isInstanceOf(LogInUser.InvalidCredentialsException.class);
        }
    }
}
