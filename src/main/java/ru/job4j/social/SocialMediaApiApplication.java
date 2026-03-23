package ru.job4j.social;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.job4j.social.model.Follower;
import ru.job4j.social.model.Message;
import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;
import ru.job4j.social.repository.FollowerRepository;
import ru.job4j.social.repository.MessageRepository;
import ru.job4j.social.repository.PostRepository;
import ru.job4j.social.repository.UserRepository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
		var user = userRepository.findById(8L);
		List<Post> userPosts = (List<Post>) postRepository.findByUser(user);
		System.out.println("Post filtered by user");
		for (Post post: userPosts) {
			System.out.println(post);
		}

		LocalDateTime dateFrom = LocalDateTime.of(2026, 01, 01, 0, 0,0);
		LocalDateTime dateTo = LocalDateTime.of(2026, 02, 20, 23, 59, 59);
		List<Post> dateDiapasonPosts = (List<Post>) postRepository.findByCreatedAtBetween(dateFrom, dateTo);

		System.out.println("Post filtered by date diapason");
		for (Post post: dateDiapasonPosts) {
			System.out.println(post);
		}

		List<Post> sortedByCreatedAtPosts = (List<Post>) postRepository.findAllByOrderByCreatedAtDesc(
			PageRequest.of(0, 10)
		);

		System.out.println("Post sored by date and paginated");
		for (Post post: sortedByCreatedAtPosts) {
			System.out.println(post);
		}
	}
}
