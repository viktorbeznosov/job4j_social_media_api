package ru.job4j.social.service;

import ru.job4j.social.model.User;

public interface SubscribeService {
    public void subscribe(User follower, User targetUser);

    public void confirm(Long id);

    public void reject(Long id, User initiator);
}
