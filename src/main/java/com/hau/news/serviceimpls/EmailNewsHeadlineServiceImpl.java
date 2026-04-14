package com.hau.news.serviceimpls;

import com.hau.news.externalapiproperties.NYTimesProperty;
import com.hau.news.models.Article;
import com.hau.news.models.NYTimesArticle;
import com.hau.news.models.exceptions.TodayArticleNoFoundException;
import com.hau.news.repositories.ArticleRepository;
import com.hau.news.repositories.EmailNewsHeadlineRepository;
import com.hau.news.repositories.FeedbackRepository;
import com.hau.news.responsebodies.NyTimesNewsResponseBody;
import com.hau.news.services.ArticleService;
import com.hau.news.services.EmailNewsHeadlineService;
import com.hau.news.services.NYTimesNewsClient;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Value;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


@Service
public class EmailNewsHeadlineServiceImpl implements EmailNewsHeadlineService {

    private static final Logger logger = LoggerFactory.getLogger(EmailNewsHeadlineServiceImpl.class);
    private static final String EMAIL_SUBJECT = "📰 Today Breaking News!";
    private static final String CHARSET = "UTF-8";

    // Externalized configuration
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${app.timezone:Asia/Kuala_Lumpur}")
    private String timezone;

    @Value("${app.email.from-display-name:News App}")
    private String emailFromName;

    // Dependencies
    private final EmailNewsHeadlineRepository emailNewsHeadlineRepository;
    private final ArticleRepository articleRepository;
    private final NYTimesNewsClient nyTimesNewsClient;
    private final NYTimesProperty nyTimesProperty;
    private final ArticleService articleService;
    private final FeedbackTokenServiceImpl tokenService;
    private final FeedbackRepository feedbackRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public EmailNewsHeadlineServiceImpl(
            EmailNewsHeadlineRepository emailNewsHeadlineRepository,
            ArticleRepository articleRepository,
            NYTimesNewsClient nyTimesNewsClient,
            NYTimesProperty nyTimesProperty,
            ArticleService articleService,
            FeedbackTokenServiceImpl tokenService,
            FeedbackRepository feedbackRepository) {
        this.emailNewsHeadlineRepository = emailNewsHeadlineRepository;
        this.articleRepository = articleRepository;
        this.nyTimesNewsClient = nyTimesNewsClient;
        this.nyTimesProperty = nyTimesProperty;
        this.articleService = articleService;
        this.tokenService = tokenService;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public void createTodayGmailHeadline(String emailAddress) throws MessagingException {
        try {
            logger.info("Creating today's headline for email: {}", emailAddress);

            ZoneId zoneId = ZoneId.of(timezone);
            LocalDate today = LocalDate.now(zoneId);

            Instant startOfDay = today.atStartOfDay(zoneId).toInstant();
            Instant endOfDay = today.plusDays(1).atStartOfDay(zoneId).toInstant();

            List<Article> todayArticles = articleRepository.findTodayUnsentArticles(startOfDay, endOfDay);

            if (todayArticles.isEmpty()) {
                logger.warn("No new articles found for today");
                throw new TodayArticleNoFoundException("No new articles created today.");
            }

            String htmlContent = buildArticleListHtml(todayArticles);
            sendHtmlEmail(emailAddress, EMAIL_SUBJECT, htmlContent);

            logger.info("Today's headline email sent successfully to: {}", emailAddress);
        } catch (MessagingException e) {
            logger.error("Failed to send today's headline email to: {}", emailAddress, e);
            throw e;
        }
    }

    @Override
    public void sendTodayNYTimesNews(String emailAddress) throws MessagingException {
        try {
            logger.info("Sending NY Times news to email: {}", emailAddress);

            NyTimesNewsResponseBody newsResponse = nyTimesNewsClient.getTopStories();

            if (newsResponse == null || newsResponse.getResults() == null || newsResponse.getResults().isEmpty()) {
                logger.warn("No NY Times news available");
                throw new RuntimeException("No news articles available from NY Times");
            }

            String htmlContent = buildNYTimesNewsHtml(newsResponse.getResults());
            sendHtmlEmail(emailAddress, EMAIL_SUBJECT, htmlContent);

            logger.info("NY Times news email sent successfully to: {}", emailAddress);
        } catch (MessagingException e) {
            logger.error("Failed to send NY Times news to: {}", emailAddress, e);
            throw e;
        }
    }

    @Override
    public void sendEmailWithFeedback(String emailAddress, String userId) throws MessagingException {
        try {
            logger.info("Sending newsletter with feedback link to email: {} for userId: {}", emailAddress, userId);

            // Generate article from NY Times
            Article article = articleService.turnsAndSavesNYTimesNewsToArticle(userId);

            // Generate secure token
            String token = tokenService.generateToken(userId, article.getOid());
            String feedbackLink = baseUrl + "/feedback/like?token=" + token;

            // Build HTML email with feedback button
            String htmlContent = buildFeedbackEmailHtml(article, feedbackLink);
            sendHtmlEmail(emailAddress, EMAIL_SUBJECT, htmlContent);

            logger.info("Newsletter with feedback sent successfully to: {} for article: {}", emailAddress, article.getOid());
        } catch (Exception e) {
            logger.error("Failed to send newsletter with feedback to: {}", emailAddress, e);
            throw new MessagingException("Failed to send feedback email", e);
        }
    }

    /**
     * Build HTML content for article list
     */
    private String buildArticleListHtml(List<Article> articles) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial, sans-serif;'>");
        html.append("<h2 style='color: #333;'>📰 Today's Top Articles</h2>");
        html.append("<hr>");

        for (Article article : articles) {
            html.append("<div style='margin: 20px 0; padding: 15px; border-left: 4px solid #007bff;'>");
            html.append("<h3 style='color: #007bff; margin-top: 0;'>")
                    .append(escapeHtml(article.getTitle()))
                    .append("</h3>");
            html.append("<p style='color: #666;'>")
                    .append(escapeHtml(article.getContent()))
                    .append("</p>");
            html.append("</div>");
        }

        html.append("</body></html>");
        return html.toString();
    }

