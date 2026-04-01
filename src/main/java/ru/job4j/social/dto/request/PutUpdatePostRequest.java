package ru.job4j.social.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.job4j.social.model.Post;

@Schema(description = "Запрос на полное обновление поста")
public class PutUpdatePostRequest {

    @Schema(
            description = "Id поста",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 1,
            maxLength = 100,
            minimum = "1"
    )
    @NotNull(message = "Id поста не может быть пустым")
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Hidden
    public Post getPost() {
        Post post = new Post();
        post.setId(this.getId());
        post.setTitle(this.title);
        post.setText(this.text);
        post.setPhoto(this.photo);

        return post;
    }
}
