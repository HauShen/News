package com.hau.news.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@Table(name = "article")
public class Article {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "articleSequenceGenerator", sequenceName = "s_article")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "articleSequenceGenerator")
    private Long oid;
    private String title;
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(name = "created_at")
    Instant createdAt;
    @Column(name = "updated_at")
    Instant updatedAt;
    @Column(name = "like_count")
    int likeCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserProfile user;
  /*  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_news_headline",nullable = false)
    @JsonIgnore
    private EmailNewsHeadline emailNewsHeadline;*/

    public Article(Long oid, String title, String content, Instant createdAt, Instant updatedAt, int likeCount, UserProfile user){
        this.oid = oid;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likeCount = likeCount;
        this.user = user;
    }
}
