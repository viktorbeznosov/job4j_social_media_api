CREATE TABLE IF NOT EXISTS posts
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title        VARCHAR(255) NOT NULL,
    text         TEXT,
    photo        VARCHAR(500),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP
    );