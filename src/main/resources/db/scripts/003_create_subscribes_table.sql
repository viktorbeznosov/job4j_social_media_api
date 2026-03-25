-- Таблица для подписок
CREATE TABLE IF NOT EXISTS subscribes
(
    id BIGSERIAL PRIMARY KEY,
    follower_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    target_user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Проверка, что пользователь не может подписаться сам на себя
    CONSTRAINT check_not_self_follow CHECK (follower_id != target_user_id)
);

-- Создаем уникальный индекс с упорядоченными полями
-- Это гарантирует, что (1,2) и (2,1) считаются одинаковыми
CREATE UNIQUE INDEX unique_subscribe_pair
ON subscribes (LEAST(follower_id, target_user_id), GREATEST(follower_id, target_user_id));