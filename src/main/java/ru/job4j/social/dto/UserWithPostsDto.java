package ru.job4j.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.job4j.social.model.Post;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "User with posts Dto Information")
public class UserWithPostsDto {
    @NotBlank(message = "Id  может быть пустым")
    private Long userId;

    @NotBlank(message = "userName не может быть пустым")
    private String userName;

    @NotBlank(message = "email не может быть пустым")
    private String email;

    private List<PostDto> posts;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDto> posts) {
        this.posts = posts;
    }
}
