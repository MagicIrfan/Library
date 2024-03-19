package org.irfan.library;

import jakarta.inject.Inject;
import org.irfan.library.controllers.AuthorController;
import org.irfan.library.services.AuthorService;
import org.irfan.library.Model.Author;
import org.irfan.library.services.JwtTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({SecurityTestConfig.class, TestDataInitializer.class})
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataInitializer testDataInitializer;

    @BeforeEach
    public void setUp() {
        testDataInitializer.init();
    }

    @AfterEach
    public void clear() {
        testDataInitializer.clear();
    }

    @Test
    public void testGetAuthors() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].firstname").value("Albert"))
                .andExpect(jsonPath("$[0].lastname").value("Camus"));
    }

    @Test
    public void testGetOneAuthor() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstname").value("Albert"))
                .andExpect(jsonPath("lastname").value("Camus"));
    }

    @Test
    public void testPatchOneAuthor() throws Exception {
        String patchAuthor = "{\"firstname\": \"Irfan\"}";
        // When & Then
        mockMvc.perform(patch("/api/v1/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchAuthor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstname").value("Irfan"))
                .andExpect(jsonPath("lastname").value("Camus"));
    }

    @Test
    public void testDeleteOneAuthor() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/authors/1"))
                .andExpect(status().isOk());
    }
}
