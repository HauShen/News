package com.hau.news.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "articleOid"})})
public class Feedback {

    @Id
    @GeneratedValue
    private Long id;
    private String userId;
    private Long articleOid;
    private boolean liked;

    public Feedback(String userId,Long articleOid, boolean liked){
        this.userId = userId;
        this.articleOid = articleOid;
        this.liked = liked;
    }
}
