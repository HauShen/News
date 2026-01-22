package com.hau.news.repositories;

import com.hau.news.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long> {
     Article findByOid(Long oid);
     @Query("""
SELECT article FROM Article article WHERE article.createdAt >= :startOfDay AND article.createdAt < :endOfDay
 """ )

     List<Article> findTodayUnsentArticles(@Param("startOfDay") Instant startOfDay,@Param("endOfDay") Instant endOfDay);
}
