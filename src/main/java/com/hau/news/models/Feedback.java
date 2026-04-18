package com.hau.news.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@Table(
        name = "feedback",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_article_like",
                        columnNames = {"reader_id", "article_oid"}
                )
        }
)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign Key to UserProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "reader_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_feedback_reader")
    )
    private UserProfile user;

    // Foreign Key to Article
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "article_oid",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_feedback_article")
    )
    private Article article;

    @Column(name = "user_token")
    private String userToken;

    @Column(nullable = false)
    private boolean liked;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // Constructors
    public Feedback(UserProfile user, Article article, boolean liked) {
        this.user = user;
        this.article = article;
        this.liked = liked;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
