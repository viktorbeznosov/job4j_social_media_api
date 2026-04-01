package ru.job4j.social.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;
import ru.job4j.social.repository.PostRepository;
import ru.job4j.social.userdetails.UserDetailsImpl;

@Slf4j
@Component("postSecurity")
public class PostSecurity {
    private final PostRepository postRepository;

    public PostSecurity(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public boolean isOwner(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Authentication is null or not authenticated");
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl currentUser)) {
            log.warn("Principal is not User object: {}", principal.getClass());
            return false;
        }

        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) return false;

        return post.getUser().getId().equals(currentUser.getId());
    }
}
