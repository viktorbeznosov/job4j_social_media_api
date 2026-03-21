-- Таблица для подписок
CREATE TABLE IF NOT EXISTS followers
(
    id INT PRIMARY KEY,
    follower_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    target_user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

   -- Уникальное ограничение: пользователь не может подписаться на одного и того же пользователя дважды
   CONSTRAINT unique_follower UNIQUE (follower_id, target_user_id),

   -- Проверка, что пользователь не может подписаться сам на себя
   CONSTRAINT check_not_self_follow CHECK (follower_id != target_user_id)
);