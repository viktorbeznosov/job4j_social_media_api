package ru.job4j.social.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.social.model.Friendship;
import ru.job4j.social.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendshipRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private PostRepository postRepository;


    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    public void setUp() {
        subscribeRepository.deleteAll();
        friendshipRepository.deleteAll();
        messageRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenCreateFriendship() {
        // Создаем двух пользователей
        User user1 = new User("User1", "user1", "user1@mail.ru", "12345");
        User user2 = new User("User2", "user2", "user2@mail.ru", "12345");

        userRepository.save(user1);
        userRepository.save(user2);

        // Создаем дружбу
        Friendship friendship = new Friendship(user1, user2);
        friendshipRepository.save(friendship);

        // Проверяем, что дружба сохранилась
        var foundFriendship = friendshipRepository.findById(friendship.getId());

        assertThat(foundFriendship).isPresent();
        assertThat(foundFriendship.get().getUser().getId()).isEqualTo(user1.getId());
        assertThat(foundFriendship.get().getFriend().getId()).isEqualTo(user2.getId());
    }

    @Test
    public void whenCreateDuplicateFriendshipThenException() {
        User user1 = new User("User1",  "user1", "user1@mail.ru", "12345");
        User user2 = new User("User2", "user2", "user2@mail.ru", "12345");

        userRepository.save(user1);
        userRepository.save(user2);

        Friendship friendship1 = new Friendship(user1, user2);
        friendshipRepository.save(friendship1);

        Friendship friendship2 = new Friendship(user1, user2);

        assertThrows(
            DataIntegrityViolationException.class,
            () -> friendshipRepository.save(friendship2)
        );
    }

    @Test
    public void whenFriendshipWithSameUserThenException() {
        User sameUser = new User("Same User", "sameUser", "same@mail.ru", "12345");
        userRepository.save(sameUser);

        Friendship friendship = new Friendship(sameUser, sameUser);

        assertThrows(
            InvalidDataAccessApiUsageException.class,
            () -> friendshipRepository.save(friendship)
        );
    }
}