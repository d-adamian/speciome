package com.epam.speciome.catalog.webservice;

import com.epam.speciome.catalog.domain.samples.Attributes;
import com.epam.speciome.catalog.persistence.testmocks.InMemoryMapSampleStorage;
import com.epam.speciome.catalog.webservice.models.SampleAttribute;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMapAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import(ServiceMvcTestConfiguration.class)
@WithMockUser
public class SampleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemoryMapSampleStorage sampleStorage;

    @BeforeEach
    void setUp() {
        sampleStorage.clear();
    }

    @Test
    public void testCreateSampleWithoutAttributesReturnsNonEmptySampleId() throws Exception {
        mockMvc.perform(post(ApiConstants.SAMPLE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sampleId", Matchers.not(Matchers.emptyString())));
    }

    @Test
    public void testGetSampleReturnsNotFoundWithoutCreatingSample() throws Exception {
        long sampleId = 1L;
        mockMvc.perform(get(ApiConstants.SAMPLE + "/" + sampleId))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testArchiveSampleReturnsNotFoundWithoutCreatingSample() throws Exception {
        long sampleId = 1L;
        mockMvc.perform(put(ApiConstants.SAMPLE + "/" + sampleId +  ApiConstants.ARCHIVE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUnArchiveSampleReturnsNotFoundWithoutCreatingSample() throws Exception {
        long sampleId = 1L;
        mockMvc.perform(put(ApiConstants.SAMPLE + "/" + sampleId +  ApiConstants.UNARCHIVE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPostAndGetSampleWithoutAttributes() throws Exception {
        long sampleId = postSampleWithoutAttributes();
        mockMvc.perform(get(ApiConstants.SAMPLE + "/" + sampleId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testPostSampleWithUnexpectedAttribute() throws Exception {
        String postBody = createSampleRequest("WRONG_ATTRIBUTE", "some_value");
        mockMvc.perform(post(ApiConstants.SAMPLE).contentType(MediaType.APPLICATION_JSON).content(postBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteNonExistentSampleReturnsNotFound() throws Exception {
        long sampleId = 5L;
        mockMvc.perform(delete(ApiConstants.SAMPLE + "/" + sampleId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteNonArchivedSampleReturnsNotFound() throws Exception {
        long sampleId = postSampleWithoutAttributes();
        String samplePath = ApiConstants.SAMPLE + "/" + sampleId;
        mockMvc.perform(delete(samplePath))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testSampleNotFoundAfterDeletion() throws Exception {
        long sampleId = postSampleWithoutAttributes();
        String samplePath = ApiConstants.SAMPLE + "/" + sampleId;
        mockMvc.perform(put(ApiConstants.SAMPLE + "/" + sampleId +  ApiConstants.ARCHIVE));
        mockMvc.perform(delete(samplePath)).andExpect(status().isNoContent());
        mockMvc.perform(get(samplePath)).andExpect(status().isNotFound());
    }

    @Test
    public void testPutNonexistentSampleReturnsNotFound() throws Exception {
        long sampleId = 5L;
        mockMvc.perform(put(ApiConstants.SAMPLE + "/" + sampleId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPutSampleUpdatesAttribute() throws Exception {
        long sampleId = postSampleWithoutAttributes();
        String attribute = Attributes.COLLECTOR_NAME;
        String value = "Collector 1";
        String putBody = createSampleRequest(attribute, value);
        String samplePath = ApiConstants.SAMPLE + "/" + sampleId;

        mockMvc.perform(put(samplePath).contentType(MediaType.APPLICATION_JSON).content(putBody))
                .andExpect(status().isNoContent());
        mockMvc.perform(get(samplePath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attributes[?(@.attribute=='" + attribute + "')].value").value(value));
    }

    @Test
    public void testListSamplesNoInputParameters() throws Exception {
        mockMvc.perform(get(ApiConstants.SAMPLES))
                .andExpect(status().isOk());
    }

    @Test
    public void testListSamplesWrongInputParameters() throws Exception {
        mockMvc.perform(get(ApiConstants.SAMPLES)
                        .param("archivalStatus", "anything"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(ApiConstants.SAMPLES)
                .params(new MultiValueMapAdapter<>(Map.of(
                        "archivalStatus", List.of("UNARCHIVED"),
                        "sortby", List.of("collectorName"),
                        "orderby", List.of("some incorrect name")))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(ApiConstants.SAMPLES)
                        .params(new MultiValueMapAdapter<>(Map.of(
                                "archivalStatus", List.of("UNARCHIVED"),
                                "sortby", List.of("some incorrect name"),
                                "orderby", List.of("desc")))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(ApiConstants.SAMPLES)
                        .params(new MultiValueMapAdapter<>(Map.of(
                                "archivalStatus", List.of("UNARCHIVED"),
                                "orderby", List.of("desc")))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(ApiConstants.SAMPLES)
                        .params(new MultiValueMapAdapter<>(Map.of(
                                "archivalStatus", List.of("UNARCHIVED"),
                                "sortby", List.of("some incorrect name")))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testListSamplesCorrectInputParameters() throws Exception {
        mockMvc.perform(get(ApiConstants.SAMPLES)
                        .params(new MultiValueMapAdapter<>(Map.of(
                                "archivalStatus", List.of("UNARCHIVED"),
                                "sortby", List.of("collectorName"),
                                "orderby", List.of("desc")))))
                .andExpect(status().isOk());
    }

    @Test
    public void testListUnarchivedSamplesReturnsUnarchivedSample() throws Exception {
        long sampleId = postSampleWithoutAttributes();
        String postResponse = mockMvc.perform(get(ApiConstants.SAMPLES)
                        .param("archivalStatus", "UNARCHIVED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalCount").value(1))
                .andExpect(jsonPath("$.samples[0].sampleId").value(sampleId))
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(postResponse.contains("\"archived\":false"));

    }

    @Test
    public void testListArchivedSamplesReturnsArchivedSample() throws Exception {
        long sampleId = postSampleWithoutAttributes();
        mockMvc.perform(put(ApiConstants.SAMPLE + "/"+ sampleId + ApiConstants.ARCHIVE));
        String postResponse = mockMvc.perform(get(ApiConstants.SAMPLES)
                        .param("archivalStatus", "ARCHIVED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalCount").value(1))
                .andExpect(jsonPath("$.samples[0].sampleId").value(sampleId))
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(postResponse.contains("\"archived\":true"));
    }

    @Test
    public void testImportFromCsv() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(ApiConstants.SAMPLES_UPLOAD_CSV)
                .file(getTestCsv("src/test/resources/samples.csv"))
                .contentType("multipart/form-data"))
                .andExpect(status().isOk());
    }

    @Test
    public void testImportExcessColumns() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(ApiConstants.SAMPLES_UPLOAD_CSV)
                        .file(getTestCsv("src/test/resources/excess_columns.csv"))
                        .contentType("multipart/form-data"))
                .andExpect(status().isOk());
    }

    @Test
    public void testImportUnsupportedContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(ApiConstants.SAMPLES_UPLOAD_CSV)
                        .file(getTestCsv("src/test/resources/invalid_data.csv"))
                        .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetCsv() throws Exception {
        String postResponse = mockMvc.perform(get(ApiConstants.SAMPLES_DOWNLOAD))
                .andExpect(content().contentType("application/csv"))
                .andReturn().getResponse().getContentAsString();
        assertTrue(postResponse.contains("sampleTaxonomy"));
    }

    private long postSampleWithoutAttributes() throws Exception {
        String postResponse = mockMvc.perform(post(ApiConstants.SAMPLE))
                .andReturn().getResponse().getContentAsString();
        return new ObjectMapper().readTree(postResponse).findValue("sampleId").asLong();
    }

    private String createSampleRequest(String attribute, String value) throws JsonProcessingException {
        Map<String, Object> postJson = Map.of("attributes", List.of(new SampleAttribute(attribute, value)));

        return new ObjectMapper().writeValueAsString(postJson);
    }

    private MockMultipartFile getTestCsv(String fileName) throws IOException {
        return new MockMultipartFile("file", "file", "text/csv",
                new FileInputStream(new File(fileName)));
    }
}
