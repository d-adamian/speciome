package com.epam.speciome.catalog.domain.users;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Given no registered users")
public class UserRegistrationTest {
    private static final String USER_1 = "user1@company1.com";
    private static final String PASSWORD_1 = "password_1";

    private UseCaseFactory useCaseFactory;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
    }


    @Nested
    @DisplayName("When no actions performed")
    public class NoActionsPerformed {

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
        @DisplayName("Then user 1 can not register one more time")
        public void testUser1CanNotRegisterTwice() {
            RegisterUser registerUser = useCaseFactory.registerUser();
            Assertions.assertThatThrownBy(() -> registerUser.registerNewUser(USER_1, PASSWORD_1))
                    .isInstanceOf(RegisterUser.UserAlreadyExistsException.class);
        }
    }
}