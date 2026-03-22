-- Таблица для хранения сообщений между пользователями
CREATE TABLE IF NOT EXISTS messages (
    id BIGSERIAL PRIMARY KEY,
    sender_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    receiver_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    text TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT check_not_self_message CHECK (sender_id != receiver_id)
);