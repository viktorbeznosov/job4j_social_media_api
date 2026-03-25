package ru.job4j.social.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostRepositoryTest {
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
    public void whenCreatePost() {
        User user = new User();
        user.setFullName("Test user");
        user.setEmail("test@mail.ru");
        user.setPassword("12345");
        userRepository.save(user);

        Post post = new Post();
        post.setUser(user);
        post.setTitle("Test post");
        post.setText("Test post text");
        postRepository.save(post);

        var foundPost = postRepository.findById(post.getId());

        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getId()).isEqualTo(post.getId());
        assertThat(foundPost.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void whenCreateManyPosts() {
        User user = new User();
        user.setFullName("Test user");
        user.setEmail("test@mail.ru");
        user.setPassword("12345");
        userRepository.save(user);

        Post post = new Post();
        post.setUser(user);
        post.setTitle("Test post");
        post.setText("Test post text");
        postRepository.save(post);

        Post post2 = new Post();
        post2.setUser(user);
        post2.setTitle("Test post 2");
        post2.setText("Test post text 2");
        postRepository.save(post2);

        List<Post> posts = (List<Post>) postRepository.findAll();

        assertThat(posts).hasSize(2);
        assertThat(posts).extracting(Post::getTitle).contains("Test post");
    }
}