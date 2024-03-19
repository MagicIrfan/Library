package org.irfan.library;

import jakarta.inject.Inject;
import org.irfan.library.controllers.AuthorController;
import org.irfan.library.services.AuthorService;
import org.irfan.library.Model.Author;
import org.irfan.library.services.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAuthors() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk());
        // Vous pouvez ajouter d'autres assertions ici pour vérifier le contenu de la réponse, etc.
    }
}
