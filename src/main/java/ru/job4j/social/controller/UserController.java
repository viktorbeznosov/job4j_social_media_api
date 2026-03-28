package ru.job4j.social.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.control.MappingControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.social.dto.UserWithPostsDto;
import ru.job4j.social.dto.swagger.CreateUserRequest;
import ru.job4j.social.dto.swagger.PatchUpdateUserRequest;
import ru.job4j.social.dto.swagger.PutUpdateUserRequest;
import ru.job4j.social.model.User;
import ru.job4j.social.service.UserService;

import java.util.List;

@Tag(name = "UserController", description = "UserController management APIs")
@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final String USER_TAG = "User";

    private final UserService userService;

    @Operation(
        summary = "Return all users list",
        description = "Get all users list.",
        tags = {USER_TAG}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserWithPostsDto.class), mediaType = "application/json")}),
        @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
    })
    @GetMapping
    public ResponseEntity<List<UserWithPostsDto>> getList() {
        List<UserWithPostsDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @Operation(
        summary = "Retrieve a User by userId",
        description = "Get a User object by specifying its userId. The response is User object with userId, username and date of created.",
        tags = {USER_TAG}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserWithPostsDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/getByIds")
    public ResponseEntity<List<UserWithPostsDto>> getUsersWithPostsByIds(
        @RequestParam("ids")
        @NotEmpty(message = "Список ID не может быть пустым")
        List<@Min(value = 1, message = "ID должен быть больше 0")Long> ids
    ) {
        List<UserWithPostsDto> users = userService.findUsersWithPostsByUserIds(ids);
        return ResponseEntity.ok(users);
    }

    @Operation(
        summary = "Retrieve a User by userId",
        description = "Get a User object with its posts by specifying its userId. The response is UserWithPostsDto object with userId, userName and posts.",
        tags = {USER_TAG}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user",
            content = @Content(schema = @Schema(implementation = UserWithPostsDto.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid userId provided (must be > 0)",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserWithPostsDto> get(
        @PathVariable("userId")
        @NotNull
        @Min(value = 1, message = "номер ресурса должен быть 1 и более")
        Long userId
    ) {
        return userService.findById(userId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Create a new user",
        description = "Create a new user with the provided information. Returns the created user with generated ID.",
        tags = {USER_TAG}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(schema = @Schema(implementation = User.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid user data provided (validation failed)",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<User> save(@Valid @RequestBody CreateUserRequest createUserRequest) {
        User user = createUserRequest.getUser();
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

    @Operation(
        summary = "Update an existing user",
        description = "Update all fields of an existing user. The user must exist in the database.",
        tags = {USER_TAG}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid user data provided",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content)
    })
    @PutMapping
    public ResponseEntity<Void> update(@Valid @RequestBody PutUpdateUserRequest updateUserRequest) {
        User user = updateUserRequest.getUser();
        if (userService.update(user)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Partially update an existing user",
        description = "Update specific fields of an existing user. The user must exist in the database.",
        tags = {USER_TAG}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User partially updated successfully",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid user data provided",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content)
    })
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void change(@Valid @RequestBody PatchUpdateUserRequest updateUserRequest) {
        User user = updateUserRequest.getUser();
        userService.update(user);
    }

    @Operation(
        summary = "Delete a user by userId",
        description = "Delete a user from the database by their userId.",
        tags = {USER_TAG}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid userId provided",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content)
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeById(
        @PathVariable("userId")
        @NotNull
        @Min(value = 1, message = "номер ресурса должен быть 1 и более")
        Long userId
    ) {
        if (userService.deleteById(userId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
