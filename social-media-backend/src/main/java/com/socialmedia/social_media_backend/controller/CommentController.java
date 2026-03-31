package com.socialmedia.social_media_backend.controller;

import com.socialmedia.social_media_backend.model.Comment;
import com.socialmedia.social_media_backend.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member/comments")
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

    @GetMapping("/by-user")
    public List<Comment> getCommentsByUser(@RequestParam Integer userId) {
        return service.getCommentByUser(userId);
    }

    @PostMapping("/create")
    public Comment createComment(@RequestBody Comment comment) {

        return service.createComment(
                comment.getUser().getUserID(),
                comment.getPost().getPostID(),
                comment.getComment_text());
    }

    @PostMapping("/update")
    public Comment updateComment(@RequestBody Comment comment) {
        return service.updateComment(comment);
    }

    @PostMapping("/delete")
    public void deleteComment(@RequestParam int id) {
        service.deleteComment(id);
    }

    @GetMapping("/by-post")
    public List<Comment> getCommentsByPost(@RequestParam int postId) {
        return service.getCommentsByPost(postId);
    }

    @GetMapping("/sorted")
    public List<Comment> getSortedComments(@RequestParam String dir) {
        return service.getSortedComments(dir);
    }

}