package com.socialmedia.social_media_backend.controller;

import com.socialmedia.social_media_backend.model.Post;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.service.UserService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member/users")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/by-id")
    public User getUserById(@RequestParam int id) {
        return service.getUserById(id);
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return service.createUser(user);
    }

    @PostMapping("/update")
    public User updateUser(@RequestBody User user) {
        return service.updateUser(user);
    }

    @PostMapping("/delete")
    public void deleteUser(@RequestParam int id) {
        service.deleteUser(id);
    }

    @GetMapping("/posts")
        public List<Post> getPostsByUser(@RequestParam int id) {
        return service.getPostsByUser(id);
    }   
}