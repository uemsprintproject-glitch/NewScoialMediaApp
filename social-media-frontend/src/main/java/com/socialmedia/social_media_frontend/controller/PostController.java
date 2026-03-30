package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Post;
import com.socialmedia.social_media_frontend.service.PostClientService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String getAllPosts(Model model) {
        model.addAttribute("data", service.getAllPosts());
        return "posts/result";
    }

    @GetMapping("/by-id")
    public String getPostById(@RequestParam("postId") int postId, Model model) {
        model.addAttribute("post", service.getPostById(postId));
        return "posts/post";
    }

    @GetMapping("/by-user")
    public String getPostsByUser(@RequestParam int userId, Model model) {
        model.addAttribute("data", service.getPostsByUser(userId));
        return "posts/result";
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

    @PostMapping("/create")
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
    public String getSortedPosts(@RequestParam String dir, Model model) {

        model.addAttribute("data", service.getSortedPosts(dir));

        return "posts/result";
    }
}