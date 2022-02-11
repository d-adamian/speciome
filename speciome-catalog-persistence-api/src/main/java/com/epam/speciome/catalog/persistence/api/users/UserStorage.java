package com.epam.speciome.catalog.persistence.api.users;

import com.epam.speciome.catalog.persistence.api.exceptions.UserIsNullException;

public interface UserStorage {

    void addUser(UserData userData);

    /**
     *
     * @throws UserIsNullException
     * if a User with the given email cannot be found
     */
    UserData loadUserData(String email);
}
