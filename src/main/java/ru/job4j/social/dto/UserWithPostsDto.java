package ru.job4j.social.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.job4j.social.model.Post;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserWithPostsDto {
    private Long userId;

    private String userName;

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

    public List<PostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDto> posts) {
        this.posts = posts;
    }
}
