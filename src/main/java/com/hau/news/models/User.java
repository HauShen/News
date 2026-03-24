package com.hau.news.models;

import com.hau.news.models.roles.Role;
import lombok.Data;

import java.util.List;
@Data
public class User {
    private final String userId;
    private final String userName;
    private final String password;
    private final int age;
    private final Role role;
    private List<Article> articles;
    public User(String userId, String userName, String password, int age, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.role = role;

    }


}
