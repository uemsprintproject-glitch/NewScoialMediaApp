package com.socialmedia.social_media_backend.controller;

import com.socialmedia.social_media_backend.model.Comment;
import com.socialmedia.social_media_backend.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member/comments")
@CrossOrigin(origins = "http://localhost:8080")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Comment> getAllComments() {
        return service.getAllComments();
    }

    @GetMapping("/by-id")
    public Comment getCommentById(@RequestParam int id) {
        return service.getCommentById(id);
    }

    @PostMapping("/create")
    public Comment createComment(
            @RequestParam int userId,
            @RequestParam int postId,
            @RequestParam String content
    ) {
        return service.createComment(userId, postId, content);
    }

    @PostMapping("/delete")
    public void deleteComment(@RequestParam int id) {
        service.deleteComment(id);
    }

    @GetMapping("/by-post")
    public List<Comment> getCommentsByPost(@RequestParam int postId) {
        return service.getCommentsByPost(postId);
    }
}