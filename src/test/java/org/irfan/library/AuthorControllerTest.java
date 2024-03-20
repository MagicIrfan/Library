package org.irfan.library;

import org.irfan.library.services.JwtTokenService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({SecurityTestConfig.class, TestDataInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestDataInitializer testDataInitializer;
    @Autowired
    private JwtTokenService jwtTokenService;
    private static final String API_AUTHORS_BASE_URL = "/api/v1/authors";
    private static final String APPLICATION_JSON_UTF8 = "application/json";
    private String tokenAdmin;
    private String tokenUser;
    private String tokenAdminInvalid;
    private String tokenUserInvalid;

    @BeforeEach
    public void setUp() {
        testDataInitializer.init();
        this.tokenAdmin = this.jwtTokenService.createToken("Irfan");
        this.tokenUser = this.jwtTokenService.createToken("Irfane");
        this.tokenAdminInvalid = this.jwtTokenService.createToken("Irfan",0);
        this.tokenUserInvalid = this.jwtTokenService.createToken("Irfane",0);
    }

    @AfterEach
    public void clear() {
        testDataInitializer.clear();
    }

    @Test
    public void testGetAuthorsAsAdminAndUser() throws Exception {
        performGetRequestWithTokenOk(API_AUTHORS_BASE_URL, tokenAdmin);
        performGetRequestWithTokenOk(API_AUTHORS_BASE_URL, tokenUser);
    }

    @Test
    public void testGetAuthorsAsAdminAndUserTokenInvalid() throws Exception {
        performGetRequestWithToken(API_AUTHORS_BASE_URL, tokenAdminInvalid,status().isUnauthorized());
        performGetRequestWithToken(API_AUTHORS_BASE_URL, tokenUserInvalid,status().isUnauthorized());
    }

    @Test
    public void testGetOneAuthorAsAdminAndUser() throws Exception {
        performGetRequestWithTokenOk(API_AUTHORS_BASE_URL + "/1", tokenAdmin);
        performGetRequestWithTokenOk(API_AUTHORS_BASE_URL + "/1", tokenUser);
    }

    @Test
    public void testGetOneAuthorAsAdminAndUserTokenInvalid() throws Exception {
        performGetRequestWithToken(API_AUTHORS_BASE_URL + "/1", tokenAdminInvalid,status().isUnauthorized());
        performGetRequestWithToken(API_AUTHORS_BASE_URL + "/1", tokenUserInvalid,status().isUnauthorized());
    }

    @Test
    public void testPatchOneAuthorAsAdmin() throws Exception {
        String patchContent = "{\"firstname\": \"Irfan\"}";
        performPatchRequestWithToken(API_AUTHORS_BASE_URL + "/1", tokenAdmin, patchContent, status().isOk());
        performGetRequestWithToken(API_AUTHORS_BASE_URL + "/1", tokenAdmin, jsonPath("firstname").value("Irfan"));

        // Test user attempt to patch, expected to be forbidden
        performPatchRequestWithToken(API_AUTHORS_BASE_URL + "/1", tokenUser, patchContent, status().isForbidden());
    }

    @Test
    public void testDeleteOneAuthorAsAdmin() throws Exception {
        performDeleteRequestWithToken(API_AUTHORS_BASE_URL + "/1", tokenAdmin, status().isOk());
        performDeleteRequestWithToken(API_AUTHORS_BASE_URL + "/1", tokenUser, status().isForbidden());
    }

    // Auxiliary methods to reduce repetition
    private void performGetRequestWithToken(String url, String token, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(get(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(expectedStatus);
    }

    private void performGetRequestWithTokenOk(String url, String token) throws Exception {
        mockMvc.perform(get(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    private void performGetRequestWithToken(String url, String token, ResultMatcher expectedStatus, ResultMatcher value) throws Exception {
        mockMvc.perform(get(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(expectedStatus)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(value);
    }

    private void performPatchRequestWithToken(String url, String token, String content, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(patch(url)
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(content))
                .andExpect(expectedStatus);
    }

    private void performDeleteRequestWithToken(String url, String token, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(delete(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(expectedStatus);
    }
}
