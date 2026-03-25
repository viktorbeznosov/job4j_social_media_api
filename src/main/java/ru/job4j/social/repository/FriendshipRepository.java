package ru.job4j.social.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.social.model.Friendship;
import ru.job4j.social.model.User;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

    @Query("SELECT f.friend from Friendship f where f.user.id = :userId")
    List<User> getAllFriends(@Param("userId") Long userId);

    @Query("""
        SELECT f from Friendship f 
        WHERE (f.user = :user AND f.friend = :friend)
        OR (f.user = :friend AND f.friend = :user)
    """)
    Optional<Friendship> getByUserAndFriend(@Param("user") User user, @Param("friend") User friend);
}
