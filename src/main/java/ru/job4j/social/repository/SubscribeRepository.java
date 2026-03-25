package ru.job4j.social.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.social.model.Subscribe;
import ru.job4j.social.model.User;

import java.util.List;
import java.util.Optional;

public interface SubscribeRepository extends CrudRepository<Subscribe, Long> {

    @Query("SELECT f.follower FROM Subscribe f WHERE f.targetUser.id = :targetUserId")
    List<User> getAllFollowers(@Param("targetUserId") Long targetUserId);

    @Query("""
        SELECT s from Subscribe s 
        WHERE s.follower = :follower AND s.targetUser = :targetUser
        OR s.follower = :targetUser AND s.targetUser = :follower
    """)
    Optional<Subscribe> getByFollowerAndTargetUser(@Param("follower") User follower, @Param("targetUser") User targetUser);
}
