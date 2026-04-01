package ru.job4j.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.social.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("""
            select user from User as user
            where user.fullName = :name AND password = :password
            """)
    Optional<User> findByNameAndPass(String name, String password);

    // Исправленный метод update
    @Transactional
    @Modifying
    @Query("""
        UPDATE User u 
        SET u.fullName = :fullName,
            u.username = :username,
            u.email = :email,
            u.password = :password,
            u.updatedAt = CURRENT_TIMESTAMP
        WHERE u.id = :id
        """)
    int update(@Param("id") Long id,
               @Param("fullName") String fullName,
               @Param("username") String username,
               @Param("email") String email,
               @Param("password") String password);

    @Transactional
    @Modifying
    default int update(User user) {
        return update(user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword());
    }

    @Modifying
    @Query("delete from User u where u.id = :id")
    int delete(@Param("id") Long id);
}
