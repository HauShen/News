package com.hau.news.responsebodies;


import lombok.Data;


import java.time.Instant;

@Data
public class EmailNewsHeadlineResponseBody {
    private Long oid;
    private String subject;
    private Instant sendAt;
    private String sendTo;
    private String emailCreatorId;

   /* public EmailNewsHeadlineResponseBody(EmailNewsHeadline emailNewsHeadline){
        this.oid = emailNewsHeadline.getOid();
        this.subject = emailNewsHeadline.getSubject();
        this.sendAt = emailNewsHeadline.getSendAt();
        this.sendTo = emailNewsHeadline.getSendTo();
        this.emailCreatorId = emailNewsHeadline.getEmailCreator().getUserId();
    }*/
}
