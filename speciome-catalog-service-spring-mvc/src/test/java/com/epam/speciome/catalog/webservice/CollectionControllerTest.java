package com.epam.speciome.catalog.webservice;

import com.epam.speciome.catalog.persistence.api.collections.CollectionData;
import com.epam.speciome.catalog.persistence.testmocks.InMemoryMapCollectionStorage;
import com.epam.speciome.catalog.webservice.models.CollectionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import(ServiceMvcTestConfiguration.class)
@WithMockUser
public class CollectionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemoryMapCollectionStorage collectionStorage;

    @BeforeEach
    void setUp() {
        collectionStorage.clear();
    }

    @Test
    public void testPostAndGetCollection() throws Exception {
        String putBody = createCollectionRequest("Berries");
        long collectionId = 1;

        mockMvc.perform(post(ApiConstants.COLLECTION).contentType(MediaType.APPLICATION_JSON).content(putBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.collectionId", Matchers.not(Matchers.emptyString())));
        String getResponse = mockMvc.perform(get(ApiConstants.COLLECTION + "/" + collectionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(getResponse.contains("Berries"));
    }

    private String createCollectionRequest(String collectionName) throws JsonProcessingException {
        CollectionRequest request = new CollectionRequest(collectionName);
        return new ObjectMapper().writeValueAsString(request);
    }

    @Test
    public void testListEmptyCollections() throws Exception {
        mockMvc.perform(get(ApiConstants.COLLECTIONS))
                .andExpect(status().isOk());
    }

    @Test
    public void testListCollectionsReturnsCreatedCollection() throws Exception {
        String collectionName1 = "Berries";
        String collectionName2 = "Tomatoes";
        String urlPath = ApiConstants.COLLECTION;

        String putBody = createCollectionRequest(collectionName1);
        String putBody2 = createCollectionRequest(collectionName2);

        mockMvc.perform(post(urlPath).contentType(MediaType.APPLICATION_JSON).content(putBody));
        mockMvc.perform(post(urlPath).contentType(MediaType.APPLICATION_JSON).content(putBody2));

        String contentAsString = mockMvc.perform(get(ApiConstants.COLLECTIONS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertTrue(contentAsString.contains(collectionName1));
        Assertions.assertTrue(contentAsString.contains(collectionName2));
    }

    @Test
    public void testArchiveCollectionReturnsNotFoundWithoutCreatingCollections() throws Exception {
        long collectionId = 1L;
        mockMvc.perform(put(ApiConstants.COLLECTION + "/" + collectionId + ApiConstants.ARCHIVE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnArchiveCollectionReturnsNotFoundWithoutCreatingCollections() throws Exception {
        long collectionId = 1L;
        mockMvc.perform(put(ApiConstants.COLLECTION + "/" + collectionId + ApiConstants.UNARCHIVE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testWhenMakeCollectionArchiveReturnStatusSuccessfullyAndCorrectJson() throws Exception {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC"));
        Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
        collectionStorage.addCollection(new CollectionData(
                "Animals",
                timestamp,
                timestamp,
                "test@mail.ru",
                false));
        long collectionId = 1L;
        String responseBody = mockMvc.perform(put(ApiConstants.COLLECTION + "/" + collectionId + ApiConstants.ARCHIVE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertTrue(responseBody.contains("\"archived\":true"));
    }

    @Test
    public void testWhenMakeCollectionUnArchiveReturnStatusSuccessfullyAndCorrectJson() throws Exception {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC"));
        Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
        collectionStorage.addCollection(new CollectionData(
                "Animals",
                timestamp,
                timestamp,
                "test@mail.ru",
                true));
        long collectionId = 1L;
        String responseBody = mockMvc.perform(put(ApiConstants.COLLECTION + "/" + collectionId + ApiConstants.UNARCHIVE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertTrue(responseBody.contains("\"archived\":false"));
    }

    @Test
    public void testDeleteNonExistentCollectionReturnsNotFound() throws Exception {
        long collectionId = 12345L;
        mockMvc.perform(delete(ApiConstants.COLLECTION + "/" + collectionId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteNonArchivedCollectionReturnsNotFound() throws Exception {
        String putBody = createCollectionRequest("Berries");
        long collectionId = 1;
        String collectionPath = ApiConstants.COLLECTION + "/" + collectionId;
        mockMvc.perform(post(ApiConstants.COLLECTION).contentType(MediaType.APPLICATION_JSON).content(putBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.collectionId", Matchers.not(Matchers.emptyString())));
        mockMvc.perform(delete(collectionPath))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCollectionNotFoundAfterDeletion() throws Exception {
        String putBody = createCollectionRequest("Berries");
        long collectionId = 1;
        String collectionPath = ApiConstants.COLLECTION + "/" + collectionId;

        mockMvc.perform(post(ApiConstants.COLLECTION).contentType(MediaType.APPLICATION_JSON).content(putBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.collectionId", Matchers.not(Matchers.emptyString())));


        mockMvc.perform(put(ApiConstants.COLLECTION + "/" + collectionId + ApiConstants.ARCHIVE));
        mockMvc.perform(delete(collectionPath)).andExpect(status().isOk());
        mockMvc.perform(get(collectionPath)).andExpect(status().isNotFound());
    }
}
