package com.hau.news;

import com.hau.news.models.roles.Role;
import com.hau.news.models.Article;
import com.hau.news.models.UserProfile;
import com.hau.news.repositories.ArticleRepository;
import com.hau.news.repositories.UserRepository;
import com.hau.news.requestbodies.ArticleRequestBody;
import com.hau.news.requestbodies.ArticleUpdatedRequestBody;
import com.hau.news.responsebodies.ArticleResponseBody;
import com.hau.news.responsebodies.ArticleUpdatedResponseBody;
import com.hau.news.serviceimpls.ArticleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    ArticleRepository fakeArticleRepository;
    @Mock
    UserRepository fakeUserRepository;
    @InjectMocks
    ArticleServiceImpl articlesService;
    private final UserProfile fakeUserProfile = new UserProfile("123","John",12, Role.READER);
    private final Article expectedArticle = new Article(123L,"Accident!","A car crashed into KFC",Instant.now(),Instant.now(),0,fakeUserProfile);
    private final Article expectedUpdatedArticle = new Article(123L,"Accident!","A bus crashed into KFC",Instant.now(),Instant.now(),0,fakeUserProfile);
    private final ArticleRequestBody fakeArticleRequestBody = new ArticleRequestBody("Accident!","A car crashed into KFC");
    private final ArticleUpdatedRequestBody fakeArticleUpdatedRequestBody = new ArticleUpdatedRequestBody("Accident!","A bus crashed into KFC");
    private final ArticleResponseBody expectedArticleResponseBody = new ArticleResponseBody(expectedArticle);
    private final ArticleUpdatedResponseBody expectedArticleUpdatedResponseBody = new ArticleUpdatedResponseBody(expectedUpdatedArticle);


    @Test
    public void createArticleShouldInsertArticleCorrectly(){
        Mockito.when(fakeUserRepository.findByUserId("123")).thenReturn(fakeUserProfile);
        Mockito.when(fakeArticleRepository.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArticleResponseBody actualArticleResponseBody = articlesService.createArticleByUserId("123",fakeArticleRequestBody);
        assertThat(actualArticleResponseBody)
                .usingRecursiveComparison()
                .ignoringFields("oid","createdAt","updatedAt")
                .isEqualTo(expectedArticleResponseBody);
    }

    @Test
    public void getArticleShouldReturnArticleByOid(){
        Mockito.when(fakeArticleRepository.findByOid(123L)).thenReturn(expectedArticle);
        ArticleResponseBody actualArticleResponseBody = articlesService.getArticleByOid(123L);
        assertThat(actualArticleResponseBody)
                .usingRecursiveComparison()
                .ignoringFields("createdAt","updatedAt")
                .isEqualTo(expectedArticleResponseBody);
    }
    @Test
    public void updateArticleShouldInsertArticleCorrectly(){
        Mockito.when(fakeArticleRepository.findByOid(123L)).thenReturn(expectedArticle);
        ArticleUpdatedResponseBody actualArticleUpdatedResponseBody = articlesService.editArticleByOid(123L,fakeArticleUpdatedRequestBody);
        assertThat(actualArticleUpdatedResponseBody)
                .usingRecursiveComparison()
                .ignoringFields("createdAt","updatedAt")
                .isEqualTo(expectedArticleUpdatedResponseBody);
    }
    @Test
    public void deleteArticleShouldInsertCorrectOid(){
        String expectedDeleteArticle = articlesService.deleteArticleByOid(123L);
        Mockito.verify(fakeArticleRepository,Mockito.times(1)).deleteById(123L);
        assertThat("This article with id " + 123L +" is deleted")
                .isEqualTo(expectedDeleteArticle);
    }
}
