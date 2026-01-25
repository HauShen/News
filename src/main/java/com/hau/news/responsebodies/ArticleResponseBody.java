package com.hau.news.responsebodies;

import com.hau.news.models.Article;
import lombok.Data;

import java.time.Instant;

@Data
public class ArticleResponseBody {
    private Long oid;
    private String title;
    private String content;
    private Instant createdAt ;
    private Instant updatedAt;
    private int likeCount;
    private String userId;
    public ArticleResponseBody(Article article){
        this.oid = article.getOid();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.likeCount = article.getLikeCount();
        this.userId = article.getUser().getUserId();
    }
}
