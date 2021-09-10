package com.epam.speciome.catalog.webservice;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.persistence.spring.samples.SampleJpaRepository;
import com.epam.speciome.catalog.persistence.spring.samples.SpringSampleStorage;
import com.epam.speciome.catalog.persistence.spring.users.SpringUserStorage;
import com.epam.speciome.catalog.persistence.spring.users.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.epam.speciome.catalog.persistence.spring.*")
@EntityScan("com.epam.speciome.catalog.persistence.spring.*")
public class AppConfiguration {
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private SampleJpaRepository sampleJpaRepository;

    @Bean
    public UseCaseFactory useCaseFactory() {
        return new UseCaseFactory(
                new SpringUserStorage(userJpaRepository),
                new SpringSampleStorage(sampleJpaRepository)
        );
    }
}
