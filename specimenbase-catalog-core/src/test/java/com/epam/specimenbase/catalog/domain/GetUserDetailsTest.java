package com.epam.specimenbase.catalog.domain;

import com.epam.specimenbase.catalog.ports.UseCaseFactory;
import com.epam.specimenbase.catalog.tests.TestsUseCaseFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Given any user")
public class GetUserDetailsTest {
    private UseCaseFactory useCaseFactory;

    @BeforeEach
    public void setUp() {
        useCaseFactory = new TestsUseCaseFactory();
    }

    @Nested
    @DisplayName("When user is authenticated")
    public class Authenticated {
        @Test
        @DisplayName("Then user email is returned")
        public void testReturnsEmail() {
            GetUserDetails useCase = useCaseFactory.getUserDetails();
            var result = useCase.getUserDetails();
            Assertions.assertThat(result).isNotNull();
            Assertions.assertThat(result.getEmail()).containsOnlyOnce("@");
        }

    }

}
