package ru.job4j.social.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.social.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
