package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Post;
import com.socialmedia.social_media_frontend.service.PostClientService;
import com.socialmedia.social_media_frontend.util.PaginationUtils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Controller
@RequestMapping("/member/posts")
public class PostController {

    private final PostClientService service;

    public PostController(PostClientService service) {
        this.service = service;
    }

    @GetMapping
    public String page() {
        return "posts/posts";
    }

    @GetMapping("/all")
    public String getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getAllPosts(),
                page,
                size,
                "/member/posts/all",
                "posts/result",
                model,
                Map.of());
    }

    @GetMapping("/by-id")
    public String getPostById(@RequestParam("postId") int postId, Model model) {
        Post post = service.getPostById(postId);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found with ID: " + postId);
        }
        model.addAttribute("post", post);
        return "posts/post";
    }

    @GetMapping("/by-user")
    public String getPostsByUser(
            @RequestParam int userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getPostsByUser(userId),
                page,
                size,
                "/member/posts/by-user",
                "posts/result",
                model,
                Map.of("userId", userId));
    }

    @GetMapping("/by-id-form")
    public String getByIdForm() {
        return "posts/get-by-id";
    }

    @GetMapping("/by-user-form")
    public String getByUserForm() {
        return "posts/get-by-user";
    }

    @GetMapping("/create-form")
    public String getCreateForm() {
        return "posts/create-post";
    }

    @GetMapping("/update-form")
    public String getUpdateForm() {
        return "posts/update-post";
    }

    @GetMapping("/delete-form")
    public String getDeleteForm() {
        return "posts/delete-post";
    }

    @PutMapping("/create")
    public String createPost(@RequestParam int userId,
            @RequestParam String content) {
        service.createPost(userId, content);
        return "redirect:/member/posts";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute Post post) {
        service.updatePost(post);
        return "redirect:/member/posts";
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam int postId) {
        service.deletePost(postId);
        return "redirect:/member/posts";
    }

    @GetMapping("/sorted")
    public String getSortedPosts(
            @RequestParam String dir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getSortedPosts(dir),
                page,
                size,
                "/member/posts/sorted",
                "posts/result",
                model,
                Map.of("dir", dir));
    }

}