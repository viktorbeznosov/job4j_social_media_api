package ru.job4j.social.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.social.model.Follower;

public interface FollowerRepository extends CrudRepository<Follower, Long> {
}
