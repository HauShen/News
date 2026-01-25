package com.hau.news.responsebodies;

import com.hau.news.models.roles.Role;
import com.hau.news.models.UserProfile;
import lombok.Data;

@Data
public class UserUpdatedResponseBody {
    private String userId;
    private String name;
    private int age;
    private Role role;

    public UserUpdatedResponseBody(UserProfile userProfile){
        this.userId = userProfile.getUserId();
        this.name = userProfile.getName();
        this.age = userProfile.getAge();
        this.role = userProfile.getRole();

    }
}
