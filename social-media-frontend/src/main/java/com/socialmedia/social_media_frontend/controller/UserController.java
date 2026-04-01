package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.User;
import com.socialmedia.social_media_frontend.service.UserClientService;
import com.socialmedia.social_media_frontend.util.PaginationUtils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member/users")
public class UserController {

    private final UserClientService service;

    public UserController(UserClientService service) {
        this.service = service;
    }

    @GetMapping
    public String page() {
        return "users/userAPI";
    }

    @GetMapping("/all")
    public String getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getAllUsers(),
                page,
                size,
                "/member/users/all",
                "users/result",
                model,
                Map.of());
    }

    @GetMapping("/by-id")
    public String getUserById(@RequestParam int id, Model model) {
        User user = service.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id);
        }
        model.addAttribute("user", user);
        return "users/user-by-id";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user) {
        service.createUser(user);
        return "redirect:/member/users";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user) {
        service.updateUser(user);
        return "redirect:/member/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam int id) {
        service.deleteUser(id);
        return "redirect:/member/users";
    }

    @GetMapping("/username")
    public String getUserByUsername(@RequestParam String username, Model model) {
        List<User> users = service.getUserByUsername(username);
        if (users == null || users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username);
        }
        model.addAttribute("data", users);
        return "users/get-by-username";
    public String getUserByUsername(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getUserByUsername(username),
                page,
                size,
                "/member/users/username",
                "users/get-by-username",
                model,
                Map.of("username", username));
    }

    @GetMapping("/email")
    public String getUserByEmail(@RequestParam String email, Model model) {
        User user = service.getUserByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + email);
        }
        model.addAttribute("user", user);
        return "/users/get-by";
    }

    @GetMapping("/posts")
    public String getUserByPost(@RequestParam int id, Model model) {
        User user = service.getUserByPost(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for post ID: " + id);
        }
        model.addAttribute("user", user);
        return "/users/get-by";
    }

    @GetMapping("/comment")
    public String getUserByComment(@RequestParam Integer id, Model model) {
        User user = service.getUserByComment(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for comment ID: " + id);
        }
        model.addAttribute("user", user);
        return "/users/get-by";
    }

    @GetMapping("/by-id-form")
    public String getByIdForm() {
        return "users/get-by-id";
    }

    @GetMapping("/username-form")
    public String getByUsernameForm() {
        return "users/get-by-user_name";
    }

    @GetMapping("/email-form")
    public String getByEmailForm() {
        return "users/get-by-email";
    }

    @GetMapping("/posts-form")
    public String getPostsForm() {
        return "users/get-by-post";
    }

    @GetMapping("/comments-form")
    public String getCommentsForm() {
        return "users/get-by-comment";
    }

    @GetMapping("/create-form")
    public String createForm() {
        return "users/create-user";
    }

    @GetMapping("/update-form")
    public String updateForm() {
        return "users/update-user";
    }
}