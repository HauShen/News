package com.hau.news.controllers;

import com.hau.news.requestBodies.ArticleRequestBody;
import com.hau.news.requestBodies.ArticleUpdatedRequestBody;
import com.hau.news.responseBodies.ArticleResponseBody;
import com.hau.news.responseBodies.ArticleUpdatedResponseBody;
import com.hau.news.services.ArticleService;
import com.hau.news.services.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/create/{userId}")
    public ResponseEntity<ArticleResponseBody> createArticleByUserId(@PathVariable String userId,@RequestBody ArticleRequestBody articleRequestBody){
        ArticleResponseBody newArticle = articleService.createArticleByUserId(userId,articleRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).allow(HttpMethod.GET).body(newArticle);
    }
    @GetMapping("/get/{oid}")
    public ResponseEntity<ArticleResponseBody> getArticleByOid(@PathVariable Long oid){
        return ResponseEntity.ok(articleService.getArticleByOid(oid)) ;
    }
    @PutMapping("/edit/{oid}")
    public ResponseEntity<ArticleUpdatedResponseBody> editArticleByOid(@PathVariable Long oid, @RequestBody ArticleUpdatedRequestBody articleUpdatedRequestBody){
        ArticleUpdatedResponseBody updatedArticle = articleService.editArticleByOid(oid, articleUpdatedRequestBody);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/delete/{oid}")
    public String deleteArticleByOid(@PathVariable Long oid){
        return articleService.deleteArticleByOid(oid);

    }

}
