CREATE TABLE IF NOT EXISTS feedback (
    id BIGSERIAL PRIMARY KEY,
    reader_id VARCHAR(255) NOT NULL,  -- ✅ Change to reader_id
    article_oid BIGINT NOT NULL,
    user_token VARCHAR(500),
    liked BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_feedback_reader
        FOREIGN KEY (reader_id)
        REFERENCES user_profile(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_feedback_article
        FOREIGN KEY (article_oid)
        REFERENCES article(oid)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    -- Ensure each user can like each article only once
    CONSTRAINT uk_user_article_like UNIQUE(reader_id, article_oid)
);

CREATE INDEX IF NOT EXISTS idx_feedback_reader_id ON feedback(reader_id);
CREATE INDEX IF NOT EXISTS idx_feedback_article_oid ON feedback(article_oid);
CREATE INDEX IF NOT EXISTS idx_feedback_reader_article ON feedback(reader_id, article_oid);
CREATE INDEX IF NOT EXISTS idx_feedback_created_at ON feedback(created_at);