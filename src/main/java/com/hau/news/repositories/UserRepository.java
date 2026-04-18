package com.hau.news.repositories;

import com.hau.news.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserProfile, String> {
    UserProfile findByUserId(String userId);
    Optional<UserProfile> findByEmail(String email);
    boolean existsByEmail(String email);
}
