package ru.job4j.social.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.social.model.Follower;
import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;

import java.util.List;

public interface FollowerRepository extends CrudRepository<Follower, Long> {

    @Query("SELECT f.follower FROM Follower f WHERE f.targetUser.id = :targetUserId")
    List<User> getAllFollowers(@Param("targetUserId") Long targetUserId);
}
