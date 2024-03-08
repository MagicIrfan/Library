package org.irfan.library;

import org.irfan.library.Model.Role;
import org.irfan.library.Model.User;
import org.irfan.library.dao.UserRepository;
import org.irfan.library.enums.RoleEnum;
import org.irfan.library.services.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class BookTypeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Test
    public void testGetBookTypes() throws Exception {
        String token =jwtTokenService.createToken("Irfan");
        mockMvc.perform(get("/api/v1/booktypes")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void testUserIsAdmin() throws Exception {

    }
}
