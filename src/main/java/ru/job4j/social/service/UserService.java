package ru.job4j.social.service;

import ru.job4j.social.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User save(User user);

    public boolean update(User user);

    public Optional<User> findById(Long id);

    public boolean deleteById(Long id);

    public List<User> findAll();
}
