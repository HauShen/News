package com.hau.news.repositories;

import com.hau.news.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserProfile,String> {
        UserProfile findByUserId(String userId);
}
