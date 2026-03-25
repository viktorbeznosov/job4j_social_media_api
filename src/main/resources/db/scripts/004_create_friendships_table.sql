-- Таблица для дружбы (взаимные подписки)
CREATE TABLE IF NOT EXISTS friendships
(
    id BIGSERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    friend_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Проверка, что пользователь не может дружить сам с собой
    CONSTRAINT check_not_self_friend CHECK (user_id != friend_id)
    );

-- Создаем уникальный индекс с упорядоченными полями
-- Это гарантирует, что (1,2) и (2,1) считаются одинаковыми
CREATE UNIQUE INDEX unique_friendship_pair
ON friendships (LEAST(user_id, friend_id), GREATEST(user_id, friend_id));