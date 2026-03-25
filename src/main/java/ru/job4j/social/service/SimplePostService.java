package ru.job4j.social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;
import ru.job4j.social.repository.PostRepository;

@Service
public class SimplePostService implements PostService {

    private final PostRepository postRepository;

    public SimplePostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void create(User user, String title, String text, String photo) {
        Post post = new Post(user, title, text, photo);
        postRepository.save(post);
    }

    @Override
    public void update(Long id, String title, String text, String photo) {
        var post = postRepository.findById(id);

        if (post.isPresent()) {
            post.get().setTitle(title);
            post.get().setText(text);
            post.get().setPhoto(photo);
            postRepository.save(post.get());
        }
    }

    @Override
    public void delete(Long id) {
        postRepository.delete(id);
    }
}
