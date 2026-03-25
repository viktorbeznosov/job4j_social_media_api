package ru.job4j.social.service;

import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;

public interface PostService {
    public void create(User user, String title, String text, String photo);

    public void update(Long id, String title, String text, String photo);

    public void delete(Long id);
}
