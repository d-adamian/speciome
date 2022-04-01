package com.epam.speciome.catalog.domain.samples;

import com.epam.speciome.catalog.domain.exceptions.ArchivalStatusException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SortSampleListParamsTest {

    @Nested
    @DisplayName("When supply all null params")
    public class OptionalsFromNullParamsTest {

        @Test
        @DisplayName("Then return all empty optional")
        public void testReturningEmptyOptionals() {
            String archiveStatus = null;
            String sortedAttribute = null;
            String orderedAttribute = null;

            SortSampleListParams testParams = new SortSampleListParams(archiveStatus, sortedAttribute, orderedAttribute);

            assertAll(
                    () -> assertEquals(Optional.empty(), testParams.getArchivalStatusAttribute()),
                    () -> assertEquals(Optional.empty(), testParams.getSortedAttribute()),
                    () -> assertEquals(Optional.empty(), testParams.getOrderedAttribute())
            );
        }
    }

    @Nested
    @DisplayName("When supply sortby param without orderby")
    public class ValidationSortByParamWithoutOrderByParamTest {

        @Test
        @DisplayName("Then throwing SortedAttributeException")
        public void testThrowingException() {
            String archiveStatus = null;
            String sortedAttribute = "collectorName";
            String orderedAttribute = null;

            Assertions.assertThatThrownBy(() -> {
                        SortSampleListParams testParamsWithStringConstructor = new SortSampleListParams(archiveStatus, sortedAttribute, orderedAttribute);
                    })
                    .isInstanceOf(SortedAttributeException.class)
                    .hasMessage("Sorted attribute not found: 'orderby' don't present");

            Optional<ArchivalStatus> archiveStatusOptional = Optional.empty();
            Optional<String> sortedAttributeOptional = Optional.of("collectorName");
            Optional<Boolean> orderedAttributeOptional = Optional.empty();

            Assertions.assertThatThrownBy(() -> {
                        SortSampleListParams testParamsWithOptionalConstructor = new SortSampleListParams(
                                archiveStatusOptional,
                                sortedAttributeOptional,
                                orderedAttributeOptional);
                    })
                    .isInstanceOf(SortedAttributeException.class)
                    .hasMessage("Sorted attribute not found: 'orderby' don't present");
        }
    }

    @Nested
    @DisplayName("When supply orderby param without sortby")
    public class ValidationOrderByParamWithoutSortByParamTest {

        @Test
        @DisplayName("Then throwing SortedAttributeException")
        public void testThrowingException() {
            String archiveStatus = null;
            String sortedAttribute = null;
            String orderedAttribute = "asc";

            Assertions.assertThatThrownBy(() -> {
                        SortSampleListParams testParams = new SortSampleListParams(archiveStatus, sortedAttribute, orderedAttribute);
                    })
                    .isInstanceOf(SortedAttributeException.class)
                    .hasMessage("Sorted attribute not found: 'sortby' don't present");

            Optional<ArchivalStatus> archiveStatusOptional = Optional.empty();
            Optional<String> sortedAttributeOptional = Optional.empty();
            Optional<Boolean> orderedAttributeOptional = Optional.of(true);

            Assertions.assertThatThrownBy(() -> {
                        SortSampleListParams testParamsWithOptionalConstructor = new SortSampleListParams(
                                archiveStatusOptional,
                                sortedAttributeOptional,
                                orderedAttributeOptional);
                    })
                    .isInstanceOf(SortedAttributeException.class)
                    .hasMessage("Sorted attribute not found: 'sortby' don't present");
        }
    }

    @Nested
    @DisplayName("When supply incorrect sortby param")
    public class ValidationIncorrectNameOfSortByParamTest {

        @Test
        @DisplayName("Then throwing SortedAttributeException")
        public void testThrowingException() {
            String archiveStatus = null;
            String sortedAttribute = "some incorrect name of attribute";
            String orderedAttribute = "asc";

            Assertions.assertThatThrownBy(() -> {
                        SortSampleListParams testParams = new SortSampleListParams(archiveStatus, sortedAttribute, orderedAttribute);
                    })
                    .isInstanceOf(SortedAttributeException.class)
                    .hasMessage("Sorted attribute not found: some incorrect name of attribute");
        }
    }

    @Nested
    @DisplayName("When supply incorrect orderby param")
    public class ValidationIncorrectNameOfOrderByParamTest {

        @Test
        @DisplayName("Then throwing SortedAttributeException")
        public void testThrowingException() {
            String archiveStatus = null;
            String sortedAttribute = "collectorName";
            String orderedAttribute = "some incorrect name of order";

            Assertions.assertThatThrownBy(() -> {
                        SortSampleListParams testParams = new SortSampleListParams(archiveStatus, sortedAttribute, orderedAttribute);
                    })
                    .isInstanceOf(SortedAttributeException.class)
                    .hasMessage("Sorted attribute not found: some incorrect name of order");
        }
    }

    @Nested
    @DisplayName("When supply incorrect archiveStatus param")
    public class ValidationIncorrectNameOfArchiveStatusParamTest {

        @Test
        @DisplayName("Then throwing ArchivalStatusException")
        public void testThrowingException() {
            String archiveStatus = "some incorrect name of archive status";
            String sortedAttribute = null;
            String orderedAttribute = null;

            Assertions.assertThatThrownBy(() -> {
                        SortSampleListParams testParams = new SortSampleListParams(archiveStatus, sortedAttribute, orderedAttribute);
                    })
                    .isInstanceOf(ArchivalStatusException.class)
                    .hasMessage("Archival status not found: some incorrect name of archive status");
        }
    }

    @Nested
    @DisplayName("When supply correct archiveStatus param")
    public class ValidationCorrectNameOfArchiveStatusParamTest {

        @Test
        @DisplayName("Then return correct optional")
        public void testReturningCorrectOptional() {
            String archiveStatus = "UNARCHIVED";
            String sortedAttribute = null;
            String orderedAttribute = null;

            SortSampleListParams testParams = new SortSampleListParams(archiveStatus, sortedAttribute, orderedAttribute);

            assertAll(
                    () -> assertTrue(testParams.getArchivalStatusAttribute().isPresent()),
                    () -> assertEquals(ArchivalStatus.UNARCHIVED, testParams.getArchivalStatusAttribute().get())
            );
        }
    }

    @Nested
    @DisplayName("When supply correct sortby and orderby param")
    public class ValidationCorrectNameOfSortByAndOrderByParamsTest {

        @Test
        @DisplayName("Then return correct optionals")
        public void testReturningCorrectOptional() {
            String archiveStatus = null;
            String sortedAttribute = "collectorName";
            String orderedAttribute = "desc";

            SortSampleListParams testParams = new SortSampleListParams(archiveStatus, sortedAttribute, orderedAttribute);

            assertAll(
                    () -> assertTrue(testParams.getSortedAttribute().isPresent()),
                    () -> assertTrue(testParams.getOrderedAttribute().isPresent()),
                    () -> assertEquals("collectorName", testParams.getSortedAttribute().get()),
                    () -> assertEquals(false, testParams.getOrderedAttribute().get())
            );
        }
    }
}
