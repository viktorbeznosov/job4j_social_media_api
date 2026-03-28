package ru.job4j.social.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "friendships",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_friendship",
            columnNames = {"user_id", "friend_id"}
        )
    }
)
@Schema(description = "Friendship Model Information")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Friendship(User user, User friend) {
        this.user = user;
        this.friend = friend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Friendship that = (Friendship) o;
        return Objects.equals(id, that.id)
                && Objects.equals(user.getId(), that.user.getId())
                && Objects.equals(friend.getId(), that.friend.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, friend);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (user != null && friend != null && user.getId().equals(friend.getId())) {
            throw new IllegalArgumentException("Нельзя добавить в друзья самого себя");
        }
    }
}
