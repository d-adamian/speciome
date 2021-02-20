package com.epam.specimenbase.catalog.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Given any user")
public class GetUserDetailsTest {

    @Nested
    @DisplayName("When user is authenticated")
    class Authenticated {
        @Test
        @DisplayName("Then user email is returned")
        public void testReturnsEmail() {
            GetUserDetails useCase = new GetUserDetails();
            var result = useCase.execute();
            Assertions.assertThat(result).isNotNull();
            Assertions.assertThat(result.getEmail()).containsOnlyOnce("@");
        }

    }

}
