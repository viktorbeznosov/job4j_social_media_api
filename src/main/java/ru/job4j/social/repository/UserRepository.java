package ru.job4j.social.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.social.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("""
            select user from User as user
            where user.fullName = :name AND password = :password
            """)
    Optional<User> findByNameAndPass(String name, String password);

}
