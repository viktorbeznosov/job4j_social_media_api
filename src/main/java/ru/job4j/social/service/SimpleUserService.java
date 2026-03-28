package ru.job4j.social.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.social.dto.UserWithPostsDto;
import ru.job4j.social.mappers.UserWithPostsMapper;
import ru.job4j.social.model.User;
import ru.job4j.social.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleUserService implements UserService {
    private final UserRepository userRepository;

    private final UserWithPostsMapper userWithPostsMapper;

    public SimpleUserService(UserRepository userRepository, UserWithPostsMapper userWithPostsMapper) {
        this.userRepository = userRepository;
        this.userWithPostsMapper = userWithPostsMapper;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean update(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (user.getFullName() != null) {
            existingUser.setFullName(user.getFullName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }

        userRepository.save(existingUser);
        return true;
    }

    public Optional<UserWithPostsDto> findById(Long id) {
        var user = userRepository.findById(id);
        UserWithPostsDto result = null;
        if (user.isPresent()) {
            result = userWithPostsMapper.getModelFromEntity(user.get());
        }

        return Optional.of(result);
    }

    @Transactional
    public boolean deleteById(Long id) {
        return userRepository.delete(id) > 0L;
    }

    public List<UserWithPostsDto> findAll() {
        List<User> users = (List<User>) userRepository.findAll();

        return users
            .stream()
            .map(userWithPostsMapper::getModelFromEntity)
            .collect(Collectors.toList());
    }

    public List<UserWithPostsDto> findUsersWithPostsByUserIds(List<Long> userIds) {
        List<User> users = (List<User>) userRepository.findAllById(userIds);

        return users
            .stream()
            .map(userWithPostsMapper::getModelFromEntity)
            .collect(Collectors.toList());
    }
}
