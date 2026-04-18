CREATE TABLE IF NOT EXISTS article (
    oid BIGINT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    like_count INTEGER,
    user_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_article_user
        FOREIGN KEY (user_id)
        REFERENCES user_profile(user_id)
);

CREATE SEQUENCE IF NOT EXISTS s_article
START WITH 1
INCREMENT BY 1
CACHE 50
NO CYCLE;

ALTER SEQUENCE s_article OWNED BY article.oid;