package com.socialmedia.social_media_backend.controller;

import com.socialmedia.social_media_backend.model.Post;
import com.socialmedia.social_media_backend.service.PostService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member/posts")
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Post> getAllPosts() {
        return service.getAllPosts();
    }

    @GetMapping("/by-id")
    public Post getPostById(@RequestParam("postId") int postId) {
        return service.getPostById(postId);
    }

    @GetMapping("/by-user")
    public List<Post> getPostsByUser(@RequestParam Integer userId) {
        return service.getPostsByUser(userId);
    }

    @PostMapping("/create")
    public Post createPost(@RequestParam Integer userId,
            @RequestParam String content) {
        return service.createPost(userId, content);
    }

    @PutMapping("/update")
    public Post updatePost(@RequestBody Post post) {
        return service.updatePost(post);
    }

    @PostMapping("/delete")
    public void deletePost(@RequestParam Integer postId) {
        service.deletePost(postId);
    }

}