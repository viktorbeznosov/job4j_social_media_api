CREATE TABLE IF NOT EXISTS posts
(
    id           INT PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    text         TEXT,
    photo        VARCHAR(500),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP
    );