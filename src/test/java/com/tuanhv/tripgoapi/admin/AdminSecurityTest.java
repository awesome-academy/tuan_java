package com.tuanhv.tripgoapi.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminSecurityTest {

    @Autowired
    private MockMvc mockMvc;

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
            username = "an@example.com",
            roles = "USER"
    )
    void userShouldNotAccessAdmin()
            throws Exception {

        mockMvc.perform(
                        get("/admin")
                )
                .andExpect(
                        status().isForbidden()
                )
                .andExpect(
                        forwardedUrl(
                                "/admin/access-denied"
                        )
                );
    }

    @Test
    @WithMockUser(
            username = "admin@tripgo.com",
            roles = "ADMIN"
    )
    void adminShouldAccessAdmin()
            throws Exception {

        mockMvc.perform(
                        get("/admin")
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        view().name(
                                "admin/dashboard"
                        )
                );
    }

    @Test
    @WithMockUser(
            username = "an@example.com",
            roles = "USER"
    )
    void accessDeniedPageShouldRender()
            throws Exception {

        mockMvc.perform(
                        get("/admin/access-denied")
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        view().name(
                                "admin/access-denied"
                        )
                )
                .andExpect(
                        content().string(
                                containsString(
                                        "Không có quyền truy cập"
                                )
                        )
                );
    }
}