    /**
     * Build HTML content for NY Times news
     */
    private String buildNYTimesNewsHtml(List<NYTimesArticle> articles) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial, sans-serif;'>");
        html.append("<h2 style='color: #333;'>📰 NY Times Top Stories</h2>");
        html.append("<hr>");

        for (NYTimesArticle article : articles) {
            html.append("<div style='margin: 20px 0; padding: 15px; border-left: 4px solid #d32f2f;'>");
            html.append("<h3 style='color: #d32f2f; margin-top: 0;'>")
                    .append(escapeHtml(article.getTitle()))
                    .append("</h3>");
            html.append("<p style='color: #666;'>")
                    .append(escapeHtml(article.getAbstractText()))
                    .append("</p>");
            html.append("<a href='").append(article.getUrl()).append("' style='color: #007bff; text-decoration: none;'>Read More →</a>");
            html.append("</div>");
        }

        html.append("</body></html>");
        return html.toString();
    }

    /**
     * Build HTML content with feedback button
     */
    private String buildFeedbackEmailHtml(Article article, String feedbackLink) {
        return "<html><body style='font-family: Arial, sans-serif; background-color: #f5f5f5;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: white; padding: 20px; border-radius: 8px;'>" +
                "<h2 style='color: #333;'>📰 Today's Exclusive Article</h2>" +
                "<hr style='border: none; border-top: 2px solid #007bff;'>" +
                "<h3 style='color: #007bff;'>" + escapeHtml(article.getTitle()) + "</h3>" +
                "<p style='color: #666; line-height: 1.6;'>" + escapeHtml(article.getContent()) + "</p>" +
                "<hr style='border: none; border-top: 1px solid #ddd;'>" +
                "<div style='text-align: center; margin: 30px 0;'>" +
                "<a href='" + feedbackLink + "' style='display: inline-block; background-color: #28a745; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; font-weight: bold;'>👍 Like This Article</a>" +
                "</div>" +
                "<p style='color: #999; font-size: 12px; text-align: center;'>This link expires in 24 hours</p>" +
                "<hr style='border: none; border-top: 1px solid #ddd;'>" +
                "<footer style='color: #999; font-size: 12px; text-align: center;'>" +
                "<p>© 2026 News App. All rights reserved.</p>" +
                "</footer>" +
                "</div>" +
                "</body></html>";
    }

    /**
     * Generic HTML email sender to reduce code duplication
     */
    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, CHARSET);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML content

            mailSender.send(mimeMessage);
            logger.info("Email sent successfully to: {}", toEmail);
        } catch (MessagingException e) {
            logger.error("Error sending email to {}: {}", toEmail, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Escape HTML special characters to prevent injection
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
