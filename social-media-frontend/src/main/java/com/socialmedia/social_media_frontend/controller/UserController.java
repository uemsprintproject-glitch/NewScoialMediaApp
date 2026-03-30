package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.User;
import com.socialmedia.social_media_frontend.service.UserClientService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String getAllUsers(Model model) {
        model.addAttribute("data", service.getAllUsers());
        return "users/result";
    }

    @GetMapping("/by-id")
    public String getUserById(@RequestParam int id, Model model) {
        model.addAttribute("user", service.getUserById(id));
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
    public String getUserByUsername(@RequestParam String username, Model model){
        model.addAttribute("data", service.getUserByUsername(username));
        return "users/get-by-username";
    }

    @GetMapping("/email")
    public String getUserByEmail(@RequestParam String email, Model model){
        model.addAttribute("user", service.getUserByEmail(email));
        return "/users/get-by";
    }

    @GetMapping("/posts")
    public String getUserByPost(@RequestParam int id, Model model){
        model.addAttribute("user", service.getUserByPost(id));
        return "/users/get-by";
    }

    @GetMapping("/comment")
    public String getUserByComment(@RequestParam Integer id, Model model){
        model.addAttribute("user", service.getUserByComment(id));
        return "/users/get-by";
    }

    @GetMapping("/by-id-form")
        public String getByIdForm() {
        return "users/get-by-id";
    }

    @GetMapping("/username-form")
        public String getByUsernameForm() {
        return "users/get-by-username";
    }

    @GetMapping("/email-form")
        public String getByEmailForm() {
        return "users/get-by-email";
    }

    @GetMapping("/posts-form")
        public String getPostsForm() {
        return "users/get-by-post";
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