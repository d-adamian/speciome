package com.epam.speciome.catalog.persistence.api.users;

import com.epam.speciome.catalog.persistence.api.exceptions.UserIsNullException;
import com.google.common.annotations.VisibleForTesting;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@VisibleForTesting
public interface UserStorageContract {
    UserStorage userStorage();

    @Test
    @DisplayName("When no users have been added. Then exception is thrown")
    default void testExceptionIsThrown() {
        Assertions.assertThatThrownBy(() ->userStorage().loadUserData(getUserEmail()))
                        .isInstanceOf(UserIsNullException.class);
    }

    @Test
    @DisplayName("When user has been added to storage. Then user can be found in storage")
    default void testUserCanBeFoundWhenAdded() {
        UserStorage userStorage = userStorage();
        addUserToStorage(userStorage);
        Assertions.assertThat(userStorage().loadUserData(getUserEmail())).isNotNull();
    }

    @Test
    @DisplayName("When user has been added to storage. Then user attributes in storage are equal to given values")
    default void testUserHasGivenAttributeValues() {
        UserStorage userStorage = userStorage();
        addUserToStorage(userStorage);
        UserData userData = userStorage.loadUserData(getUserEmail());
        Assertions.assertThat(userData.passwordHash()).isEqualTo(getPasswordHash());
    }

    @Test
    @DisplayName("When user has been added to storage. Then retrieving an another user throws an exception")
    default void testOtherUserThrowsException() {
        UserStorage userStorage = userStorage();
        addUserToStorage(userStorage);
        Assertions.assertThatThrownBy(() -> userStorage.loadUserData("other@email.com"))
                        .isInstanceOf(UserIsNullException.class);
    }

    @Test
    @DisplayName("When user has been added to storage. Then it can not be added again")
    default void testUserCanNotBeAddedTwice() {
        UserStorage userStorage = userStorage();
        addUserToStorage(userStorage);
        Assertions.assertThatThrownBy(() -> addUserToStorage(userStorage))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    private static void addUserToStorage(UserStorage userStorage) {
        userStorage.addUser(new UserData(getUserEmail(), getPasswordHash()));
    }

    private static String getUserEmail() {
        return "user1@company.com";
    }

    private static String getPasswordHash() {
        return "MOCK_PASSWORD_HASH_VALUE";
    }
}
