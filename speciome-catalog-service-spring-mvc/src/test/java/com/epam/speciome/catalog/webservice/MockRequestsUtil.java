package com.epam.speciome.catalog.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public final class MockRequestsUtil {
    static MockHttpServletRequestBuilder postJson(String url, String requestContent) {
        return post(url).content(requestContent).contentType(MediaType.APPLICATION_JSON);
    }

    static String registerUserRequest(String email, String password) throws JsonProcessingException {
        Map<String, String> params = Map.of("email", email, "password", password);
        return new ObjectMapper().writeValueAsString(params);
    }

}
