package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Comment;
import com.socialmedia.social_media_frontend.service.CommentClientService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String createComment(@ModelAttribute Comment comment) {
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
        model.addAttribute("data", List.of(comment)); // ✅ important
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
}