package ru.job4j.social.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "followers")
public class Follower {
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
    private FollowStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Follower(User follower, User targetUser) {
        this.follower = follower;
        this.targetUser = targetUser;
    }

    private enum FollowStatus {
        pending,
        accepted,
        rejected
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = FollowStatus.pending;
    }
}
