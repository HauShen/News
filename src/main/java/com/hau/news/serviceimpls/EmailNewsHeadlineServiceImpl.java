package com.hau.news.serviceimpls;

import com.hau.news.models.Article;
import com.hau.news.models.exceptions.TodayArticleNoFoundException;
import com.hau.news.repositories.ArticleRepository;
import com.hau.news.repositories.EmailNewsHeadlineRepository;
import com.hau.news.services.EmailNewsHeadlineService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


@Service
public class EmailNewsHeadlineServiceImpl implements EmailNewsHeadlineService {
    private EmailNewsHeadlineRepository emailNewsHeadlineRepository;
    private ArticleRepository articleRepository;
    @Autowired
    public EmailNewsHeadlineServiceImpl(EmailNewsHeadlineRepository emailNewsHeadlineRepository,ArticleRepository articleRepository){
        this.emailNewsHeadlineRepository = emailNewsHeadlineRepository;
        this.articleRepository = articleRepository;
    }
    @Autowired
    private JavaMailSender mailSender;
    private static final Logger logger =
            LoggerFactory.getLogger(EmailNewsHeadlineServiceImpl.class);
    @Override
    public void createTodayGmailHeadline (String emailAddress)throws MessagingException{

        ZoneId zoneId = ZoneId.of("Asia/Kuala_Lumpur");
        LocalDate today = LocalDate.now(zoneId);

        Instant startOfDay = today.atStartOfDay(zoneId).toInstant();
        Instant endOfDay = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        List<Article> todayArticles = articleRepository.findTodayUnsentArticles(startOfDay,endOfDay);
        if(todayArticles.isEmpty()){
            logger.warn("No new article found.");
            throw new TodayArticleNoFoundException("No new article has created today.");
        }
        String emailContext = "";
        for(Article article : todayArticles){
            emailContext = emailContext + article.getTitle() + "\n";
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setTo(emailAddress);
            mimeMessageHelper.setSubject("Today breaking news!!");
            mimeMessageHelper.setText(emailContext);

            ClassPathResource image = new ClassPathResource("attachments/ElSb6-VVgAA_DBH.jfif");
            mimeMessageHelper.addAttachment("ElSb6-VVgAA_DBH.jfif",image);

        mailSender.send(mimeMessage);

    }
}
