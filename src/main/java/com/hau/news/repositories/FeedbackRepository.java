package com.hau.news.repositories;

import com.hau.news.models.Article;
import com.hau.news.models.Feedback;
import com.hau.news.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    /**
     * Check if a user already liked an article
     */
    boolean existsByUserAndArticle(UserProfile user, Article article);

    /**
     * Count total likes for an article
     */
    long countByArticleAndLiked(Article article, boolean liked);

    /**
     * Get all likes for a specific article
     */
    List<Feedback> findByArticleAndLiked(Article article, boolean liked);

    /**
     * Get all likes by a specific user
     */
    List<Feedback> findByUserAndLiked(UserProfile user, boolean liked);

    /**
     * Find specific like by user and article
     */
    Optional<Feedback> findByUserAndArticle(UserProfile user, Article article);
}
