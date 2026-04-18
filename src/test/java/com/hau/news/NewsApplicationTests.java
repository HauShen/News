package com.hau.news;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class NewsApplicationTests {

    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }
}
