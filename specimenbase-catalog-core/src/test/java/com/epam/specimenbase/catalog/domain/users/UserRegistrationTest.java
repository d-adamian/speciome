package com.epam.specimenbase.catalog.domain.users;

import com.epam.specimenbase.catalog.UseCaseFactory;
import com.epam.specimenbase.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Given no registered users")
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
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @DisplayName("Then user can not register with invalid email")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"INVALID", "email@other@aaa.com"})
        public void testUserCanNotRegisterWithInvalidEmail(String email) {
            RegisterUser registerUser = useCaseFactory.registerUser();
            Assertions.assertThatThrownBy(() -> registerUser.registerNewUser(email, PASSWORD_1))
                    .isInstanceOf(RegisterUser.EmailInvalidException.class);
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
            Assertions.assertThat(user.getEmail()).isEqualTo(USER_1);
        }

        @Test
        @DisplayName("Then user 1 can not log in with incorrect password")
        public void testUser1CantLogInWithIncorrectPassword() {
            LogInUser logInUser = useCaseFactory.logInUser();
            Assertions.assertThatThrownBy(() -> logInUser.logIn(USER_1, PASSWORD_2))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @DisplayName("Then user 2 can not log in with any password")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {PASSWORD_1, PASSWORD_2, "other_password"})
        public void testUser2CantLogInWithAnyPassword(String password) {
            LogInUser logInUser = useCaseFactory.logInUser();
            Assertions.assertThatThrownBy(() -> logInUser.logIn(USER_2, password))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @Test
        @DisplayName("Then user 1 can not register one more time")
        public void testUser1CanNotRegisterTwice() {
            RegisterUser registerUser = useCaseFactory.registerUser();
            Assertions.assertThatThrownBy(() -> registerUser.registerNewUser(USER_1, PASSWORD_1))
                    .isInstanceOf(RegisterUser.UserAlreadyExistsException.class);
        }
    }

    @Nested
    @DisplayName("When both users have been registered")
    public class BothUsersRegistered {
        @BeforeEach
        public void setUp() {
            useCaseFactory.registerUser().registerNewUser(USER_1, PASSWORD_1);
            useCaseFactory.registerUser().registerNewUser(USER_2, PASSWORD_2);
        }

        @ParameterizedTest
        @DisplayName("Then both users can log in")
        @CsvSource({
                USER_1 + ", " + PASSWORD_1,
                USER_2 + ", " + PASSWORD_2
        })
        public void testBothUsersCanLogIn(String email, String password) {
            User user = useCaseFactory.logInUser().logIn(email, password);
            Assertions.assertThat(user).isNotNull();
            Assertions.assertThat(user.getEmail()).isEqualTo(email);
        }
    }
}