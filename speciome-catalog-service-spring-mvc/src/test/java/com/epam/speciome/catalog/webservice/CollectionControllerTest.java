package com.epam.speciome.catalog.webservice;

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

        mockMvc.perform(post("/collection").contentType(MediaType.APPLICATION_JSON).content(putBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.collectionId", Matchers.not(Matchers.emptyString())));
        String getResponse = mockMvc.perform(get("/collection/" + collectionId))
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
        mockMvc.perform(get("/collections"))
                .andExpect(status().isOk());
    }
    @Test
    public void testListCollectionsReturnsCreatedCollection() throws Exception {
        String collectionName1 = "Berries";
        String collectionName2 = "Tomatoes";
        String urlPath = "/collection";

        String putBody = createCollectionRequest(collectionName1);
        String putBody2 = createCollectionRequest(collectionName2);

        mockMvc.perform(post(urlPath).contentType(MediaType.APPLICATION_JSON).content(putBody));
        mockMvc.perform(post(urlPath).contentType(MediaType.APPLICATION_JSON).content(putBody2));

        String contentAsString = mockMvc.perform(get("/collections"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertTrue(contentAsString.contains(collectionName1));
        Assertions.assertTrue(contentAsString.contains(collectionName2));
    }
}
