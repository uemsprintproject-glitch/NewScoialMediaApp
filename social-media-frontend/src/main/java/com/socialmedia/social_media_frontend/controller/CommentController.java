package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Comment;
import com.socialmedia.social_media_frontend.model.Post;
import com.socialmedia.social_media_frontend.model.User;
import com.socialmedia.social_media_frontend.service.CommentClientService;
import com.socialmedia.social_media_frontend.util.PaginationUtils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member/comments")
public class CommentController {

    private final CommentClientService service;

    public CommentController(CommentClientService service) {
        this.service = service;
    }

    @GetMapping
    public String page() {
        return "comments/comments";
    }

    @GetMapping("/all")
    public String getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getAllComments(),
                page,
                size,
                "/member/comments/all",
                "comments/result",
                model,
                Map.of());
    }

    @PostMapping("/create")
    public String createComment(
            @RequestParam int userId,
            @RequestParam int postId,
            @RequestParam String comment_text) {

        Comment comment = new Comment();
        comment.setComment_text(comment_text);

        User user = new User();
        user.setUserID(userId);

        Post post = new Post();
        post.setPostID(postId);

        comment.setUser(user);
        comment.setPost(post);

        service.createComment(comment);

        return "redirect:/member/comments";
    }

    @PostMapping("/update")
    public String updateComment(@ModelAttribute Comment comment) {
        service.updateComment(comment);
        return "redirect:/member/comments";
    }

    @PostMapping("/delete")
    public String deleteComment(@RequestParam int id) {
        service.deleteComment(id);
        return "redirect:/member/comments";
    }

    @GetMapping("/by-id")
    public String getCommentById(
            @RequestParam int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Comment comment = service.getCommentById(id);
        if (comment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found with ID: " + id);
        }
        return PaginationUtils.renderPaginatedResult(
                List.of(comment),
                page,
                size,
                "/member/comments/by-id",
                "comments/result",
                model,
                Map.of("id", id));
    }

    @GetMapping("/by-post")
    public String getCommentsByPost(
            @RequestParam int postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getCommentsByPost(postId),
                page,
                size,
                "/member/comments/by-post",
                "comments/result",
                model,
                Map.of("postId", postId));
    }

    @GetMapping("/by-user")
    public String getCommentsByUser(
            @RequestParam int userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getCommentsByUser(userId),
                page,
                size,
                "/member/comments/by-user",
                "comments/result",
                model,
                Map.of("userId", userId));
    }

    @GetMapping("/sorted")
    public String getSortedComments(
            @RequestParam String dir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getSortedComments(dir),
                page,
                size,
                "/member/comments/sorted",
                "comments/result",
                model,
                Map.of("dir", dir));
    }

    @GetMapping("/by-id-form")
    public String getByIdForm() {
        return "comments/get-by-id";
    }

    @GetMapping("/by-post-form")
    public String getByPostForm() {
        return "comments/get-by-post";
    }

    @GetMapping("/by-user-form")
    public String getByUserForm() {
        return "comments/get-by-user";
    }

    @GetMapping("/sorted-form")
    public String getSortedForm() {
        return "comments/get-sorted";
    }

    @GetMapping("/create-form")
    public String getCreateForm() {
        return "comments/create-comment";
    }

    @GetMapping("/update-form")
    public String getUpdateForm() {
        return "comments/update-comment";
    }

    @GetMapping("/delete-form")
    public String getDeleteForm() {
        return "comments/delete-comment";
    }
}