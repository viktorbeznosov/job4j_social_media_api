package ru.job4j.social.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;

@Schema(description = "Запрос на создание поста")
public class CreatePostRequest {

    @Schema(
        description = "Пазвание поста",
        example = "Пост",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 2,
        maxLength = 100
    )
    @NotBlank(message = "Название поста не может быть пустым")
    private String title;

    @Schema(
            description = "Текст поста",
            example = "Текст поста",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            minLength = 2,
            maxLength = 100
    )
    private String text;

    @Schema(
            description = "Фото",
            example = "picture.jpg",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            minLength = 2,
            maxLength = 100
    )
    private String photo;

    @Schema(
            description = "Id пользователя",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1"
    )
    private Long userId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Hidden
    public Post getPost() {
        Post post = new Post();
        post.setTitle(this.title);
        post.setText(this.text);
        post.setPhoto(this.photo);
        User user = new User();
        user.setId(this.userId);
        post.setUser(user);

        return post;
    }
}
