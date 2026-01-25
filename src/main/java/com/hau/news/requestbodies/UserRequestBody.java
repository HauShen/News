package com.hau.news.requestbodies;

import com.hau.news.models.roles.Role;
import lombok.Data;

@Data
public class UserRequestBody {
    private String name;
    private int age;
    private Role role;

    public UserRequestBody(String name, int age, Role role){
        this.name = name;
        this.age = age;
        this.role = role;
    }
}
