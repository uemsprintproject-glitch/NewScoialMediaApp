package com.socialmedia.social_media_backend.controller;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/member/users")
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
    public User createUser(@Valid @RequestBody User user) {
        return service.createUser(user);
    }

    @PostMapping("/update")
    public User updateUser(@Valid @RequestBody User user) {
        return service.updateUser(user);
    }

    // @PostMapping("/delete")
    // public void deleteUser(@RequestParam int id) {
    //     service.deleteUser(id);
    // }
    
    @GetMapping("/username")
    public List<User> getUserByUsername(@RequestParam String username){
        return service.getUserByUsername(username);
    }

    @GetMapping("/email")
    public User getUserByEmail(@RequestParam String email){
        return service.getUserByEmail(email);
    }

    @GetMapping("/posts")
    public User getUserByPost(@RequestParam int id){
        return service.getUserByPostId(id);
    }

    @GetMapping("/comment")
    public User getUserByComment(@RequestParam int id){
        return service.getUserByComment(id);
    }
}