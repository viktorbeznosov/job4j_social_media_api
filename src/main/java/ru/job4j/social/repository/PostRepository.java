package ru.job4j.social.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
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

    @Query("""
        SELECT p FROM Post p
        JOIN Subscribe s ON p.user.id = s.targetUser.id
        WHERE s.follower.id = :followerId AND s.status = 'accepted'
        ORDER BY p.createdAt DESC
    """)
    List<Post> getAllPostsOfTargetUsers(
        @Param("followerId") Long followerId,
        Pageable pageable
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
            update Post post 
            set post.title = :title, 
            post.text = :text,
            post.photo = :photo,
            post.updatedAt = CURRENT_TIMESTAMP
            where post.id = :id
            """)
    int update(@Param("id") Long id, @Param("title") String title, @Param("text") String text, @Param("photo") String photo);

    @Transactional
    @Modifying
    default int update(Post post) {
        return update(post.getId(),
                post.getTitle(),
                post.getText(),
                post.getPhoto());
    }

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
            update Post post 
            set post.photo = NULL 
            where post.id = :id
            """)
    void deleteImage(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = """
            DELETE FROM posts
            WHERE id = :id
            """, nativeQuery = true)
    int delete(@Param("id") Long id);
}
