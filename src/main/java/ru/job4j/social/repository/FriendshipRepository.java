package ru.job4j.social.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.social.model.Friendship;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {
}
