package com.hau.news.services;

import com.hau.news.models.Article;
import com.hau.news.models.UserProfile;
import com.hau.news.models.exceptions.ArticleNotFoundException;
import com.hau.news.models.exceptions.UserNotFoundException;
import com.hau.news.repositories.ArticleRepository;
import com.hau.news.repositories.UserRepository;
import com.hau.news.requestBodies.ArticleRequestBody;
import com.hau.news.requestBodies.ArticleUpdatedRequestBody;
import com.hau.news.responseBodies.ArticleResponseBody;
import com.hau.news.responseBodies.ArticleUpdatedResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;

    private UserRepository userRepository;
    @Autowired
    public ArticleServiceImpl(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }
    private static final Logger logger =
            LoggerFactory.getLogger(ArticleServiceImpl.class);



    @Override
    public ArticleResponseBody createArticleByUserId(String userId, ArticleRequestBody articleRequestBody){
        UserProfile currentUser = userRepository.findByUserId(userId);
        if(currentUser == null){
            logger.warn("User with userId={} not found",userId);
            throw new UserNotFoundException( "User with id " + userId + " not found");
        }
        Article newArticle = new Article();
        newArticle.setUser(currentUser);
        newArticle.setTitle(articleRequestBody.getTitle());
        newArticle.setContent(articleRequestBody.getContent());
        newArticle.setCreatedAt(Instant.now());
        logger.info("Article created successfully");
        articleRepository.save(newArticle);
        return new ArticleResponseBody(newArticle);
    }
    @Override
    public ArticleResponseBody getArticleByOid(Long oid){
        Article article = articleRepository.findByOid(oid);
        if(article == null){
            logger.warn("Article with articleId={} not found",oid);
            throw new ArticleNotFoundException("User with id " + oid + " not found");
        }
        return new ArticleResponseBody(article);
    }
    @Override
    public ArticleUpdatedResponseBody editArticleByOid(Long oid, ArticleUpdatedRequestBody articleUpdatedRequestBody){
        Article currentArticle = articleRepository.findByOid(oid);
        if(currentArticle == null){
            logger.warn("Article with articleId={} not found",oid);
            throw new ArticleNotFoundException("User with id " + oid + " not found");
        }
        currentArticle.setTitle(articleUpdatedRequestBody.getTitle());
        currentArticle.setContent(articleUpdatedRequestBody.getContent());
        currentArticle.setUpdatedAt(Instant.now());
        articleRepository.save(currentArticle);
        return new ArticleUpdatedResponseBody(currentArticle);
    }
    @Override
    public String deleteArticleByOid(Long oid){
         articleRepository.deleteById(oid);
         return "This article with id " + oid +" is deleted";
    }

}
