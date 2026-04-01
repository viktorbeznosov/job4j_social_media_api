package ru.job4j.social.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.social.model.Message;
import ru.job4j.social.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageRepositoryTest {
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
    public void whenCreateMessageThenSuccess() {
        User sender = new User("Sender", "sender", "sender@mail.ru", "12345");
        User receiver = new User("Receiver", "receiver",  "receiver@mail.ru", "12345");

        userRepository.save(sender);
        userRepository.save(receiver);

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setText("Hello, this is a test message!");

        messageRepository.save(message);

        var foundMessage = messageRepository.findById(message.getId());

        assertThat(foundMessage).isPresent();
        assertThat(foundMessage.get().getSender().getId()).isEqualTo(sender.getId());
        assertThat(foundMessage.get().getReceiver().getId()).isEqualTo(receiver.getId());
        assertThat(foundMessage.get().getText()).isEqualTo("Hello, this is a test message!");
        assertThat(foundMessage.get().getCreatedAt()).isNotNull();
        assertThat(foundMessage.get().isRead()).isFalse();
    }

    @Test
    public void whenSendMessageToYourselfThenException() {
        User sameUser = new User("Same User",  "sameUser", "same@mail.ru", "12345");
        userRepository.save(sameUser);

        Message message = new Message();
        message.setSender(sameUser);
        message.setReceiver(sameUser);
        message.setText("Message to myself");

        InvalidDataAccessApiUsageException exception = assertThrows(
            InvalidDataAccessApiUsageException.class,
            () -> messageRepository.save(message)
        );

        assertThat(exception.getMessage()).isEqualTo("Нельзя отправить сообщение самому себе");
        assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
        assertThat(messageRepository.findAll()).isEmpty();
    }
}