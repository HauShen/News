package com.hau.news.responsebodies;

import com.hau.news.models.Article;
import lombok.Data;

import java.time.Instant;

@Data
public class ArticleUpdatedResponseBody {
    private Long oId;
    private String title;
    private String content;
    private Instant createdAt ;
    private Instant updatedAt;
    private int likeCount;
    private String userId;
    public ArticleUpdatedResponseBody(Long oid , String title, String content,Instant createAt, Instant updatedAt, int likeCount, String userId){
        this.oId = oid;
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
        this.likeCount = likeCount;
        this.userId = userId;

    }

    public ArticleUpdatedResponseBody(Article article){
        this.oId = article.getOid();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.likeCount = article.getLikeCount();
        this.userId = article.getUser().getUserId();
    }


}
