package com.hau.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hau.news.models.UserProfile;
import com.hau.news.models.roles.Role;
import com.hau.news.repositories.UserRepository;
import com.hau.news.requestbodies.LoginRequest;
import com.hau.news.requestbodies.SignupRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void signupShouldCreateUserAndReturnToken() throws Exception {
        SignupRequest request = new SignupRequest("John Doe", "john@example.com", "password123", 25, Role.READER);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.userId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("READER"));
    }

    @Test
    void signupShouldRejectDuplicateEmail() throws Exception {
        UserProfile existing = new UserProfile();
        existing.setUserId(UUID.randomUUID().toString());
        existing.setName("Existing User");
        existing.setEmail("john@example.com");
        existing.setPassword(passwordEncoder.encode("password123"));
        existing.setAge(25);
        existing.setRole(Role.READER);
        userRepository.save(existing);

        SignupRequest request = new SignupRequest("John Doe", "john@example.com", "password456", 30, Role.READER);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email already registered"));
    }

    @Test
    void loginShouldReturnTokenForValidCredentials() throws Exception {
        UserProfile user = new UserProfile();
        user.setUserId(UUID.randomUUID().toString());
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setAge(25);
        user.setRole(Role.READER);
        userRepository.save(user);

        LoginRequest request = new LoginRequest("john@example.com", "password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("READER"));
    }

    @Test
    void loginShouldRejectInvalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest("nonexistent@example.com", "wrongpassword");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
