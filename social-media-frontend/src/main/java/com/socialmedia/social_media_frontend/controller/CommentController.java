package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Comment;
import com.socialmedia.social_media_frontend.model.Post;
import com.socialmedia.social_media_frontend.model.User;
import com.socialmedia.social_media_frontend.service.CommentClientService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public String getAllComments(Model model) {
        model.addAttribute("data", service.getAllComments());
        return "comments/result";
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
    public String getCommentById(@RequestParam int id, Model model) {
        Comment comment = service.getCommentById(id);
        if (comment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found with ID: " + id);
        }
        model.addAttribute("data", List.of(comment));
        return "comments/result";
    }

    @GetMapping("/by-post")
    public String getCommentsByPost(@RequestParam int postId, Model model) {
        model.addAttribute("data", service.getCommentsByPost(postId));
        return "comments/result";
    }

    @GetMapping("/by-user")
    public String getCommentsByUser(@RequestParam int userId, Model model) {
        model.addAttribute("data", service.getCommentsByUser(userId));
        return "comments/result";
    }

    @GetMapping("/sorted")
    public String getSortedComments(@RequestParam String dir, Model model) {

        model.addAttribute("data", service.getSortedComments(dir));

        return "comments/result";
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