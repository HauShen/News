package com.hau.news.serviceimpls;

import com.hau.news.models.Article;
import com.hau.news.models.Feedback;
import com.hau.news.models.UserProfile;
import com.hau.news.models.exceptions.ExpiredTokenException;
import com.hau.news.models.exceptions.InvalidTokenException;
import com.hau.news.repositories.FeedbackRepository;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

@Service
public class FeedbackTokenServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackTokenServiceImpl.class);

    @Value("${app.feedback.secret-key:super-secret-key-change-in-production}")
    private String SECRET;

    @Value("${app.feedback.token-expiry-hours:24}")
    private int tokenExpiryHours;

    private final FeedbackRepository feedbackRepository;
    private UserProfile reader;
    private Article article;

    public FeedbackTokenServiceImpl(FeedbackRepository feedbackRepository,UserProfile reader,Article article) {
        this.feedbackRepository = feedbackRepository;
        this.reader = reader;
        this.article = article;
    }

    /**
     * Generates a secure token with userId, articleOid, and expiry
     */
    public String generateToken(String userId, Long articleOid) {
        try {
            long expiryMs = System.currentTimeMillis() + (1000L * 60 * 60 * tokenExpiryHours);
            String data = userId + ":" + articleOid + ":" + expiryMs;
            String signature = hmacSha256(data, SECRET);
            String token = data + ":" + signature;

            logger.info("Generated token for userId: {}, articleOid: {}", userId, articleOid);
            return Base64.getEncoder().encodeToString(token.getBytes());
        } catch (Exception e) {
            logger.error("Failed to generate token", e);
            throw new RuntimeException("Token generation failed", e);
        }
    }

    /**
     * Validates token and returns payload
     */
    public TokenPayload validateToken(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split(":");

            if (parts.length != 4) {
                throw new InvalidTokenException("Invalid token format");
            }

            String userId = parts[0];
            Long articleOid = Long.parseLong(parts[1]);
            long expiry = Long.parseLong(parts[2]);
            String signature = parts[3];

            // Verify signature
            String data = userId + ":" + articleOid + ":" + expiry;
            String expectedSignature = hmacSha256(data, SECRET);

            if (!expectedSignature.equals(signature)) {
                logger.warn("Token tampered for userId: {}", userId);
                throw new InvalidTokenException("Token has been tampered with");
            }

            // Check expiry
            if (System.currentTimeMillis() > expiry) {
                logger.warn("Token expired for userId: {}", userId);
                throw new ExpiredTokenException("Token has expired");
            }

            logger.info("Token validated successfully for userId: {}, articleOid: {}", userId, articleOid);
            return new TokenPayload(userId, articleOid);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to decode token", e);
            throw new InvalidTokenException("Invalid token encoding", e);
        }
    }

    /**
     * Saves a like for user and article
     */
    public void saveLike(String userId, Long articleOid) {
        if (feedbackRepository.existsByUserIdAndArticleOid(userId, articleOid)) {
            logger.info("Like already exists for userId: {}, articleOid: {}", userId, articleOid);
            return;
        }

        Feedback feedback = new Feedback(reader, article, true);
        feedbackRepository.save(feedback);
        logger.info("Like saved for userId: {}, articleOid: {}", userId, articleOid);
    }

    /**
     * HMAC-SHA256 signature generation
     */
    private String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            logger.error("Failed to generate HMAC", e);
            throw new RuntimeException("HMAC generation failed", e);
        }
    }

    public record TokenPayload(String userId, Long articleOid) {}
    /**
     * Check if user already liked this article
     */
    public boolean hasUserLikedArticle(String userId, Long articleOid) {
        return feedbackRepository.existsByUserIdAndArticleOid(userId, articleOid);
    }

    /**
     * Get like count for article
     */
    public long getArticleLikeCount(Long articleOid) {
        return feedbackRepository.countByArticleOidAndLiked(articleOid, true);
    }

    /**
     * Save like with more details
     */
    public void saveLikeWithToken(String userId, Long articleOid, String token) {
        if (feedbackRepository.existsByUserIdAndArticleOid(userId, articleOid)) {
            logger.info("User {} already liked article {}", userId, articleOid);
            return;
        }

        Feedback feedback = new Feedback(reader, article, true);
        feedback.setUserToken(token);
        feedbackRepository.save(feedback);
        logger.info("Like saved with token for userId: {}, articleOid: {}", userId, articleOid);
    }
}
