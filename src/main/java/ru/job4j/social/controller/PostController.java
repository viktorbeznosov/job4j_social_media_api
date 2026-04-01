package ru.job4j.social.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.social.dto.PostDto;
import ru.job4j.social.dto.request.CreatePostRequest;
import ru.job4j.social.dto.request.PatchUpdatePostRequest;
import ru.job4j.social.dto.request.PutUpdatePostRequest;
import ru.job4j.social.model.ERole;
import ru.job4j.social.model.Post;
import ru.job4j.social.service.PostService;
import ru.job4j.social.userdetails.UserDetailsImpl;

import java.util.List;

@Tag(name = "PostController", description = "PostController management APIs")
@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {
    private  static final String POST_TAG = "Post";

    private final PostService postService;

    @Operation(
        summary = "Return all posts list",
        description = "Get all posts list.",
        tags = {POST_TAG}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            content = {@Content(schema = @Schema(implementation = PostDto.class),
                mediaType = "application/json")}),
        @ApiResponse(responseCode = "400",
            content = {@Content(schema = @Schema())})
    })
    @GetMapping
    public ResponseEntity<List<PostDto>> getList() {
        List<PostDto> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }

    @Operation(
        summary = "Retrieve a Post by postId",
        description = "Get a Post object by specifying its postId. The response is Post object with postId, title, description and date of created.",
        tags = {POST_TAG}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Successfully retrieved post",
            content = @Content(schema = @Schema(implementation = PostDto.class),
                mediaType = "application/json")),
        @ApiResponse(responseCode = "400",
            description = "Invalid postId provided (must be > 0)",
            content = @Content),
        @ApiResponse(responseCode = "404",
            description = "Post not found",
            content = @Content)
    })
    @GetMapping("/{postId}")
    @PreAuthorize("@postSecurity.isOwner(#p0) or hasRole('ADMIN') or hasRole('MORERATOR')")
    public ResponseEntity<PostDto> getById(
        @PathVariable("postId")
        @NotNull
        @Min(value = 1, message = "номер ресурса должен быть 1 и более")
        Long postId
    ) {
        return postService.findById(postId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Create a new post",
        description = "Create a new post with the provided information. Returns the created post with generated ID.",
        tags = {POST_TAG}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
            description = "Post created successfully",
            content = @Content(schema = @Schema(implementation = Post.class),
                mediaType = "application/json")),
        @ApiResponse(responseCode = "400",
            description = "Invalid post data provided (validation failed)",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<Post> save(
        @Valid @RequestBody CreatePostRequest createPostRequest,
        @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        Post post = createPostRequest.getPost();
        post.setUser(currentUser.getUser());

        postService.create(post);
        var uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(post.getId())
            .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
            .location(uri)
            .body(post);
    }

    @Operation(
        summary = "Update an existing post",
        description = "Update all fields of an existing post. The post must exist in the database.",
        tags = {POST_TAG}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Post updated successfully",
            content = @Content),
        @ApiResponse(responseCode = "400",
            description = "Invalid post data provided",
            content = @Content),
        @ApiResponse(responseCode = "404",
            description = "Post not found",
            content = @Content)
    })
    @PutMapping
    @PreAuthorize("@postSecurity.isOwner(#p0.post.id) or hasRole('ADMIN') or hasRole('MORERATOR')")
    public ResponseEntity<Void> update(@RequestBody PutUpdatePostRequest updatePostRequest) {
        Post post = updatePostRequest.getPost();
        if (postService.update(post)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Partially update an existing post",
        description = "Update specific fields of an existing post. The post must exist in the database.",
        tags = {POST_TAG}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Post partially updated successfully",
            content = @Content),
        @ApiResponse(responseCode = "400",
            description = "Invalid post data provided",
            content = @Content),
        @ApiResponse(responseCode = "404",
            description = "Post not found",
            content = @Content)
    })
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@postSecurity.isOwner(#p0.post.id) or hasRole('ADMIN') or hasRole('MORERATOR')")
    public void change(@RequestBody PatchUpdatePostRequest updatePostRequest) {
        Post post = updatePostRequest.getPost();
        postService.update(post);
    }

    @Operation(
        summary = "Delete a post by postId",
        description = "Delete a post from the database by their postId.",
        tags = {POST_TAG}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204",
            description = "Post deleted successfully",
            content = @Content),
        @ApiResponse(responseCode = "400",
            description = "Invalid postId provided",
            content = @Content),
        @ApiResponse(responseCode = "404",
            description = "Post not found",
            content = @Content)
    })
    @DeleteMapping("/{postId}")
    @PreAuthorize("@postSecurity.isOwner(#p0) or hasRole('ADMIN') or hasRole('MORERATOR')")
    public ResponseEntity<Void> removeById(
        @PathVariable("postId")
        @NotNull
        @Min(value = 1, message = "номер ресурса должен быть 1 и более")
        Long postId
    ) {
        if (postService.deleteById(postId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
