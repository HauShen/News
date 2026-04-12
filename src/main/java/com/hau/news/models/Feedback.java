package com.hau.news.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "feedback")
public class Feedback {
    private Long oid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile reader;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oid", nullable = false)
    private Article article;
    private boolean liked;

}
