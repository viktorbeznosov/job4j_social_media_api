package ru.job4j.social.service;

import org.springframework.stereotype.Service;
import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;
import ru.job4j.social.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimplePostService implements PostService {

    private final PostRepository postRepository;

    public SimplePostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> findAll() {
        return (List<Post>) postRepository.findAll();
    }

    @Override
    public void create(User user, String title, String text, String photo) {
        Post post = new Post(user, title, text, photo);
        postRepository.save(post);
    }

    @Override
    public void create(Post post) {
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
    public boolean update(Post post) {
        return postRepository.update(post) > 0L;
    }

    @Override
    public boolean deleteById(Long id) {
        return postRepository.delete(id) > 0L;
    }
}
