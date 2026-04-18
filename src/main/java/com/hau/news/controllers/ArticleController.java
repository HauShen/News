package com.hau.news.controllers;

import com.hau.news.repositories.ArticleRepository;
import com.hau.news.repositories.UserRepository;
import com.hau.news.requestbodies.ArticleRequestBody;
import com.hau.news.requestbodies.ArticleUpdatedRequestBody;
import com.hau.news.responsebodies.ArticleResponseBody;
import com.hau.news.responsebodies.ArticleUpdatedResponseBody;
import com.hau.news.services.ArticleService;
import com.hau.news.serviceimpls.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    public ArticleController(ArticleServiceImpl articlesService){
        this.articleService = articlesService;
    }

    @PreAuthorize("hasAnyRole('NEWS_POSTER', 'ADMIN')")
    @PostMapping("/create/{userId}")
    public ResponseEntity<ArticleResponseBody> createArticleByUserId(@PathVariable String userId,@RequestBody ArticleRequestBody articleRequestBody){
        ArticleResponseBody newArticle = articleService.createArticleByUserId(userId,articleRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).allow(HttpMethod.GET).body(newArticle);
    }
    @GetMapping("/get/{oid}")
    public ResponseEntity<ArticleResponseBody> getArticleByOid(@PathVariable Long oid){
        return ResponseEntity.ok(articleService.getArticleByOid(oid)) ;
    }
    @PreAuthorize("hasAnyRole('NEWS_POSTER', 'ADMIN')")
    @PutMapping("/edit/{oid}")
    public ResponseEntity<ArticleUpdatedResponseBody> editArticleByOid(@PathVariable Long oid, @RequestBody ArticleUpdatedRequestBody articleUpdatedRequestBody){
        ArticleUpdatedResponseBody updatedArticle = articleService.editArticleByOid(oid, articleUpdatedRequestBody);
        return ResponseEntity.ok(updatedArticle);
    }

    @PreAuthorize("hasAnyRole('NEWS_POSTER', 'ADMIN')")
    @DeleteMapping("/delete/{oid}")
    public String deleteArticleByOid(@PathVariable Long oid){
        return articleService.deleteArticleByOid(oid);

    }

}
