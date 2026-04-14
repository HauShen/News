package com.hau.news.serviceimpls;

import com.hau.news.models.Feedback;
import com.hau.news.repositories.FeedbackRepository;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class FeedbackTokenServiceImpl {

    private final String SECRET = "super-secret-key";
    private final FeedbackRepository feedbackRepository;
    public FeedbackTokenServiceImpl(FeedbackRepository feedbackRepository){
        this.feedbackRepository = feedbackRepository;
    }

    public String generateToken(String userId, Long articleOid) {
        long expiry = System.currentTimeMillis() + 1000 * 60 * 60 * 24; // 24h

        String data = userId + ":" + articleOid + ":" + expiry;
        String signature = hmacSha256(data, SECRET);

        String token = data + ":" + signature;
        return Base64.getEncoder().encodeToString(token.getBytes());
    }

    public TokenPayload validateToken(String token) {
        String decoded = new String(Base64.getDecoder().decode(token));

        String[] parts = decoded.split(":");

        String userId = parts[0];
        Long articleOid = Long.parseLong(parts[1]);
        long expiry = Long.parseLong(parts[2]);
        String signature = parts[3];

        String data = userId + ":" + articleOid + ":" + expiry;
        String expectedSignature = hmacSha256(data, SECRET);

        if (!expectedSignature.equals(signature)) {
            throw new RuntimeException("Invalid token (tampered)");
        }

        if (System.currentTimeMillis() > expiry) {
            throw new RuntimeException("Token expired");
        }

        return new TokenPayload(userId, articleOid);
    }
    public record TokenPayload(String userId, Long articleOid) {}

    private String hmacSha256(String data, String secret) {
        try {
            // 1. Create HMAC-SHA256 instance
            Mac mac = Mac.getInstance("HmacSHA256");

            // 2. Convert secret into key
            SecretKeySpec keySpec = new SecretKeySpec(
                    secret.getBytes(),
                    "HmacSHA256"
            );

            // 3. Initialize Mac with key
            mac.init(keySpec);

            // 4. Generate raw HMAC bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());

            // 5. Convert to Base64 (or Hex)
            return Base64.getEncoder().encodeToString(rawHmac);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC", e);
        }
    }
    public void saveLike(String userId, Long articleOid) {
        if (feedbackRepository.existsByUserIdAndArticleOid(userId, articleOid)) {
            return;
        }

        feedbackRepository.save(new Feedback(userId, articleOid, true));
    }




}
