package com.hau.news;

import com.hau.news.models.NYTimesArticle;
import com.hau.news.services.NewsService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public NewsService mockNewsService() {
        return new NewsService() {
            @Override
            public List<NYTimesArticle> getTopStories() {
                NYTimesArticle article = new NYTimesArticle();
                article.setTitle("Test Story");
                article.setAbstractText("Test abstract");
                article.setUrl("https://example.com");
                return List.of(article);
            }

            @Override
            public List<NYTimesArticle> getScienceTopStories() {
                return List.of();
            }
        };
    }
}
