package ru.job4j.social.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.job4j.social.model.User;
import ru.job4j.social.userdetails.UserDetailsImpl;

@Slf4j
@Component("userSecurity")
public class UserSecurity {

    public boolean isCurrentUser(Long userId) {
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

        boolean isOwner = currentUser.getId().equals(userId);

        log.info("USER ID {}", userId);
        log.info("CURRENT USER ID {}", currentUser.getId());

        if (!isOwner) {
            log.warn("User {} attempted to access resource of user {}", currentUser.getId(), userId);
        }

        return isOwner;
    }
}
