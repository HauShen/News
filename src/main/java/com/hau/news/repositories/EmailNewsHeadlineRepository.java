package com.hau.news.repositories;

import com.hau.news.models.EmailNewsHeadline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailNewsHeadlineRepository extends JpaRepository<EmailNewsHeadline,Long> {
}
