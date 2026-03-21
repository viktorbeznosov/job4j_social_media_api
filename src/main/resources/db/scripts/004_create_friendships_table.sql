-- Таблица для дружбы (взаимные подписки)
CREATE TABLE IF NOT EXISTS friendships
(
     id INT PRIMARY KEY,
     user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
     friend_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

     -- Уникальное ограничение: уникальная пара друзей
     CONSTRAINT unique_friendship UNIQUE (user_id, friend_id),

     -- Проверка, что пользователь не может дружить сам с собой
     CONSTRAINT check_not_self_friend CHECK (user_id != friend_id)
);