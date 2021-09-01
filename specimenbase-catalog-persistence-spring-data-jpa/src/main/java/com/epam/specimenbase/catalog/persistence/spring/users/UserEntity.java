package com.epam.specimenbase.catalog.persistence.spring.users;

import com.epam.specimenbase.catalog.persistence.api.users.UserData;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "user")
public class UserEntity {

    @Id
    private String email;
    private String passwordHash;

    @SuppressWarnings("unused") // used by Hibernate
    public UserEntity() {

    }

    private UserEntity(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public static UserEntity fromUserData(UserData userData) {
        return new UserEntity(userData.getEmail(), userData.getPasswordHash());
    }

    public UserData toUserData() {
        return new UserData(email, passwordHash);
    }
}
