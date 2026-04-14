package com.hau.news.repositories;

import com.hau.news.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    boolean existsByUserIdAndArticleOid(String userId,Long articleOid);

    long countByArticleOidAndLiked(Long articleOid,boolean likedOrNot);
    @Query("SELECT f FROM Feedback f WHERE f.articleOid = :articleOid AND f.liked = true")
    java.util.List<Feedback> findLikesByArticle(@Param("articleOid") Long articleOid);

    /**
     * Get all likes by a specific user
     */
    @Query("SELECT f FROM Feedback f WHERE f.userId = :userId AND f.liked = true")
    java.util.List<Feedback> findLikesByUser(@Param("userId") String userId);

}
