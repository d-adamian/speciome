package com.epam.speciome.catalog.webservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(ServiceMvcTestConfiguration.class)
public class UserRegistrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUserRegistrationInvalidFormat() throws Exception {
        mockMvc.perform(MockRequestsUtil.postJson("/new-user", "INVALID"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        String requestContent = MockRequestsUtil.registerUserRequest("user1@aaa.com", "secret1");
        mockMvc.perform(MockRequestsUtil.postJson("/new-user", requestContent))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRegisterUserInvalidEmail() throws Exception {
        String requestContent = MockRequestsUtil.registerUserRequest("not_an_email", "secret2");
        mockMvc.perform(MockRequestsUtil.postJson("/new-user", requestContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterUserTwice() throws Exception {
        String requestContent = MockRequestsUtil.registerUserRequest("user1@aaa.com", "secret2");
        mockMvc.perform(MockRequestsUtil.postJson("/new-user", requestContent)).andDo(result ->
                mockMvc.perform(MockRequestsUtil.postJson("/new-user", requestContent))
                        .andExpect(status().isBadRequest()));
    }

}
