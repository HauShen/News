CREATE TABLE IF NOT EXISTS feedback (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    article_oid BIGINT NOT NULL,
    user_token VARCHAR(500),
    liked BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_feedback_user
        FOREIGN KEY (user_id)
        REFERENCES user_profile(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_feedback_article
        FOREIGN KEY (article_oid)
        REFERENCES article(oid)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    -- Ensure each user can like each article only once
    CONSTRAINT uk_user_article_like UNIQUE(user_id, article_oid)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_feedback_user_id
    ON feedback(user_id);

CREATE INDEX IF NOT EXISTS idx_feedback_article_oid
    ON feedback(article_oid);

CREATE INDEX IF NOT EXISTS idx_feedback_user_article
    ON feedback(user_id, article_oid);

CREATE INDEX IF NOT EXISTS idx_feedback_created_at
    ON feedback(created_at);

-- Add comments to table
COMMENT ON TABLE feedback IS 'Stores article likes/feedback from users';
COMMENT ON COLUMN feedback.user_id IS 'ID of the user who liked the article (FK to user_profile)';
COMMENT ON COLUMN feedback.article_oid IS 'ID of the article that was liked (FK to article)';
COMMENT ON COLUMN feedback.user_token IS 'Token used to verify the like action';
COMMENT ON COLUMN feedback.liked IS 'Whether the user liked (true) or disliked (false)';