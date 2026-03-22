package ru.job4j.social;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.job4j.social.model.Follower;
import ru.job4j.social.model.Message;
import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;
import ru.job4j.social.repository.FollowerRepository;
import ru.job4j.social.repository.MessageRepository;
import ru.job4j.social.repository.PostRepository;
import ru.job4j.social.repository.UserRepository;

import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class SocialMediaApiApplication implements CommandLineRunner {

	private final UserRepository userRepository;

	private final PostRepository postRepository;

	private final FollowerRepository followerRepository;

	private final MessageRepository messageRepository;

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userRepository.deleteAll();
		postRepository.deleteAll();
		followerRepository.deleteAll();
		messageRepository.deleteAll();
		User admin =new User("Admin", "admin@mail.ru", "12345");
		User user = new User("User", "user@mail.ru", "12345");
		userRepository.save(admin);
		userRepository.save(user);
		System.out.println((List<User>)userRepository.findAll());

		Post post = new Post(user, "Test", "Lorem ipsum dolor sit amen", "photo.jpg");
		postRepository.save(post);

		Follower follower = new Follower(user, admin);
		followerRepository.save(follower);

		Message message = new Message(user, admin, "Let's be friends");
		messageRepository.save(message);
	}
}
