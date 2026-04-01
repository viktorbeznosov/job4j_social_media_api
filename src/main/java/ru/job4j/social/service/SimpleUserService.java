package ru.job4j.social.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.social.dto.UserWithPostsDto;
import ru.job4j.social.dto.request.SignupRequest;
import ru.job4j.social.dto.response.RegisterResponse;
import ru.job4j.social.mappers.UserWithPostsMapper;
import ru.job4j.social.model.ERole;
import ru.job4j.social.model.Role;
import ru.job4j.social.model.User;
import ru.job4j.social.repository.RoleRepository;
import ru.job4j.social.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class SimpleUserService implements UserService {

    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final RoleRepository repository;

    private final UserWithPostsMapper userWithPostsMapper;

    public SimpleUserService(
            PasswordEncoder encoder,
            UserRepository userRepository,
            RoleRepository roleRepository,
            RoleRepository repository,
            UserWithPostsMapper userWithPostsMapper
    ) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.repository = repository;
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
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(encoder.encode(user.getPassword()));
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

    public RegisterResponse signUp(SignupRequest signupRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signupRequest.getUsername()))
                || Boolean.TRUE.equals(userRepository.existsByEmail(signupRequest.getEmail()))) {
            return new RegisterResponse(HttpStatus.BAD_REQUEST, "Error: Username or Email is already taken!" );
        }

        User user = new User(
            signupRequest.getFullName(),
            signupRequest.getUsername(),
            signupRequest.getEmail(),
            encoder.encode(signupRequest.getPassword())
        );

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        Supplier<RuntimeException> supplier = () -> new RuntimeException("Error: Role is not found.");

        if (strRoles == null) {
            roles.add(roleRepository.findByName(ERole.ROLE_USER.name()).orElseThrow(supplier));
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> roles.add(roleRepository.findByName(ERole.ROLE_ADMIN.name()).orElseThrow(supplier));
                    case "mod" -> roles.add(roleRepository.findByName(ERole.ROLE_MODERATOR.name()).orElseThrow(supplier));
                    default -> roles.add(roleRepository.findByName(ERole.ROLE_USER.name()).orElseThrow(supplier));
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return new RegisterResponse(HttpStatus.OK, "Person registered successfully!" );
    }
}
