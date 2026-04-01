package ru.job4j.social.service;

import org.springframework.stereotype.Service;
import ru.job4j.social.dto.PostDto;
import ru.job4j.social.mappers.PostMapper;
import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;
import ru.job4j.social.repository.PostRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimplePostService implements PostService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    public SimplePostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    public Optional<PostDto> findById(Long id) {
        var post = postRepository.findById(id);

        PostDto result = null;
        if (post.isPresent()) {
            result = postMapper.getModelFromEntity(post.get());
        }
        return Optional.of(result);
    }

    public List<PostDto> findAll() {
        List<Post> posts = (List<Post>) postRepository.findAll();

        return posts
            .stream()
            .map(postMapper::getModelFromEntity)
            .collect(Collectors.toList());
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
        Post existingPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("Пост не найден"));

        if (post.getTitle() != null) {
            existingPost.setTitle(post.getTitle());
        }

        if (post.getText() != null) {
            existingPost.setText(post.getText());
        }

        if (post.getPhoto() != null) {
            existingPost.setPhoto(post.getPhoto());
        }

        postRepository.save(existingPost);
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        return postRepository.delete(id) > 0L;
    }
}
