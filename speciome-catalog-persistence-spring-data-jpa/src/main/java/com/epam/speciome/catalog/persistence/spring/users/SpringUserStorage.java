package com.epam.speciome.catalog.persistence.spring.users;

import com.epam.speciome.catalog.persistence.api.exceptions.UserIsNullException;
import com.epam.speciome.catalog.persistence.api.users.UserAlreadyExistsException;
import com.epam.speciome.catalog.persistence.api.users.UserData;
import com.epam.speciome.catalog.persistence.api.users.UserStorage;

import java.util.Optional;

public class SpringUserStorage implements UserStorage {
    private final UserJpaRepository repository;

    public SpringUserStorage(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addUser(UserData userData) {
        String email = userData.email();
        boolean isNewUser = repository.findByEmail(email).isEmpty();
        if (isNewUser) {
            repository.save(UserEntity.fromUserData(userData));
        } else {
            throw new UserAlreadyExistsException(email);
        }
    }

    @Override
    public UserData loadUserData(String email) {
        Optional<UserData> userDataOptional = repository.findByEmail(email).stream()
                .map(UserEntity::toUserData)
                .findFirst();
        if (userDataOptional.isEmpty()) {
            throw new UserIsNullException(email);
        }
        return userDataOptional.get();
    }
}
