package ru.job4j.social.service;

import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;

import java.util.List;
import java.util.Optional;

public interface PostService {
    public Optional<Post> findById(Long id);

    public List<Post> findAll();

    public void create(User user, String title, String text, String photo);

    public void create(Post post);

    public void update(Long id, String title, String text, String photo);

    public boolean update(Post post);

    public boolean deleteById(Long id);
}
