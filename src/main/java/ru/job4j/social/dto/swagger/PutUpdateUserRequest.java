package ru.job4j.social.dto.swagger;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.job4j.social.model.User;

@Schema(description = "Запрос на полное изменение пользователя")
public class PutUpdateUserRequest {

    @Schema(
            description = "Id пользователя",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 1,
            maxLength = 100,
            minimum = "1"
    )
    @NotNull(message = "Id пользователя не может быть пустым")
    private Long id;

    @Schema(
            description = "Полное имя пользователя",
            example = "Иван Петров",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 2,
            maxLength = 100
    )
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String fullName;

    @Schema(
            description = "Email пользователя",
            example = "ivan.petrov@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "email"
    )
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @Schema(
            description = "Пароль пользователя",
            example = "SecurePass123!",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 6,
            maxLength = 255,
            format = "password"
    )
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Hidden
    public User getUser() {
        User user = new User();
        user.setId(this.id);
        user.setFullName(this.fullName);
        user.setEmail(this.email);
        user.setPassword(this.password);

        return user;
    }
}
