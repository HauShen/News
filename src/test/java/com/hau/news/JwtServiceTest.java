package com.hau.news;

import com.hau.news.config.JwtService;
import com.hau.news.models.UserProfile;
import com.hau.news.models.roles.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey",
                "dGVzdC1zZWNyZXQta2V5LWZvci1uZXdzLWFwcC1qd3Qtc2lnbmluZy1taW4tMzItY2hhcnM=");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 86400000L);
    }

    private UserProfile createTestUser() {
        UserProfile user = new UserProfile("test-id", "Test User", 25, Role.READER);
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");
        return user;
    }

    @Test
    void shouldGenerateToken() {
        UserProfile user = createTestUser();
        String token = jwtService.generateToken(user);
        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    void shouldExtractUsernameFromToken() {
        UserProfile user = createTestUser();
        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);
        assertThat(username).isEqualTo("test@example.com");
    }

    @Test
    void shouldValidateCorrectToken() {
        UserProfile user = createTestUser();
        String token = jwtService.generateToken(user);
        boolean isValid = jwtService.isTokenValid(token, user);
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldRejectTokenForDifferentUser() {
        UserProfile user1 = createTestUser();
        UserProfile user2 = new UserProfile("other-id", "Other User", 30, Role.NEWS_POSTER);
        user2.setEmail("other@example.com");
        user2.setPassword("other-password");

        String token = jwtService.generateToken(user1);
        boolean isValid = jwtService.isTokenValid(token, user2);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldRejectExpiredToken() {
        JwtService shortLivedJwtService = new JwtService();
        ReflectionTestUtils.setField(shortLivedJwtService, "secretKey",
                "dGVzdC1zZWNyZXQta2V5LWZvci1uZXdzLWFwcC1qd3Qtc2lnbmluZy1taW4tMzItY2hhcnM=");
        ReflectionTestUtils.setField(shortLivedJwtService, "jwtExpirationMs", -1000L);

        UserProfile user = createTestUser();
        String token = shortLivedJwtService.generateToken(user);

        try {
            boolean isValid = shortLivedJwtService.isTokenValid(token, user);
            assertThat(isValid).isFalse();
        } catch (Exception e) {
            assertThat(e).isNotNull();
        }
    }
}
