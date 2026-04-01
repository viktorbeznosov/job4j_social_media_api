package ru.job4j.social.service;

import ru.job4j.social.dto.UserWithPostsDto;
import ru.job4j.social.dto.request.SignupRequest;
import ru.job4j.social.dto.response.RegisterResponse;
import ru.job4j.social.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User save(User user);

    public boolean update(User user);

    public Optional<UserWithPostsDto> findById(Long id);

    public boolean deleteById(Long id);

    public List<UserWithPostsDto> findAll();

    public List<UserWithPostsDto> findUsersWithPostsByUserIds(List<Long> userIds);

    public RegisterResponse signUp(SignupRequest signupRequest);
}
