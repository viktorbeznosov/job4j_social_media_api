package ru.job4j.social.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.social.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

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
    public void whenSaveUserThenFindById() {
        User user = new User();
        user.setFullName("User");
        user.setUsername("user");
        user.setEmail("user@mail.ru");
        user.setPassword("12345");
        userRepository.save(user);
        var foundUser = userRepository.findById(user.getId());
        assertThat(foundUser.isPresent());
        assertThat(foundUser.get().getId()).isEqualTo(user.getId());
    }

    @Test
    public void whenFindAllUsers() {
        User user1 = new User();
        user1.setFullName("Jane");
        user1.setUsername("jane");
        user1.setEmail("jane@mail.ru");
        user1.setPassword("12345");
        User user2 = new User();
        user2.setFullName("John");
        user2.setUsername("john");
        user2.setEmail("john@mail.ru");
        user2.setPassword("12345");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = (List<User>) userRepository.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getFullName).contains(user1.getFullName());
    }
}