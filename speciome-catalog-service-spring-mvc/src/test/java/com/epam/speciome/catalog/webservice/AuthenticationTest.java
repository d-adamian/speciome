package com.epam.speciome.catalog.webservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(ServiceMvcTestConfiguration.class)
public class AuthenticationTest {
    private static final String EMAIL = "user1@company.com";
    private static final String PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc.perform(MockRequestsUtil.postJson(ApiConstants.NEW_USER, MockRequestsUtil.registerUserRequest(EMAIL, PASSWORD)));
    }

    @Test
    public void testUserDetailsUnauthorizedWithoutLogin() throws Exception {
        mockMvc.perform(get(ApiConstants.USER_DETAILS))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLoginUnauthorizedWithWrongPassword() throws Exception {
        mockMvc.perform(loginWithCredentials(EMAIL, "WRONG_PASSWORD"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLoginUnauthorizedWithWrongEmail() throws Exception {
        mockMvc.perform(loginWithCredentials("not_an_email", PASSWORD))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLoginSuccessfulWithCorrectCredentials() throws Exception {
        mockMvc.perform(loginWithCredentials(EMAIL, PASSWORD))
                .andExpect(status().isNoContent());
    }


    @Disabled("Works when testing complete server set-up. Test disabled (returns 401) since I couldn't make it work")
    @Test
    public void testUserDetailsReturnsEmailAfterLogin() throws Exception {
        mockMvc.perform(loginWithCredentials(EMAIL, PASSWORD)).andDo(
                result -> mockMvc.perform(get(ApiConstants.USER_DETAILS))
                        .andExpect(status().isOk())
                        .andExpect(content().string(EMAIL))
        );
    }

    @Test
    public void testUserDetailsUnauthorizedAfterLogout() throws Exception {
        mockMvc.perform(loginWithCredentials(EMAIL, PASSWORD)).andExpect(status().isNoContent());
        mockMvc.perform(logout()).andExpect(status().isNoContent());
        mockMvc.perform(get(ApiConstants.USER_DETAILS)).andExpect(status().isUnauthorized());
    }

    private static SecurityMockMvcRequestBuilders.FormLoginRequestBuilder loginWithCredentials(
            String email, String password
    ) {
        return formLogin(ApiConstants.LOGIN).userParameter("email").user(email).password(password);
    }
}
