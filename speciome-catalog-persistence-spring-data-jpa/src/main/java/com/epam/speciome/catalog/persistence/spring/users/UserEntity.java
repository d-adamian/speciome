package com.epam.speciome.catalog.persistence.spring.users;

import com.epam.speciome.catalog.persistence.api.users.UserData;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "user")
@Table(name = "users", schema = "catalog")
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
        return new UserEntity(userData.email(), userData.passwordHash());
    }

    public UserData toUserData() {
        return new UserData(email, passwordHash);
    }
}
