package ru.job4j.social.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.social.model.Follower;
import ru.job4j.social.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FollowerRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private PostRepository postRepository;


    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    public void setUp() {
        followerRepository.deleteAll();
        friendshipRepository.deleteAll();
        messageRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenCreateFollower() {
        User targetUser = new User("Test user", "user@mail.ru",  "12345");
        User followerUser = new User("Follower", "follower@mail.ru", "12345");
        Follower follower = new Follower(followerUser, targetUser);
        userRepository.save(targetUser);
        userRepository.save(followerUser);
        followerRepository.save(follower);

        var foundFollower = followerRepository.findById(follower.getId());

        assertThat(foundFollower).isPresent();
        assertThat(foundFollower.get().getFollower().getId()).isEqualTo(followerUser.getId());
        assertThat(foundFollower.get().getStatus()).isEqualTo(Follower.FollowStatus.pending);
        assertThat(foundFollower.get().getTargetUser().getId()).isEqualTo(targetUser.getId());
    }

    @Test
    public void whenFollowerTheSameTarget() {
        User sameUser = new User("Same User", "same@mail.ru", "12345");
        userRepository.save(sameUser);

        Follower follower = new Follower(sameUser, sameUser);

        // Ожидаем InvalidDataAccessApiUsageException, который оборачивает IllegalArgumentException
        InvalidDataAccessApiUsageException exception = assertThrows(
                InvalidDataAccessApiUsageException.class,
                () -> followerRepository.save(follower)
        );

        // Проверяем сообщение исключения
        assertThat(exception.getMessage()).isEqualTo("Нельзя подписаться на самого себя");

        // Или проверяем cause
        assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception.getCause().getMessage()).isEqualTo("Нельзя подписаться на самого себя");
    }
}