package com.epam.speciome.catalog.webservice;

import com.epam.speciome.catalog.domain.samples.Attributes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(ServiceMvcTestConfiguration.class)
@WithMockUser
public class AttributesListControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAllAttributesAreReturned() throws Exception {
        String expectedJson = new ObjectMapper().writeValueAsString(Attributes.ALL);
        mockMvc.perform(get("/attributes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }
}
