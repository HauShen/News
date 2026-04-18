package com.hau.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hau.news.config.JwtService;
import com.hau.news.models.UserProfile;
import com.hau.news.models.roles.Role;
import com.hau.news.repositories.UserRepository;
import com.hau.news.requestbodies.ArticleRequestBody;
import com.hau.news.requestbodies.UserRequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
public class RoleBasedAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private String adminToken;
    private String newsPosterToken;
    private String readerToken;
    private UserProfile adminUser;
    private UserProfile newsPosterUser;
    private UserProfile readerUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        adminUser = createUser("admin@test.com", "Admin User", Role.ADMIN);
        newsPosterUser = createUser("poster@test.com", "News Poster", Role.NEWS_POSTER);
        readerUser = createUser("reader@test.com", "Reader User", Role.READER);

        adminToken = jwtService.generateToken(adminUser);
        newsPosterToken = jwtService.generateToken(newsPosterUser);
        readerToken = jwtService.generateToken(readerUser);
    }

    private UserProfile createUser(String email, String name, Role role) {
        UserProfile user = new UserProfile();
        user.setUserId(UUID.randomUUID().toString());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setAge(25);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Test
    void adminCanCreateUser() throws Exception {
        UserRequestBody request = new UserRequestBody("New User", 20, Role.READER);

        mockMvc.perform(post("/user/create")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void readerCannotCreateUser() throws Exception {
        UserRequestBody request = new UserRequestBody("New User", 20, Role.READER);

        mockMvc.perform(post("/user/create")
                        .header("Authorization", "Bearer " + readerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/delete/" + readerUser.getUserId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void readerCannotDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/delete/" + newsPosterUser.getUserId())
                        .header("Authorization", "Bearer " + readerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void newsPosterCanCreateArticle() throws Exception {
        ArticleRequestBody request = new ArticleRequestBody("Title", "Content");

        mockMvc.perform(post("/article/create/" + newsPosterUser.getUserId())
                        .header("Authorization", "Bearer " + newsPosterToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void readerCannotCreateArticle() throws Exception {
        ArticleRequestBody request = new ArticleRequestBody("Title", "Content");

        mockMvc.perform(post("/article/create/" + readerUser.getUserId())
                        .header("Authorization", "Bearer " + readerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthenticatedCanAccessPublicNewsEndpoint() throws Exception {
        mockMvc.perform(get("/api/nytimesnews/home"))
                .andExpect(status().isOk());
    }

    @Test
    void unauthenticatedCannotAccessProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/user/get/some-id"))
                .andExpect(status().isForbidden());
    }

    @Test
    void authenticatedReaderCanGetUser() throws Exception {
        mockMvc.perform(get("/user/get/" + readerUser.getUserId())
                        .header("Authorization", "Bearer " + readerToken))
                .andExpect(status().isOk());
    }
}
