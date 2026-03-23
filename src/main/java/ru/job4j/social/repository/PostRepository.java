package ru.job4j.social.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends CrudRepository<Post, Long> {

    List<Post> findByUser(Optional<User> user);

    List<Post> findByCreatedAtBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
