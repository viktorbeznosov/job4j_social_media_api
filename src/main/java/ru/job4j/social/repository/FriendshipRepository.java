package ru.job4j.social.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.social.model.Friendship;
import ru.job4j.social.model.User;

import java.util.List;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

    @Query("SELECT f.friend from Friendship f where f.user.id = :userId")
    List<User> getAllFriends(@Param("userId") Long userId);

}
