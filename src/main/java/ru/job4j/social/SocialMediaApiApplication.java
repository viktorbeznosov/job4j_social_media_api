package ru.job4j.social;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.job4j.social.model.Friendship;
import ru.job4j.social.model.User;
import ru.job4j.social.repository.*;
import ru.job4j.social.service.NotifyService;
import ru.job4j.social.service.PostService;
import ru.job4j.social.service.SubscribeService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
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

	@Bean("customExecutor")
	public ThreadPoolTaskExecutor initCustomExecutor() {
		var pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(1);
		pool.setMaxPoolSize(5);
		pool.setQueueCapacity(10);
		return pool;
	}

	@Bean("notificationPool")
	public ThreadPoolTaskExecutor initNotificationPool() {
		var pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(1);
		return pool;
	}

	@Bean("reportPool")
	public ThreadPoolTaskExecutor initReportPool() {
		var pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(1);
		return pool;
	}

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("==========================================================================================");
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			var service = ctx.getBean(NotifyService.class);
			service.asyncOperation();
			service.asyncOperation();
			service.asyncOperationByReportPool();
			service.asyncOperationByReportPool();
			var reports = service.report(-1L);
			System.out.println("Wait 1 second.");
			reports.get().forEach(System.out::println);
		};
	}
}
