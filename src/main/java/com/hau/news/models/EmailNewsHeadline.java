package com.hau.news.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.List;
@Data
@Entity
@NoArgsConstructor
@Table(name = "email_news_headline")
public class EmailNewsHeadline {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "emailArticleHeadlineSequenceGenerator", sequenceName = "s_email_article_headline")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emailArticleHeadlineSequenceGenerator")
    private Long oid;
    private String subject;
    @Column(name = "send_at")
    private Instant sendAt;
    @Column(name = "send_to")
    private String sendTo;


  /*  @OneToOne
    @JoinColumn(name = "email_creator_id")
    private UserProfile emailCreator;*/
   /* @OneToMany(fetch = FetchType.LAZY,mappedBy = "emailArticleHeadline",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Article> todayArticles;*/

    public EmailNewsHeadline(Long oid, String subject, Instant sendAt, String sendTo/*UserProfile emailCreator*/){
        this.oid = oid;
        this.subject = subject;
        this.sendAt = sendAt;
        this.sendTo = sendTo;
       // this.emailCreator = emailCreator;
    }



}
