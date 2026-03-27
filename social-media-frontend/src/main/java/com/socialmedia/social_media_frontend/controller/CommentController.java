package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Comment;
import com.socialmedia.social_media_frontend.service.CommentClientService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/by-id")
    public String getCommentById(@RequestParam int id, Model model) {
        model.addAttribute("comment", service.getCommentById(id));
        return "comments/comment-by-id";
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

    @GetMapping("/post")
    public String getCommentsByPost(@RequestParam int id, Model model) {
        model.addAttribute("comments", service.getCommentsByPost(id));
        return "comments/comments-by-post";
    }

    @GetMapping("/user")
    public String getCommentsByUser(@RequestParam int id, Model model) {
        model.addAttribute("comments", service.getCommentsByUser(id));
        return "comments/comments-by-user";
    }
}