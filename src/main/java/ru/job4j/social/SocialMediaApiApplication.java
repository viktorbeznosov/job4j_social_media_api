package ru.job4j.social;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.job4j.social.model.Friendship;
import ru.job4j.social.model.User;
import ru.job4j.social.repository.*;
import ru.job4j.social.service.NotifyService;
import ru.job4j.social.service.PostService;
import ru.job4j.social.service.SubscribeService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@AllArgsConstructor
public class SocialMediaApiApplication implements CommandLineRunner {

	private final UserRepository userRepository;

	private final PostRepository postRepository;

	private final SubscribeRepository subscribeRepository;

	private final FriendshipRepository friendshipRepository;

	private final MessageRepository messageRepository;

	private final PostService postService;

	private final SubscribeService subscribeService;

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("========================================== START ==========================================");
	}
}
