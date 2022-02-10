package com.epam.speciome.catalog.persistence.spring;

import com.epam.speciome.catalog.persistence.api.users.UserStorage;
import com.epam.speciome.catalog.persistence.api.users.UserStorageContract;
import com.epam.speciome.catalog.persistence.spring.users.SpringUserStorage;
import com.epam.speciome.catalog.persistence.spring.users.UserJpaRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureEmbeddedDatabase(beanName = "DataSource", provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@DisplayName("Test Spring Data JPA user storage implementation")
public class SpringUserStorageTest implements UserStorageContract {
    private UserStorage userStorage;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void setUp() {
        userStorage = new SpringUserStorage(userJpaRepository);
    }

    @AfterEach
    void tearDown() {
        userJpaRepository.deleteAll();
    }

    @Override
    public UserStorage userStorage() {
        return userStorage;
    }
}
