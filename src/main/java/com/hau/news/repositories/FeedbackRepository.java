package com.hau.news.repositories;

import com.hau.news.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    boolean existsByUserIdAndArticleOid(String userId,Long articleOid);
}
