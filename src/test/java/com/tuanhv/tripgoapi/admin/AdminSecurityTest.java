package com.tuanhv.tripgoapi.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminSecurityTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void anonymousShouldRedirectToLogin()
            throws Exception {

        mockMvc.perform(
                        get("/admin")
                )
                .andExpect(
                        status().is3xxRedirection()
                )
                .andExpect(
                        redirectedUrlPattern(
                                "**/admin/login"
                        )
                );
    }

    @Test
    @WithMockUser(
            username = "user@example.com",
            roles = "USER"
    )
    void userShouldNotAccessAdmin()
            throws Exception {

        mockMvc.perform(
                        get("/admin")
                )
                .andExpect(
                        status().isForbidden()
                );
    }

    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = "ADMIN"
    )
    void adminShouldAccessAdmin()
            throws Exception {

        mockMvc.perform(
                        get("/admin")
                )
                .andExpect(
                        status().isOk()
                );
    }
}
