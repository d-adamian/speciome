package com.epam.specimenbase.catalog.persistence.spring.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByEmail(String email);
}
