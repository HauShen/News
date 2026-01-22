package com.hau.news.responseBodies;


import com.hau.news.models.Article;
import com.hau.news.models.EmailNewsHeadline;
import com.hau.news.models.UserProfile;
import lombok.Data;


import java.time.Instant;
import java.util.List;
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
