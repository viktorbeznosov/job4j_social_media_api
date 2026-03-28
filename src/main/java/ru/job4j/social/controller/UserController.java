package ru.job4j.social.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.social.dto.UserWithPostsDto;
import ru.job4j.social.model.User;
import ru.job4j.social.service.UserService;

import java.util.List;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getList() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getByIds")
    public ResponseEntity<List<UserWithPostsDto>> getUsersWithPostsByIds(
        @RequestParam("ids")
        @NotEmpty(message = "Список ID не может быть пустым")
        List<@Min(value = 1, message = "ID должен быть больше 0")Long> ids
    ) {
        List<UserWithPostsDto> users = userService.findUsersWithPostsByUserIds(ids);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> get(
        @PathVariable("userId")
        @NotNull
        @Min(value = 1, message = "номер ресурса должен быть 1 и более")
        Long userId
    ) {
        return userService.findById(userId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> save(@Valid @RequestBody User user) {
        userService.save(user);
        var uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(user.getId())
            .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
            .location(uri)
            .body(user);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody User user) {
        if (userService.update(user)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void change(@RequestBody User user) {
        userService.update(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeById(@PathVariable long userId) {
        if (userService.deleteById(userId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
