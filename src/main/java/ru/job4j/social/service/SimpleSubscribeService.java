package ru.job4j.social.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.social.model.Friendship;
import ru.job4j.social.model.Subscribe;
import ru.job4j.social.model.User;
import ru.job4j.social.repository.FriendshipRepository;
import ru.job4j.social.repository.SubscribeRepository;

@Service
public class SimpleSubscribeService implements SubscribeService {

    private final SubscribeRepository subscribeRepository;

    private final FriendshipRepository friendshipRepository;

    public SimpleSubscribeService(
            SubscribeRepository subscribeRepository,
            FriendshipRepository friendshipRepository
    ) {
        this.subscribeRepository = subscribeRepository;
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public void subscribe(User follower, User targetUser) {
        var existingSubscribe = subscribeRepository.getByFollowerAndTargetUser(follower, targetUser);
        if (existingSubscribe.isEmpty()) {
            Subscribe subscribe = new Subscribe(follower, targetUser);
            subscribeRepository.save(subscribe);
        }
    }

    @Override
    @Transactional
    public void confirm(Long id) {
        Subscribe subscribe = subscribeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscribe not found"));
        subscribe.setStatus(Subscribe.SubscribeStatus.accepted);
        subscribeRepository.save(subscribe);
        var friendshipOptional = friendshipRepository.getByUserAndFriend(subscribe.getFollower(), subscribe.getTargetUser());
        if (friendshipOptional.isEmpty()) {
            Friendship friendship = new Friendship(subscribe.getFollower(), subscribe.getTargetUser());
            friendshipRepository.save(friendship);
        }
    }

    @Override
    @Transactional
    public void reject(Long id, User initiator) {
        Subscribe subscribe = subscribeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscribe not found"));
        var friendship = friendshipRepository.getByUserAndFriend(subscribe.getFollower(), subscribe.getTargetUser());
        if (friendship.isPresent()) {
            friendshipRepository.delete(friendship.get());
        }
        subscribe.setStatus(Subscribe.SubscribeStatus.rejected);
        if (initiator.equals(subscribe.getFollower())) {
            subscribe.setFollower(subscribe.getTargetUser());
            subscribe.setTargetUser(initiator);
        }
    }
}
