package com.epam.speciome.catalog.persistence.spring.users;

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
        String email = userData.getEmail();
        boolean isNewUser = repository.findByEmail(email).isEmpty();
        if (isNewUser) {
            repository.save(UserEntity.fromUserData(userData));
        } else {
            throw new UserAlreadyExistsException(email);
        }
    }

    @Override
    public Optional<UserData> loadUserData(String email) {
        return repository.findByEmail(email).stream()
                .map(UserEntity::toUserData)
                .findFirst();
    }
}
