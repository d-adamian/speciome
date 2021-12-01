package com.epam.speciome.catalog.domain.samples;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class ArchivalStatusTest {

    @Nested
    @DisplayName("When Optional of Null is passed to the method")
    public class EmptyInputString {

        String emptyString;

        @BeforeEach
        public void setUp() {
            emptyString = null;
        }

        @Test
        @DisplayName("Then the method returns an empty archival status")
        public void testGetEmptyEnum() {
            Optional<ArchivalStatus> archivalStatus = ArchivalStatus.defineArchivalStatus(emptyString);
            Assertions.assertThat(archivalStatus.isEmpty()).isEqualTo(true);
        }
    }

    @Nested
    @DisplayName("When an invalid String is passed to the method")
    public class WrongInputParameters {

        boolean caught;
        String wrongInput;
        String expectedMessage;
        String resultedMessage;

        @BeforeEach
        public void setUp() {
            caught = false;
            wrongInput = "Anything";
            expectedMessage = "Archival status not found: " + wrongInput;
            resultedMessage = null;
        }

        @Test
        @DisplayName("Then InvalidInputException is thrown")
        public void testGetInvalidEnum() {
            try{
                ArchivalStatus.defineArchivalStatus(wrongInput);
            } catch (ArchivalStatusException exception) {
                caught = true;
            }
            Assertions.assertThat(caught).isEqualTo(true);
        }

        @Test
        @DisplayName("And exception message reports the invalid input parameter")
        public void testGetRequiredStatusInException() {
            try{
                ArchivalStatus.defineArchivalStatus(wrongInput);
            } catch (ArchivalStatusException exception) {
                resultedMessage = exception.getMessage();
            }
            Assertions.assertThat(expectedMessage).isEqualTo(resultedMessage);
        }
    }

    @Nested
    @DisplayName("When a valid String is passed to the method")
    public class EnumFromString {

        String testString;
        ArchivalStatus archivalStatus;

        @BeforeEach
        public void setUp() {
            testString = "ARCHIVED";
            archivalStatus = ArchivalStatus.defineArchivalStatus(testString).get();
        }

        @Test
        @DisplayName("Then corresponding archival status is returned")
        public void testGetValidEnum() {
            Assertions.assertThat(archivalStatus).isEqualTo(ArchivalStatus.ARCHIVED);
        }
    }
}