package ru.otus.spring.course.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Как нибудь потом :)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    @BeforeEach
    void before() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testAuthenticatedOnAdminWithCorrectRole() throws Exception {
        mvc.perform(get("/auth"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_OTHER"}
    )
    @Test
    public void testAuthenticatedOnAdminWithRoleOther() throws Exception {
        mvc.perform(get("/auth"))
                .andExpect(status().isForbidden());
    }

}
