package ru.job4j.social.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "subscribes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_follower",
            columnNames = {"follower_id", "target_user_id"}
        )
    }
)
@Schema(description = "Subscribe Model Information")
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "target_user_id", nullable = false)
    private User targetUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscribeStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Subscribe(User follower, User targetUser) {
        this.follower = follower;
        this.targetUser = targetUser;
    }

    public static enum SubscribeStatus {
        pending,
        accepted,
        rejected
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public SubscribeStatus getStatus() {
        return status;
    }

    public void setStatus(SubscribeStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subscribe subscribe1 = (Subscribe) o;
        return Objects.equals(id, subscribe1.id)
                && Objects.equals(follower.getId(), subscribe1.follower.getId())
                && Objects.equals(targetUser.getId(), subscribe1.targetUser.getId())
                && status == subscribe1.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, follower.getId(), targetUser.getId(), status);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = SubscribeStatus.pending;
        if (follower != null && targetUser != null && follower.getId().equals(targetUser.getId())) {
            throw new IllegalArgumentException("Нельзя подписаться на самого себя");
        }
    }

    @PreUpdate
    private void validate() {
        if (follower != null && targetUser != null && follower.getId().equals(targetUser.getId())) {
            throw new IllegalArgumentException("Нельзя подписаться на самого себя");
        }
    }
}
