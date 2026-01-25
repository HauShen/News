package com.hau.news.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hau.news.models.roles.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @Column(name = "user_id")
    // @GeneratedValue(strategy = GenerationType.IDENTITY) // Use UUID in Service to create random userId.
    private String userId;
    private String name;
    private int age;
    @Enumerated(EnumType.STRING)
    private Role role; //NEWS_POSTER or READER

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    @JsonManagedReference
    List<Article> articles = new ArrayList<>();

  /*  @OneToOne(mappedBy = "emailCreator")
    private EmailNewsHeadline emailNewsHeadline;*/

    public UserProfile(String userId, String name, int age, Role role)
    {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.role = role;
    }

}
