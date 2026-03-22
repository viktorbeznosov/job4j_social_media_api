package ru.job4j.social.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.social.model.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
}
