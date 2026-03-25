package ru.job4j.social.service;

import org.springframework.stereotype.Service;
import ru.job4j.social.model.User;
import ru.job4j.social.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleUserService implements UserService {
    private final UserRepository userRepository;

    public SimpleUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean update(User user) {
        return userRepository.update(user) > 0L;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean deleteById(Long id) {
        return userRepository.delete(id) > 0L;
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }
}
